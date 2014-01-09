package com.nfl.dm.clubsites.cms.articles;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * This class is optional and represents the configuration for the nfl-poc-vaadin module.
 * By exposing simple getter/setter/adder methods, this bean can be configured via content2bean
 * using the properties and node from <tt>config:/modules/nfl-poc-vaadin</tt>.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class NFLPoCVaadinModule implements ModuleLifecycle {
    @Override
    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        try {
            Session jcrSession = MgnlContext.getJCRSession("nfl");
            Node root = jcrSession.getRootNode();
            Node authors = root.getNode("authors");
            if (authors.getNodes().getSize() == 0) {
                Node newAuthor = NodeUtil.createPath(authors, "/brogers", NodeTypes.Content.NAME);
                newAuthor.setProperty("firstName", "Brendan");
                newAuthor.setProperty("lastName", "Rogers");
            }

            if (authors.getNodes().getSize() == 1) {
                Node newAuthor = NodeUtil.createPath(authors, "/jdoe", NodeTypes.Content.NAME);
                newAuthor.setProperty("firstName", "John");
                newAuthor.setProperty("lastName", "Doe");
            }

            Node series = root.getNode("series");
            if (series.getNodes().getSize() == 0) {
                Node newSeries = NodeUtil.createPath(series, "/gameday1", NodeTypes.Content.NAME);
                newSeries.setProperty("title", "Gameday");
            }

            if (series.getNodes().getSize() == 1) {
                Node newSeries = NodeUtil.createPath(series, "/gameday2", NodeTypes.Content.NAME);
                newSeries.setProperty("title", "Gameday2");
            }

            jcrSession.save();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
    }

}
