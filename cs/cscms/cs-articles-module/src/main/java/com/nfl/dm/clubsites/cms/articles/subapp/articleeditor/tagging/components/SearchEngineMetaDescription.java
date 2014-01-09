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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.tagging.components;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import info.magnolia.ui.vaadin.integration.jcr.DefaultPropertyUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.apache.commons.io.IOUtils;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 25/12/13
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class SearchEngineMetaDescription extends CustomField<String> {

    private VerticalLayout layout = new VerticalLayout();
    private TextArea textArea = new TextArea();

    private Label googlePreview = new Label("", ContentMode.HTML);
    private Label bingPreview = new Label("", ContentMode.HTML);
    private Label yahooPreview = new Label("", ContentMode.HTML);

    private String googleMetaDescriptionTemplate;
    private String bingMetaDescriptionTemplate;
    private String yahooMetaDescriptionTemplate;

    private String url;
    private String date;
    private String title;
    private JcrNodeAdapter articleItem;

    public SearchEngineMetaDescription() {
        textArea.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                fireValueChange(false);
            }
        });
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(getClass().getResourceAsStream("/com/nfl/dm/clubsites/cms/articles/subapp/articleeditor/tagging/components/google-template.html"), writer, null);
            googleMetaDescriptionTemplate = writer.toString();

            writer = new StringWriter();
            IOUtils.copy(getClass().getResourceAsStream("/com/nfl/dm/clubsites/cms/articles/subapp/articleeditor/tagging/components/bing-template.html"), writer, null);
            bingMetaDescriptionTemplate = writer.toString();

            writer = new StringWriter();
            IOUtils.copy(getClass().getResourceAsStream("/com/nfl/dm/clubsites/cms/articles/subapp/articleeditor/tagging/components/yahoo-template.html"), writer, null);
            yahooMetaDescriptionTemplate = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setInternalValue(String newValue) {
        textArea.setValue(newValue);
    }

    @Override
    protected String getInternalValue() {
        return textArea.getValue();
    }

    @Override
    protected Component initContent() {
        layout.setSizeFull();
        layout.setSpacing(true);
        Label caption = new Label("Search Engine Meta Description");

        textArea.setRows(8);
        textArea.setImmediate(true);
        textArea.setWidth("100%");

        layout.addComponent(caption);
        layout.addComponent(textArea);

        Component seoProgressBar = createSEOProgressBar();
        layout.addComponent(seoProgressBar);

        layout.addComponent(googlePreview);
        layout.addComponent(bingPreview);
        layout.addComponent(yahooPreview);

        String content = "";
        try {
            if (articleItem.getJcrItem().hasProperty("metaDescription") &&
                    !articleItem.getJcrItem().getProperty("metaDescription").equals(null) &&
                    !articleItem.getJcrItem().getProperty("metaDescription").equals("")) {
                content = articleItem.getJcrItem().getProperty("metaDescription").getString();
                textArea.setValue(content);
            } else if (articleItem.getJcrItem().hasProperty("lead") &&
                    !articleItem.getJcrItem().getProperty("lead").equals(null) &&
                    !articleItem.getJcrItem().getProperty("lead").equals("")) {
                content = articleItem.getJcrItem().getProperty("lead").getString();
            } else if (articleItem.getJcrItem().hasProperty("body")) {
                content = articleItem.getJcrItem().getProperty("body").getString();
            }

            if (content.length() > 154) {
                content = content.substring(0, 154);
                content += "...";
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        updateMetaDescriptionPreview(content);
        textArea.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateMetaDescriptionPreview(String.valueOf(event.getProperty().getValue()));
            }
        });
        return layout;
    }

    private Component createSEOProgressBar() {
        ProgressBar seoProgressBar = new ProgressBar();
        seoProgressBar.setWidth("250px");
        seoProgressBar.setValue(0.5f);

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(new Label("SEO Rating:"));
        layout.addComponent(seoProgressBar);
        layout.setSpacing(true);
        layout.setMargin(new MarginInfo(false, false, true, false));

        NativeButton addUpdateButton = new NativeButton("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                // do nothing
            }
        });
        addUpdateButton.addStyleName("update-button");
        addUpdateButton.setHeight("28px");
        addUpdateButton.setCaption("Update");
        addUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateMetaDescriptionPreview(textArea.getValue());
            }
        });
        layout.addComponent(addUpdateButton);

        return layout;
    }

    private void updateMetaDescriptionPreview(String content) {
        String title = "article-title";
        String url = "www.steelers.com/.../" + title;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String date = sdf.format(new Date());

        googlePreview.setValue(String.format(googleMetaDescriptionTemplate, "GOOGLE", title, url, date, content));
        bingPreview.setValue(String.format(bingMetaDescriptionTemplate, "BING", title, url, date, content));
        yahooPreview.setValue(String.format(yahooMetaDescriptionTemplate, "YAHOO", title, url, content));

        setMetaDescriptionOnNode(content);
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArticleItem(JcrNodeAdapter articleItem) {
        this.articleItem = articleItem;
    }

    private void setMetaDescriptionOnNode(String content) {
        try {
            articleItem.removeItemProperty("metaDescription");
            articleItem.addItemProperty("metaDescription", DefaultPropertyUtil.newDefaultProperty(String.class, content));
            articleItem.applyChanges();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
}
