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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.header;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.NodeNameChangedEvent;
import info.magnolia.cms.core.Path;
import info.magnolia.event.EventBus;
import info.magnolia.jcr.RuntimeRepositoryException;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.api.location.DefaultLocation;
import info.magnolia.ui.api.location.LocationController;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.jackrabbit.JcrConstants;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Calendar;

/**
 * HeaderPresenter.
 */
public class HeaderPresenter implements HeaderView.Listener {
    private HeaderView headerView;
    private JcrNodeAdapter articleItem;
    private String currentTitle = "";
    private EventBus eventBus;
    private SubAppContext subAppContext;
    private LocationController locationController;

    @Inject
    public HeaderPresenter(final HeaderView headerView, final @Named(SubAppEventBus.NAME) EventBus eventBus, SubAppContext subAppContext, LocationController locationController) {
        this.headerView = headerView;
        this.eventBus = eventBus;
        this.subAppContext = subAppContext;
        this.locationController = locationController;
    }

    public HeaderView start(JcrNodeAdapter articleItem) {
        headerView.setListener(this);
        this.articleItem = articleItem;

        if (articleItem.getItemProperty("title") != null) {
            currentTitle = (String) articleItem.getItemProperty("title").getValue();
        }
        Node node = articleItem.getJcrItem();

        doHeaderUpdate(node);

        return headerView;
    }

    @Override
    public void saveDraft() {
        try {
            String oldTitle = currentTitle;
            final String title = (String) articleItem.getItemProperty("title").getValue();

            final Node node = articleItem.applyChanges();

            if (!StringUtils.equals(oldTitle, title)) {
                final String newNodeName = Path.getUniqueLabel(node.getSession(), node.getParent().getPath(), Path.getValidatedLabel(title));
                articleItem.setNodeName(newNodeName);
                NodeUtil.renameNode(node, newNodeName);
            }
            node.getSession().save();
            // LayoutEditor needs to be notified when title/node name changes so that it can update the iframe src accordingly
            eventBus.fireEvent(new NodeNameChangedEvent(node.getName()));

            doHeaderUpdate(node);

        } catch (RepositoryException e) {
            new RuntimeRepositoryException(e);
        }
    }

    private void doHeaderUpdate(Node node) {
        try {
            Calendar createdDate = node.hasProperty(JcrConstants.JCR_CREATED) ? node.getProperty(JcrConstants.JCR_CREATED).getDate() : null;
            String created = createdDate != null ? DateFormatUtils.format(createdDate, "MM/dd/yyyy, HH:mm:ss") : "unsaved";
            Calendar lastModifiedDate = NodeTypes.LastModified.getLastModified(node);
            String lastModified = lastModifiedDate != null ? DateFormatUtils.format(lastModifiedDate, "MM/dd/yyyy, HH:mm:ss") : "unsaved";

            String lastModifiedBy = NodeTypes.LastModified.getLastModifiedBy(node) != null ? NodeTypes.LastModified.getLastModifiedBy(node) : "";
            String title = node.hasProperty("title") ? node.getProperty("title").getString() : "untitled";
            HeaderInfo headerInfo = new HeaderInfo(StringEscapeUtils.escapeXml(title), lastModifiedBy, created, lastModified, lastModifiedBy, lastModifiedBy);
            headerView.update(headerInfo);
        } catch (RepositoryException e) {
            throw new RuntimeRepositoryException(e);
        }
    }

    public void launchPreview() {
        saveDraft();
        locationController.goTo(new DefaultLocation(DefaultLocation.LOCATION_TYPE_APP, subAppContext.getAppContext().getName(), "preview", articleItem.getNodeName()));
        subAppContext.close();
    }
}
