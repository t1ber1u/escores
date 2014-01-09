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

import com.nfl.dm.clubsites.cms.articles.util.domain.Author;
import com.nfl.dm.clubsites.cms.articles.util.domain.Series;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;

import java.util.List;

/**
 * UmbrellaContentViewImpl.
 */
public class UmbrellaContentViewImpl implements UmbrellaContentView {

    private HorizontalLayout layout = new HorizontalLayout();

    private VerticalLayout textAndSEOArea = new VerticalLayout();

    private VerticalLayout authorAndSeriesArea = new VerticalLayout();

    private ComboBox authorSelect = new UmbrellaViewComboBox(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            listener.onAuthorChange((Author) event.getProperty().getValue());
        }
    });

    private ComboBox seriesSelect = new UmbrellaViewComboBox(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            listener.onSeriesChange((Series) event.getProperty().getValue());
        }
    });

    private Listener listener;

    private TextArea headlineTextArea = new UmbrellaViewTextArea("Title/Headline", new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            listener.onTitleChange((String) event.getProperty().getValue());
        }
    });

    private TextArea leadInParagraph = new UmbrellaViewTextArea("Lead-In paragraph", new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            listener.onLeadInParagraphChange((String) event.getProperty().getValue());
        }
    });

    public UmbrellaContentViewImpl() {
        construct();
    }

    private void construct() {
        headlineTextArea.setWidth("100%");
        headlineTextArea.setRows(3);
        headlineTextArea.setNullRepresentation("");
        headlineTextArea.setInputPrompt("Insert a story title here...");

        Component seoProgressBar = createSEOProgressBar();

        leadInParagraph.setWidth("100%");
        leadInParagraph.setRows(5);
        leadInParagraph.setNullRepresentation("");

        textAndSEOArea.setWidth("100%");
        textAndSEOArea.setSpacing(true);
        textAndSEOArea.addComponent(headlineTextArea);
        textAndSEOArea.addComponent(seoProgressBar);
        textAndSEOArea.addComponent(leadInParagraph);

        HorizontalLayout authorsLayout = createAuthorsLayout();
        HorizontalLayout seriesLayout = createSeriesLayout();

        authorAndSeriesArea.setSizeUndefined();
        authorAndSeriesArea.addComponent(authorsLayout);
        authorAndSeriesArea.addComponent(seriesLayout);

        layout.addComponent(textAndSEOArea);
        layout.addComponent(authorAndSeriesArea);

        layout.setExpandRatio(textAndSEOArea, 1f);

        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

    }

    private Component createSEOProgressBar() {
        ProgressBar seoProgressBar = new ProgressBar();
        seoProgressBar.setWidth("300px");
        seoProgressBar.setValue(0.5f);

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(new Label("SEO Rating:"));
        layout.addComponent(seoProgressBar);
        layout.setSpacing(true);
        layout.setMargin(new MarginInfo(false, false, true, false));
        return layout;
    }

    private HorizontalLayout createSeriesLayout() {
        HorizontalLayout seriesLayout = new HorizontalLayout();
        seriesLayout.setSpacing(true);
        seriesLayout.setCaption("Series");
        seriesSelect.addItem("Test");
        Button addSeries = new Button("new series");
        addSeries.addStyleName("link");

        seriesLayout.addComponent(seriesSelect);
        seriesLayout.addComponent(addSeries);
        return seriesLayout;
    }

    private HorizontalLayout createAuthorsLayout() {
        HorizontalLayout authorsLayout = new HorizontalLayout();
        authorsLayout.setSpacing(true);
        authorsLayout.setCaption("Author");
        authorSelect.addItem("Test");
        Button addAuthor = new Button("new author");
        addAuthor.addStyleName("link");
        /*
        addAuthor.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    String path = Path.getAbsolutePath(parentItem.applyChanges().getPath(), NEW_NODE_NAME);
                    DetailLocation location = new DetailLocation(getDefinition().getAppName(), getDefinition().getSubAppId(), DetailView.ViewType.EDIT, path, "");
                    locationController.goTo(location);
                } catch (RepositoryException e) {
                    throw new ActionExecutionException(e);
                }
            }
        });
        */

        authorsLayout.addComponent(authorSelect);
        authorsLayout.addComponent(addAuthor);
        return authorsLayout;
    }

    @Override
    public Component asVaadinComponent() {
        return layout;
    }

    @Override
    public void setAuthor(Author author) {
        authorSelect.setValue(author);
    }

    @Override
    public void setSeries(Series series) {
        seriesSelect.setValue(series);
    }

    @Override
    public void setAuthors(List<Author> authorDataSource) {
        authorSelect.setContainerDataSource(new BeanItemContainer<Author>(Author.class, authorDataSource));
        authorSelect.setItemCaptionPropertyId("fullName");
    }

    @Override
    public void setSeries(List<Series> seriesDataSource) {
        seriesSelect.setContainerDataSource(new BeanItemContainer<Series>(Series.class, seriesDataSource));
        seriesSelect.setItemCaptionPropertyId("title");
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void setTitleDataSource(Property p) {
        headlineTextArea.setPropertyDataSource(p);
    }

    @Override
    public void setLeadInParagraphDataSource(Property p) {
        leadInParagraph.setPropertyDataSource(p);
    }

    private static class UmbrellaViewComboBox extends ComboBox {

        private UmbrellaViewComboBox(ValueChangeListener listener) {
            setImmediate(true);
            setNullSelectionAllowed(false);
            addValueChangeListener(listener);
        }
    }

    private static class UmbrellaViewTextArea extends TextArea {

        private UmbrellaViewTextArea(String caption, ValueChangeListener listener) {
            setImmediate(true);
            setCaption(caption);
            addValueChangeListener(listener);
        }
    }
}
