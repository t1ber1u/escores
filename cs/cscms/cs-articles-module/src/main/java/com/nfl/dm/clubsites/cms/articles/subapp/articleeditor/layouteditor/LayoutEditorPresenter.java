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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.layouteditor;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.NodeNameChangedEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import info.magnolia.context.MgnlContext;
import info.magnolia.event.EventBus;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.RepositoryException;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
public class LayoutEditorPresenter {

    private EventBus eventBus;

    private LayoutEditorView view;

    private String server;

    @Inject
    public LayoutEditorPresenter(final LayoutEditorView view, final @Named(SubAppEventBus.NAME) EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        this.eventBus.addHandler(NodeNameChangedEvent.class, new NodeNameChangedEvent.Handler() {

            @Override
            public void onNodeNameChanged(NodeNameChangedEvent event) {
                view.setArticleUrl(server + event.getNewNodeName());
            }
        });
    }

    public LayoutEditorView start(JcrNodeAdapter articleItem) {
        final String title = "LAYOUT EDITOR";

        Label previewLabel = new Label(title, ContentMode.HTML);
        previewLabel.setSizeUndefined();
        previewLabel.addStyleName("navigation-label");
        previewLabel.addStyleName("ax-shape");

        eventBus.fireEvent(new PreviewChangedEvent(previewLabel, ViewType.LayoutEditor));

        try {
            server = MgnlContext.getWebContext().getRequest().getRequestURL().toString();
            server = server.substring(0, server.indexOf("/.magnolia")) + "/article?contentPath=";
            view.setArticleUrl(server + articleItem.getJcrItem().getName());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return view;
    }

}
