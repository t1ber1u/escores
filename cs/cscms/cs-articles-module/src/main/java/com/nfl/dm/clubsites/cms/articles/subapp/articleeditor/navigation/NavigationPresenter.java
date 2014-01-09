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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation;

import info.magnolia.event.EventBus;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.navigation.event.NavigationEvent;
import info.magnolia.ui.api.app.SubAppEventBus;

import javax.inject.Inject;
import javax.inject.Named;

import com.vaadin.ui.Component;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class NavigationPresenter implements NavigationView.Listener {

    private NavigationView view;

    private EventBus eventBus;

    @Inject
    public NavigationPresenter(NavigationView view, @Named(SubAppEventBus.NAME) EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
        this.eventBus.addHandler(PreviewChangedEvent.class, new PreviewChangedEvent.Handler() {
            @Override
            public void onPreviewChanged(PreviewChangedEvent event) {
                onNavigationPreviewChanged(event.getViewType(), event.getPreview());
            }
        });
    }

    public NavigationView start() {
        view.setListener(this);
        return view;
    }

    public final void onNavigationPreviewChanged(ViewType type, Component preview) {
        view.setPreview(type, preview);
    }

    @Override
    public void onNavigation(ViewType type) {
        eventBus.fireEvent(new NavigationEvent(type));
    }
}
