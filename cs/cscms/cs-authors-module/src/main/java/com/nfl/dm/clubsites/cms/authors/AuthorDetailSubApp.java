package com.nfl.dm.clubsites.cms.authors;

import info.magnolia.cms.core.version.VersionManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.event.EventBus;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.event.AdmincentralEventBus;
import info.magnolia.ui.contentapp.ContentSubAppView;
import info.magnolia.ui.contentapp.detail.DetailEditorPresenter;
import info.magnolia.ui.contentapp.detail.DetailLocation;
import info.magnolia.ui.contentapp.detail.DetailSubApp;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Authors detail sub-app - just sets the proper caption.
 */
public class AuthorDetailSubApp extends DetailSubApp {

    private static final Logger log = LoggerFactory.getLogger(AuthorDetailSubApp.class);

    private final VersionManager versionManager;
    private final SimpleTranslator i18n;

    @Inject
    protected AuthorDetailSubApp(final SubAppContext subAppContext, final ContentSubAppView view, @Named(AdmincentralEventBus.NAME) EventBus adminCentralEventBus, DetailEditorPresenter workbench, VersionManager versionManager, SimpleTranslator i18n) {
        super(subAppContext, view, adminCentralEventBus, workbench, i18n);
        this.versionManager = versionManager;
        this.i18n = i18n;
    }

    @Override
    protected String getBaseCaption(DetailLocation location) {
        String baseCaption = super.getBaseCaption(location);
        try {
            if (MgnlContext.getJCRSession(getWorkspace()).nodeExists(location.getNodePath())) {
                Node author = MgnlContext.getJCRSession(getWorkspace()).getNode(location.getNodePath());
                // Get specific Node version if needed
                if (StringUtils.isNotBlank(location.getVersion())) {
                    author = versionManager.getVersion(author, location.getVersion());
                }
                String firstName = PropertyUtil.getString(author, AuthorsNodeTypes.Author.PROPERTY_FIRST_NAME, "");
                String lastName = PropertyUtil.getString(author, AuthorsNodeTypes.Author.PROPERTY_LAST_NAME, "");
                if (StringUtils.isNotBlank(firstName + lastName)) {
                    baseCaption = firstName + " " + lastName;
                }
            } else {
                baseCaption = i18n.translate("authors.detail.label.newAuthor");
            }
        } catch (RepositoryException e) {
            log.warn("Could not set Sub App Tab Caption for item : {}", location.getNodePath(), e);
        }
        return baseCaption;
    }
}
