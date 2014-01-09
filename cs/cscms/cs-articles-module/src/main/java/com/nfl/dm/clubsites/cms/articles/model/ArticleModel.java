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
package com.nfl.dm.clubsites.cms.articles.model;

import info.magnolia.cms.util.ContentUtil;
import info.magnolia.cms.util.ContentWrapper;
import info.magnolia.cms.util.NodeMapWrapper;
import info.magnolia.dam.asset.functions.DamTemplatingFunctions;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.model.RenderingModelImpl;
import info.magnolia.rendering.template.RenderableDefinition;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 27/12/13
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class ArticleModel<RD extends RenderableDefinition> extends RenderingModelImpl<RD> {

    private DamTemplatingFunctions templatingFunctions;

    @Inject
    public ArticleModel(Node content, RD definition, RenderingModel<?> parent, DamTemplatingFunctions templatingFunctions) {
        super(content, definition, parent);
        this.templatingFunctions = templatingFunctions;
    }

    public List<ContentWrapper> getSections() throws RepositoryException {
        Node articleNode = getNode();
        List<ContentWrapper> result = new ArrayList<ContentWrapper>();
        if (articleNode.hasNode("body")) {
            NodeIterator it = articleNode.getNode("body").getNodes();
            while (it.hasNext()) {
                Node section = it.nextNode();
                if (section.hasProperty("image")) {
                    String imageLink = templatingFunctions.getAssetLinkForId(section.getProperty("image").getString());
                    section.setProperty("image", imageLink);
                }
                result.add(new NodeMapWrapper(ContentUtil.asContent(section), section.getPath()));
            }
        }

        return result;
    }
}
