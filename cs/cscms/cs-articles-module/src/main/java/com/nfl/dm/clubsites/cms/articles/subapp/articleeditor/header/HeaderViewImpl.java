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

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

/**
 * HeaderViewImpl.
 */
public class HeaderViewImpl implements HeaderView {
    private CssLayout header = new CssLayout();
    private Listener listener;
    private Label title;

    public HeaderViewImpl() {
        header.addStyleName("nfl-header");
    }

    @Override
    public Component asVaadinComponent() {
        return header;
    }

    @Override
    public void update(HeaderInfo update) {
        header.removeAllComponents();
        Label title = new Label("<span id='story'>STORY:</span><span id='title'>" + update.getTitle() + "</span>", ContentMode.HTML);
        title.addStyleName("header-story");
        header.addComponent(title);

        Label metaData = new Label("<span id='created-by'>Created by [" + update.getCreatedBy() + "] on " + update.getCreated() + "</span>" +
                "<div id='last-opened-by'>Last Opened by [" + update.getLastOpenedBy() + "] on " + update.getLastSaved() + "</div>" +
                "<div id='modified-by'>Modified by [" + update.getLastModifiedBy() + "] on " + update.getLastSaved() + "</div>",
                ContentMode.HTML);
        metaData.addStyleName("header-metadata");
        header.addComponent(metaData);

        createButtons();
        Label lastSaved = new Label("<div id='title'>LAST SAVED:</div><span id='time'>" + update.getLastSaved() + "</span>", ContentMode.HTML);
        lastSaved.addStyleName("last-saved");
        header.addComponent(lastSaved);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void createButtons() {
        Button saveDraftButton = new Button("Save Draft", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.saveDraft();
            }
        });
        saveDraftButton.addStyleName("header-button");
        saveDraftButton.addStyleName("save-draft-button");
        header.addComponent(saveDraftButton);

        Button livePreviewButton = new Button("Live Preview", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.launchPreview();
            }
        });
        livePreviewButton.addStyleName("header-button");
        header.addComponent(livePreviewButton);

        Button publishButton = new Button("Publish Story", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // TODO implement it
            }
        });
        publishButton.addStyleName("header-button");
        header.addComponent(publishButton);
    }

}
