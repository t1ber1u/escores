/**
 * This file Copyright (c) 2013 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor;

import com.nfl.dm.clubsites.cms.articles.NFLNodeTypes;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.bodyeditor.BodyEditorPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.header.HeaderPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.header.HeaderView;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.layouteditor.LayoutEditorPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.NavigationPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.NavigationView;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.NavigationViewImpl;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.event.NavigationEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.promote.ArticlePromotionPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.TaggingPresenter;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.umbrellacontent.UmbrellaContentPresenter;
import info.magnolia.context.MgnlContext;
import info.magnolia.event.EventBus;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.SessionUtil;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.api.location.Location;
import info.magnolia.ui.api.view.View;
import info.magnolia.ui.contentapp.detail.DetailLocation;
import info.magnolia.ui.framework.app.BaseSubApp;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */
public class ArticleEditorSubApp extends BaseSubApp<ArticleEditorSubAppView> {

    private NavigationPresenter navigationPresenter;

    private ArticleEditorSubAppView subAppView;

    private LayoutEditorPresenter layoutEditorPresenter;

    private ArticlePromotionPresenter promotionPresenter;

    private UmbrellaContentPresenter umbrellaContentPresenter;

    private TaggingPresenter taggingPresenter;
    private BodyEditorPresenter bodyEditorPresenter;
    private EventBus eventBus;

    private Map<ViewType, View> articleEditorViews = new EnumMap<ViewType, View>(ViewType.class);

    private JcrNodeAdapter articleItem;

    private HeaderPresenter headerPresenter;

    @Inject
    public ArticleEditorSubApp(
            SubAppContext subAppContext,
            ArticleEditorSubAppView view,
            NavigationPresenter navigationPresenter,
            LayoutEditorPresenter layoutEditorPresenter,
            ArticlePromotionPresenter promotionPresenter,
            UmbrellaContentPresenter umbrellaContentPresenter,
            TaggingPresenter taggingPresenter,
            BodyEditorPresenter bodyEditorPresenter,
            HeaderPresenter headerPresenter,

            @Named(SubAppEventBus.NAME) EventBus eventBus) {
        super(subAppContext, view);
        this.subAppView = view;
        this.navigationPresenter = navigationPresenter;
        this.layoutEditorPresenter = layoutEditorPresenter;
        this.promotionPresenter = promotionPresenter;
        this.umbrellaContentPresenter = umbrellaContentPresenter;
        this.taggingPresenter = taggingPresenter;
        this.bodyEditorPresenter = bodyEditorPresenter;
        this.headerPresenter = headerPresenter;
        this.eventBus = eventBus;
        this.eventBus.addHandler(NavigationEvent.class, new NavigationEvent.Handler() {
            @Override
            public void onNavigation(NavigationEvent event) {
                View view = articleEditorViews.get(event.getViewType());
                if (view != null) {
                    subAppView.setCurrentView(view);
                }
            }
        });
    }

    @Override
    public ArticleEditorSubAppView start(Location location) {
        DetailLocation detailLocation = DetailLocation.wrap(location);
        super.start(detailLocation);
        String nodePath = detailLocation.getNodePath();
        try {
            Session session = MgnlContext.getJCRSession("articles");
            if (session.nodeExists(nodePath) && session.getNode(nodePath).getPrimaryNodeType().getName().equals(NFLNodeTypes.NFLArticle.NAME)) {
                Node node = SessionUtil.getNode("articles", nodePath);
                articleItem = new JcrNodeAdapter(node);
            } else {
                String parentPath = StringUtils.substringBeforeLast(nodePath, "/");
                parentPath = parentPath.isEmpty() ? "/" : parentPath;
                Node parent = session.getNode(parentPath);

                Node articleNode = NodeUtil.createPath(parent, detailLocation.getNodePath(), NFLNodeTypes.NFLArticle.NAME);
                articleNode.setProperty(NodeTypes.Renderable.TEMPLATE, "cs-articles-module:pages/cs-articles-module");
                articleNode.getSession().save();

                articleItem = new JcrNodeAdapter(articleNode);
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        HeaderView headerView = headerPresenter.start(articleItem);
        subAppView.setHeader(headerView);

        NavigationView navigationView = navigationPresenter.start();
        subAppView.setNavigator(navigationView);

        articleEditorViews.put(ViewType.LayoutEditor, layoutEditorPresenter.start(articleItem));
        articleEditorViews.put(ViewType.Promote, promotionPresenter.start(articleItem));
        articleEditorViews.put(ViewType.Summary, umbrellaContentPresenter.start(articleItem));
        articleEditorViews.put(ViewType.Tags, taggingPresenter.start(articleItem));
        articleEditorViews.put(ViewType.BodyEditor, bodyEditorPresenter.start(articleItem));

        subAppView.setCurrentView(articleEditorViews.get(ViewType.BodyEditor));
        ((NavigationViewImpl) navigationView).getComponent(2).addStyleName("selected");
        return subAppView;
    }

    @Override
    public String getCaption() {
        return "Edit " + getCurrentLocation().getNodePath();
    }

    @Override
    public DetailLocation getCurrentLocation() {
        return DetailLocation.wrap(super.getCurrentLocation());
    }

    @Override
    public boolean supportsLocation(Location location) {
        DetailLocation itemLocation = DetailLocation.wrap(location);
        String currentPath = getCurrentLocation().getNodePath();
        return currentPath.equals(itemLocation.getNodePath());
    }
}
