package com.nfl.dm.clubsites.cms.articles;

import info.magnolia.cms.core.version.VersionManager;
import info.magnolia.context.MgnlContext;
import info.magnolia.event.EventBus;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.event.AdmincentralEventBus;
import info.magnolia.ui.api.location.LocationController;
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
 * Article detail sub-app - just sets the proper caption.
 */
public class ArticleDetailSubApp extends DetailSubApp {

    private static final Logger log = LoggerFactory.getLogger(ArticleDetailSubApp.class);

    private final VersionManager versionManager;
    private final SimpleTranslator i18n;
    private LocationController locationController;

    @Inject
    protected ArticleDetailSubApp(final SubAppContext subAppContext, final ArticleDetailSubAppView view, @Named(AdmincentralEventBus.NAME) EventBus adminCentralEventBus, DetailEditorPresenter workbench, VersionManager versionManager, SimpleTranslator i18n, LocationController locationController) {
        super(subAppContext, view, adminCentralEventBus, workbench, i18n);
        this.versionManager = versionManager;
        this.i18n = i18n;
        this.locationController = locationController;
    }

    /*
    @Override
    protected void onSubAppStart() {
        getView().setListener(this);
        getView().addPreviewButton("Preview Article");
    }
    */

    @Override
    protected String getBaseCaption(DetailLocation location) {
        String baseCaption = super.getBaseCaption(location);
        try {
            if (MgnlContext.getJCRSession(getWorkspace()).nodeExists(location.getNodePath())) {
                Node article = MgnlContext.getJCRSession(getWorkspace()).getNode(location.getNodePath());
                // Get specific Node version if needed
                if (StringUtils.isNotBlank(location.getVersion())) {
                    article = versionManager.getVersion(article, location.getVersion());
                }
                String title = PropertyUtil.getString(article, ArticlesNodeTypes.Article.PROPERTY_TITLE, "no-article-name-set");
                if (StringUtils.isNotBlank(title)) {
                    baseCaption = title;
                }
            } else {
                baseCaption = i18n.translate("articles.detail.label.newArticle");
            }
        } catch (RepositoryException e) {
            log.warn("Could not set Sub App Tab Caption for item : {}", location.getNodePath(), e);
        }
        return baseCaption;
    }

    /*
    @Override
    public ArticleDetailSubAppView getView() {
        return (ArticleDetailSubAppView) super.getView();
    }

    @Override
    public void previewArticle(String title) {
        locationController.goTo(new DefaultLocation(DefaultLocation.LOCATION_TYPE_APP, getAppContext().getName(), "preview", title));
    }
    */
}
