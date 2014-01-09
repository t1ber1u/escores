package com.nfl.dm.clubsites.cms.setup;

import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.CreateNodeTask;
import info.magnolia.module.delta.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is optional and lets you manager the versions of your module,
 * by registering "deltas" to maintain the module's configuration, or other type of content.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class NFLPoCVaadinModuleVersionHandler extends DefaultModuleVersionHandler {

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> tasks = new ArrayList<Task>(super.getExtraInstallTasks(installContext));
        tasks.add(new CreateNodeTask("authors", "", "articles", "/", "authors", NodeTypes.Content.NAME));
        tasks.add(new CreateNodeTask("series", "", "articles", "/", "series", NodeTypes.Content.NAME));
        tasks.add(new CreateNodeTask("categories", "", "articles", "/", "categories", NodeTypes.Content.NAME));
        return tasks;
    }
}