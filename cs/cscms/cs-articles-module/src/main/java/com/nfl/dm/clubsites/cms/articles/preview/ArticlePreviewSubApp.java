package com.nfl.dm.clubsites.cms.articles.preview;

import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.location.Location;
import info.magnolia.ui.framework.app.BaseSubApp;

import javax.inject.Inject;

/**
 * @author arash.shokoufandeh
 */
public class ArticlePreviewSubApp extends BaseSubApp {

    private String name;

    @Inject
    public ArticlePreviewSubApp(SubAppContext subAppContext, ArticlePreviewSubAppView view) {
        super(subAppContext, view);
    }

    @Override
    protected void onSubAppStart() {
        this.name = "Previewing: " + getCurrentLocation().getParameter();
        getView().setPreview(getCurrentLocation().getParameter());
    }

    @Override
    public ArticlePreviewSubAppView getView() {
        return (ArticlePreviewSubAppView)super.getView();
    }

    /**
     * Used to set the label on the tab.
     */
    @Override
    public String getCaption() {
        return name;
    }

    @Override
    public boolean supportsLocation(Location location) {
        String newUser = location.getParameter();
        return getCurrentLocation().getParameter().equals(newUser);
    }
}
