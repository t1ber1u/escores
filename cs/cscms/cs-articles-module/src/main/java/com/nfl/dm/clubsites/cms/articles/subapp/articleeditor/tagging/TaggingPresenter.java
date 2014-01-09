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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging;

import com.nfl.dm.clubsites.cms.articles.categorization.NFLCategoryGeneratorCommand;
import com.nfl.dm.clubsites.cms.articles.categorization.OpenCalaisRESTPost;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.event.NavigationEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.components.TagField;
import com.nfl.dm.clubsites.cms.articles.util.ArticleService;
import com.nfl.dm.clubsites.cms.articles.util.domain.Tag;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import info.magnolia.cms.core.Path;
import info.magnolia.commands.CommandsManager;
import info.magnolia.event.EventBus;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 14:47
 * To change this template use File | Settings | File Templates.
 */
public class TaggingPresenter implements TaggingView.Listener {

    private TaggingView view;

    private EventBus eventBus;
    private ArticleService service;

    private JcrNodeAdapter articleItem;

    @Inject
    public TaggingPresenter(TaggingView view, @Named(SubAppEventBus.NAME) EventBus eventBus, ArticleService service) {
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;
        view.setListener(this);
    }

    public TaggingView start(JcrNodeAdapter articleItem) {
        this.articleItem = articleItem;

        initTags();
        renderTags(articleItem);
        generatePreview();

        this.eventBus.addHandler(NavigationEvent.class, new NavigationEvent.Handler() {
            @Override
            public void onNavigation(NavigationEvent event) {
                if (event.getViewType() == ViewType.Tags){
                    initTags();
                }
            }
        });

        view.setArticleItem(articleItem);
        return view;
    }

    private void renderTags(JcrNodeAdapter articleItem) {
        view.removeAll();
        List<Tag> tags = service.getTagsForNode(articleItem.getJcrItem());
        for (Tag tag : tags) {
            switch (tag.getType()) {
            case custom:
                view.addCustomTag(tag);
                break;
            case person:
                view.addPersonTag(tag);
                break;
            case organization:
                view.addTeamTag(tag);
                break;
            case schedule:
                view.addScheduleTag(tag);
                break;
            }
        }
    }

    private void generatePreview() {
        final CssLayout preview = new CssLayout();
        preview.addStyleName("tagging-preview");
        preview.setCaption("Tags");
        List<Tag> tags = service.getTagsForNode(articleItem.getJcrItem());
        for (int i = 0; i < Math.min(5, tags.size()); ++i) {
            final Tag tag = tags.get(i);
            preview.addComponent(new TagField(tag, new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    onTagRemoved(tag);
                    renderTags(articleItem);
                }
            }));
        }
        eventBus.fireEvent(new PreviewChangedEvent(preview, ViewType.Tags));
    }

    private void initTags() {
        Node articleNode = articleItem.getJcrItem();
        try {
            if (!articleNode.hasNode("categories")) {
                if (articleNode.hasProperty("body") && !articleNode.getProperty("body").equals(null)) {
                    getCategorization(articleNode);
                }
            } else {
                Node nodeCategories = articleNode.getNode("categories");
                PropertyIterator categories = nodeCategories.getProperties();
                if (categories.getSize() <= 7 && articleNode.hasProperty("body") && !articleNode.getProperty("body").equals(null)) {
                    getCategorization(articleNode);
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategorization(Node articleNode) throws Exception {
        Map<String, ArrayList<String>> categorization;
        OpenCalaisRESTPost openCalaisRESTPost = new OpenCalaisRESTPost();
        categorization = openCalaisRESTPost.execute(articleNode);

        CommandsManager cm = CommandsManager.getInstance();
        NFLCategoryGeneratorCommand command = (NFLCategoryGeneratorCommand) cm.getCommand("categorization-nflGenerateCategories");
        command.setRepository("articles");
        command.setPath("/");
        command.execute(categorization, articleNode);
    }

    public Tag addTag(String s, TagType type) throws RepositoryException {
        Node tag = NodeUtil.createPath(getTagNode(), createTagName(s), NodeTypes.Content.NAME, true);
        tag.setProperty("displayName", s);
        tag.setProperty("name", type.toString());
        tag.getSession().save();
        return new Tag(tag.getIdentifier(), s, type);
    }

    private Node getTagNode() throws RepositoryException {
        return articleItem.getJcrItem().getNode("categories");
    }

    private String createTagName(String s) throws RepositoryException {
        return Path.getUniqueLabel(getTagNode().getSession(), getTagNode().getPath(), Path.getValidatedLabel(s));
    }

    @Override
    public void onTagRemoved(Tag tag) {
        try {
            Node articleNode = NodeUtil.getNodeByIdentifier("articles", articleItem.getJcrItem().getIdentifier());
            articleNode.getNode("categories").getProperty(tag.getTag()).remove();
            articleNode.getSession().save();
            generatePreview();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTagCreated(String tag) {
        try {
            view.addCustomTag(addTag(tag, TagType.custom));
            generatePreview();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }
}
