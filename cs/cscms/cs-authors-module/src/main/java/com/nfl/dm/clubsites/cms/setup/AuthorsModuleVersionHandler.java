package com.nfl.dm.clubsites.cms.setup;

import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.delta.DeltaBuilder;
import info.magnolia.module.delta.ModuleBootstrapTask;
import info.magnolia.module.delta.RemoveNodeTask;
import info.magnolia.repository.RepositoryConstants;

/**
 * This class is optional and lets you manager the versions of your module,
 * by registering "deltas" to maintain the module's configuration, or other type of content.
 * If you don't need this, simply remove the reference to this class in the module descriptor xml.
 */
public class AuthorsModuleVersionHandler extends DefaultModuleVersionHandler {

    public AuthorsModuleVersionHandler() {

        register(DeltaBuilder.update("1.0.1", "")
                .addTask(new RemoveNodeTask("Remove outdated 'authors' configuration", "", RepositoryConstants.CONFIG, "/server/URI2RepositoryMapping/mappings/authors"))
                .addTask(new RemoveNodeTask("Remove outdated 'authors' configuration", "", RepositoryConstants.CONFIG, "/server/URI2RepositoryMapping/mappings/cs-authors-module"))
                .addTask(new RemoveNodeTask("Remove outdated 'authors' configuration", "", RepositoryConstants.CONFIG, "/modules/ui-admincentral/config/appLauncherLayout/groups/edit/apps/authors"))
                .addTask(new RemoveNodeTask("Remove outdated 'authors' configuration", "", RepositoryConstants.CONFIG, "/modules/cs-authors-module"))
                .addTask(new ModuleBootstrapTask()));
    }
}