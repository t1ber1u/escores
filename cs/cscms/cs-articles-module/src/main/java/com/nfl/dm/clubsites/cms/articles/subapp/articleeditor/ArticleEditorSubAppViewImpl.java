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

import info.magnolia.ui.api.view.View;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.header.HeaderView;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.NavigationView;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 19:27
 * To change this template use File | Settings | File Templates.
 */
public class ArticleEditorSubAppViewImpl implements ArticleEditorSubAppView {

    private HorizontalLayout layout = new HorizontalLayout();
    private VerticalLayout wrapper = new VerticalLayout();

    public ArticleEditorSubAppViewImpl() {
        layout.addStyleName("article-editor");
        layout.setWidth("100%");
        // calculate height dynamically using browser window height. Not doing so will display a gray block at the bottom of the app.
        layout.setHeight(Page.getCurrent().getBrowserWindowHeight(), Unit.PIXELS);
        layout.setMargin(true);
        layout.setSpacing(true);
    }

    @Override
    public Component asVaadinComponent() {
        return wrapper;
    }

    @Override
    public void setNavigator(NavigationView navigationView) {
        layout.addComponentAsFirst(navigationView.asVaadinComponent());
        wrapper.addComponent(layout);
    }

    @Override
    public void setCurrentView(View view) {
        if (layout.getComponentCount() > 1) {
            layout.removeComponent(layout.getComponent(1));
        }
        layout.addComponent(view.asVaadinComponent());
        layout.setExpandRatio(view.asVaadinComponent(), 1f);
        wrapper.addComponent(layout);
    }

    @Override
    public void setHeader(HeaderView headerView) {
        wrapper.addComponent(headerView.asVaadinComponent());
    }
}
