package com.nfl.dm.clubsites.cms.articles;

import info.magnolia.ui.contentapp.ContentSubAppView;

/**
 * @author arash.shokoufandeh
 */
public interface ArticleDetailSubAppView extends ContentSubAppView {

    void setListener(Listener listener);
    void addPreviewButton(String title);

    public interface Listener {
        void previewArticle(String title);
    }
}
