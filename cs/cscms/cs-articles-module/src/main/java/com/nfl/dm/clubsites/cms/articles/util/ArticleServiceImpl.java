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
package com.nfl.dm.clubsites.cms.articles.util;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.TagType;
import com.nfl.dm.clubsites.cms.articles.util.domain.Author;
import com.nfl.dm.clubsites.cms.articles.util.domain.Series;
import com.nfl.dm.clubsites.cms.articles.util.domain.Tag;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;

import javax.inject.Singleton;
import javax.jcr.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ArticleServiceImpl implements ArticleService {

    @Override
    public List<Author> getAuthors() {
        List<Author> authors = new LinkedList<Author>();
        try {
            for (Node node : NodeUtil.getNodes(MgnlContext.getJCRSession("authors").getNode("/"))) {
                if (node.getPrimaryNodeType().getName().equals("mgnl:author")) {
                    Author author = getAuthorFromNode(node);
                    authors.add(author);
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return authors;
    }

    @Override
    public List<Series> getSeries() {
        List<Series> series = new LinkedList<Series>();
        try {
            for (Node node : NodeUtil.getNodes(MgnlContext.getJCRSession("config").getNode("/modules/cs-articles-module/series"))) {
                Series series1 = getSeriesFromNode(node);
                series.add(series1);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return series;
    }

    @Override
    public void addAuthor(Author author) {
        try {
            Node node = NodeUtil.createPath(MgnlContext.getJCRSession("articles").getNode("/authors"), author.getLastName(), NodeTypes.Content.NAME);
            node.setProperty("firstName", author.getFirstName());
            node.setProperty("lastName", author.getFirstName());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSeries(Series series) {
        try {
            Node node = NodeUtil.createPath(MgnlContext.getJCRSession("articles").getNode("/series"), series.getTitle(), NodeTypes.Content.NAME);
            node.setProperty("title", series.getTitle());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Author getAuthorById(String author) {
        if (author != null) {
            try {
                Node node = NodeUtil.getNodeByIdentifier("authors", author);
                return getAuthorFromNode(node);
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Series getSeriesById(String series) {
        if (series != null) {
            try {
                //Node node = NodeUtil.getNodeByIdentifier("articles", series);
                for (Node node : NodeUtil.getNodes(MgnlContext.getJCRSession("config").getNode("/modules/cs-articles-module/series"))) {
                    if (node.getIdentifier().equals(series)) return getSeriesFromNode(node);
                }
            } catch (RepositoryException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<Tag> getTagsForNode(Node node) {
        List<Tag> tagList = new LinkedList<Tag>();
        try {
            if (node.hasNode("categories")) {
                PropertyIterator pi = node.getNode("categories").getProperties();
                while (pi.hasNext()) {
                    Property property = pi.nextProperty();
                    if (!property.getName().contains("jcr:")) {
                        String query = "select * from [mgnl:category] where [mgnl:deleted] is null and displayName = '" + property.getValue().getString() +"'";
                        NodeIterator categories = QueryUtil.search("category", query, "JCR-SQL2", "mgnl:category");

                        Node systemCategory = null;
                        while (categories.hasNext()) {
                            systemCategory = categories.nextNode();
                        }

                        if (systemCategory != null) {
                            tagList.add(tagFromNode(systemCategory));
                        }
                    }
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return tagList;
    }

    private Tag tagFromNode(Node tag) {
        try {
            String tagMsg = tag.getProperty("displayName").getString();
            TagType type = TagType.valueOf(tag.getProperty("name").getString());
            return new Tag(tag.getIdentifier(), tagMsg, type);
        } catch (RepositoryException e) {
            return null;
        }
    }

    private Author getAuthorFromNode(Node node) throws RepositoryException {
        String firstName = node.getProperty("firstName").getString();
        String lastName = node.getProperty("lastName").getString();
        return new Author(node.getIdentifier(), firstName, lastName);
    }

    private Series getSeriesFromNode(Node node) throws RepositoryException {
        String title = node.getProperty("title").getString();
        return new Series(node.getIdentifier(), title);
    }
}
