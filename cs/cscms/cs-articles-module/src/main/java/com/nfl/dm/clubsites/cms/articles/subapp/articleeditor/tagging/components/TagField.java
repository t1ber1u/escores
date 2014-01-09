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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.components;

import com.nfl.dm.clubsites.cms.articles.util.domain.Tag;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class TagField extends CustomField<Tag> {

    private Label label = new Label("", ContentMode.HTML);

    private NativeButton closeButton = new NativeButton();

    private HorizontalLayout layout = new HorizontalLayout();

    public TagField(Tag value, Button.ClickListener closeHandler) {
        this(value);
        closeButton.addClickListener(closeHandler);
        closeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ComponentContainer cc = (ComponentContainer) getParent();
                if (cc != null) {
                    cc.removeComponent(TagField.this);
                }
            }
        });
    }

    public TagField(Tag value) {
        addStyleName("tag");
        label.addStyleName("tag-label");
        closeButton.addStyleName("tag-close-button");
        setValue(value);
        setSizeUndefined();
    }

    @Override
    protected void setInternalValue(Tag newValue) {
        super.setInternalValue(newValue);
        label.setValue("<span>" + newValue.getTag() + "</span>");
    }

    @Override
    public Class<? extends Tag> getType() {
        return Tag.class;
    }

    @Override
    protected Component initContent() {
        layout.setSizeUndefined();
        layout.addComponent(label);
        layout.addComponent(closeButton);
        layout.setExpandRatio(label, 1f);
        return layout;
    }

    public void addCloseListener(Button.ClickListener listener) {
        closeButton.addClickListener(listener);
    }
}
