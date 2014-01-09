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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event;

import info.magnolia.event.Event;
import info.magnolia.event.EventHandler;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;

import com.vaadin.ui.Component;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class PreviewChangedEvent implements Event<PreviewChangedEvent.Handler> {

    private Component preview;

    private ViewType viewType;

    public PreviewChangedEvent(Component preview, ViewType viewType) {
        this.preview = preview;
        this.viewType = viewType;
    }

    @Override
    public void dispatch(Handler handler) {
        handler.onPreviewChanged(this);
    }

    public Component getPreview() {
        return preview;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public interface Handler extends EventHandler {
        void onPreviewChanged(PreviewChangedEvent event);
    }
}
