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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.umbrellacontent;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.nfl.dm.clubsites.cms.articles.util.ArticleService;
import com.nfl.dm.clubsites.cms.articles.util.domain.Author;
import com.nfl.dm.clubsites.cms.articles.util.domain.Series;
import com.vaadin.data.Property;
import info.magnolia.event.EventBus;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.TransformedProperty;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;
import info.magnolia.ui.vaadin.integration.jcr.DefaultPropertyUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * UmbrellaContentPresenter.
 */
public class UmbrellaContentPresenter implements UmbrellaContentView.Listener {

    private UmbrellaContentView view;

    private ArticleService service;

    private EventBus eventBus;

    private ComponentProvider provider;

    private JcrNodeAdapter umbrellaContentItem;

    @Inject
    public UmbrellaContentPresenter(UmbrellaContentView view, ArticleService service, @Named(SubAppEventBus.NAME) EventBus eventBus, ComponentProvider provider) {
        this.view = view;
        this.service = service;
        this.eventBus = eventBus;
        this.provider = provider;
    }

    public UmbrellaContentView start(JcrNodeAdapter umbrellaContentItem) {
        view.setListener(this);
        this.umbrellaContentItem = umbrellaContentItem;
        prepareDataSource();
        generatePreview();
        view.setAuthors(service.getAuthors());
        view.setSeries(service.getSeries());

        Author author = service.getAuthorById(PropertyUtil.getString(umbrellaContentItem.getJcrItem(), "author"));
        view.setAuthor(author);

        Series series = service.getSeriesById(PropertyUtil.getString(umbrellaContentItem.getJcrItem(), "series"));
        view.setSeries(series);

        return view;
    }

    private void prepareDataSource() {
        view.setTitleDataSource(getProperty("title"));
        view.setLeadInParagraphDataSource(getProperty("leadInParagraph"));
    }

    private Property getProperty(String propertyName) {
        ConfiguredFieldDefinition titleDefinition = new ConfiguredFieldDefinition();
        titleDefinition.setName(propertyName);
        return new TransformedProperty(provider.newInstance(BasicTransformer.class, umbrellaContentItem, titleDefinition, String.class));
    }

    @Override
    public void onTitleChange(String newTitle) {
        generatePreview();
    }

    @Override
    public void onAuthorChange(Author newAuthor) {
        umbrellaContentItem.addItemProperty("author", DefaultPropertyUtil.newDefaultProperty(String.class, newAuthor.getId()));
        generatePreview();

    }

    @Override
    public void onSeriesChange(Series newSeries) {
        umbrellaContentItem.addItemProperty("series", DefaultPropertyUtil.newDefaultProperty(String.class, newSeries.getId()));
        generatePreview();

    }

    @Override
    public void onLeadInParagraphChange(String value) {
        generatePreview();
    }

    private void generatePreview() {
        UmbrellaContentPreview preview = new UmbrellaContentPreview();
        preview.setTitle((String) umbrellaContentItem.getItemProperty("title").getValue());
        String authorId = umbrellaContentItem.getItemProperty("author") != null ? (String) umbrellaContentItem.getItemProperty("author").getValue() : "";
        String seriesId = umbrellaContentItem.getItemProperty("series") != null ? (String) umbrellaContentItem.getItemProperty("series").getValue() : "";
        Author author = service.getAuthorById(authorId);
        Series series = service.getSeriesById(seriesId);

        if (author != null) {
            preview.setAuthor(author.getFullName());
        }

        if (series != null) {
            preview.setSeries(series.getTitle());
        }

        eventBus.fireEvent(new PreviewChangedEvent(preview, ViewType.Summary));
    }
}
