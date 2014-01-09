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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.layouteditor.components;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 26/12/13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class ParagraphTitleLabel extends Label {

    private String paragraphTitleTemplate;

    public ParagraphTitleLabel(String value) {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(getClass().getResourceAsStream("/com/nfl/poc/subapp/articleeditor/layouteditor/templates/paragraph_title_template.html"), writer, null);
            paragraphTitleTemplate = writer.toString();
            setValue(value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentMode(ContentMode.HTML);
    }

    @Override
    public void setValue(String newStringValue) {
        if (newStringValue == null || paragraphTitleTemplate == null) {
            super.setValue(newStringValue);
        } else {
            super.setValue(String.format(paragraphTitleTemplate, newStringValue));
        }
    }
}
