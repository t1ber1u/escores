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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.bodyeditor;

import java.util.Arrays;
import java.util.List;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class BodyEditorViewImpl implements BodyEditorView {

    private VerticalLayout toolbar = new VerticalLayout();

    private HorizontalLayout layout = new HorizontalLayout();

    private VerticalLayout content = new VerticalLayout();

    private Listener listener;

    public BodyEditorViewImpl() {

        layout.setSizeFull();
        layout.setWidth("100%");

        toolbar.setWidth("180px");
        content.setWidth("100%");
        content.setHeight("100%");

        layout.addComponent(toolbar);
        layout.addComponent(content);

        layout.setExpandRatio(content, 1f);
    }

    public void createToolbar() {
        toolbar.setMargin(true);

        toolbar.addComponent(createToolbarFormattingElements());
        toolbar.addComponent(createToolbarContentElements());
        toolbar.addComponent(createToolbarAssetsElements());

    }

    private Layout createToolbarFormattingElements() {
        CssLayout wrapper = new CssLayout();
        wrapper.addStyleName("toolbar-elements");
        wrapper.addStyleName("toolbar-formatting");

        for (int i = 0; i < 4; i++) {
            Button addButton = new Button(null, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    notImplemented();
                }
            });
            addButton.setStyleName("toolbar-formatting-button");
            ThemeResource tableResource = new ThemeResource("img/placeholder.png");
            addButton.setIcon(tableResource);

            wrapper.addComponent(addButton);
        }

        return wrapper;
    }

    private Layout createToolbarContentElements() {
        ThemeResource placeHolder = new ThemeResource("img/placeholder.png");
        CssLayout wrapper = new CssLayout();
        wrapper.addStyleName("toolbar-elements");
        Button addTableButton = new Button("table", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                notImplemented();
            }
        });
        addTableButton.setStyleName("ax-shape-button");
        ThemeResource tableResource = new ThemeResource("img/table.png");
        addTableButton.setIcon(tableResource);

        wrapper.addComponent(addTableButton);

        Button addRuleButton = new Button("rule", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                notImplemented();
            }
        });
        addRuleButton.setStyleName("ax-shape-button");
        addRuleButton.setIcon(placeHolder);

        wrapper.addComponent(addRuleButton);

        Button addParagraphButton = new Button("section", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.createSection("Section");
            }
        });
        addParagraphButton.setStyleName("ax-shape-button");
        ThemeResource sectionResource = new ThemeResource("img/section_sign.gif");
        addParagraphButton.setIcon(sectionResource);

        wrapper.addComponent(addParagraphButton);

        Button addPullQuoteButton = new Button("pullquote", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                notImplemented();
            }
        });
        addPullQuoteButton.setStyleName("ax-shape-button");
        addPullQuoteButton.setIcon(placeHolder);

        wrapper.addComponent(addPullQuoteButton);
        return wrapper;
    }

    private Layout createToolbarAssetsElements() {
        final CssLayout wrapper = new CssLayout();
        wrapper.addStyleName("toolbar-elements");

        final Button addPhotographButton = new Button("photograph");
        addPhotographButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                listener.openMediaGallery();
            }
        });
        addPhotographButton.setStyleName("ax-shape-button");
        ThemeResource sectionResource = new ThemeResource("img/photograph.png");
        addPhotographButton.setIcon(sectionResource);
        wrapper.addComponent(addPhotographButton);

        List<String> assetElements = Arrays.asList(new String[] { "video", "poll", "related link", "iFrame" });
        for (String name : assetElements) {
            Button addButton = new Button(name, new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    notImplemented();
                }
            });
            addButton.setStyleName("ax-shape-button");
            ThemeResource tableResource = new ThemeResource("img/placeholder.png");
            addButton.setIcon(tableResource);

            wrapper.addComponent(addButton);
        }

        return wrapper;
    }

    @Override
    public Component asVaadinComponent() {
        return layout;
    }

    @Override
    public void clear() {
        content.removeAllComponents();
    }

    @Override
    public void addEditor(Component editor) {
        content.addComponent(editor);
    }

    @Override
    public void setListener(BodyEditorView.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void addHiddenComponent(Component c) {
        c.addStyleName("hidden-component");
        layout.addComponent(c);
    }

    private void notImplemented() {
        Notification.show("", "Not implemented yet", Notification.Type.WARNING_MESSAGE);
    }
}
