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
package com.nfl.poc.editor.app;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * The editor app view. It comprises an iframe loading an internal "nfl-editor" resource. Such resource resolves to &lt;context-app&gt;/.magnolia/nfl-editor which is mapped to {@link com.nfl.poc.editor.servlet.NFLEditorServlet}.
 * All resources under /.magnolia are protected by default. The mapping is defined in the module descriptor at /nfl-poc-app/src/main/resources/META-INF/magnolia/nfl-poc.xml (see the servlet tag)
 */
public class NFLEditorViewImpl implements NFLEditorView {

    private final VerticalLayout root = new VerticalLayout();

    public NFLEditorViewImpl() {
        root.setSizeFull();
        // Vaadin Iframe component
        BrowserFrame editor = new BrowserFrame(null, new ExternalResource("nfl-editor"));
        editor.setSizeFull();
        root.addComponent(editor);
    }

    @Override
    public Component asVaadinComponent() {
        return root;
    }

}