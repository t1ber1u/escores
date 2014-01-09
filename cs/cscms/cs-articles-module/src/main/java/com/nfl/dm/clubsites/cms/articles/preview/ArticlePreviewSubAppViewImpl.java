package com.nfl.dm.clubsites.cms.articles.preview;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import info.magnolia.cms.beans.config.ServerConfiguration;

/**
 * @author arash.shokoufandeh
 */
public class ArticlePreviewSubAppViewImpl implements ArticlePreviewSubAppView {

    VerticalLayout layout = new VerticalLayout();

    public ArticlePreviewSubAppViewImpl() {
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setHeight(100, Sizeable.Unit.PERCENTAGE);
    }

    @Override
    public Component asVaadinComponent() {
        return layout;
    }

    @Override
    public void setPreview(String name) {
        ServerConfiguration sc = ServerConfiguration.getInstance();
        String def = sc.getDefaultBaseUrl();

        String[] parts = def.split("http://");
        String[] parts2 = parts[1].split("/");
        String envIp = parts2[0];

        BrowserFrame browser = new BrowserFrame(null,
                new ExternalResource("http://" + envIp + "/magnoliaAuthor/article?contentPath=" + name));
        browser.setWidth(100, Sizeable.Unit.PERCENTAGE);
        browser.setHeight(100, Sizeable.Unit.PERCENTAGE);
        layout.addComponent(browser);
    }
}
