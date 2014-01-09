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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.promote;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import info.magnolia.ui.vaadin.integration.jcr.DefaultPropertyUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import info.magnolia.ui.vaadin.richtext.MagnoliaRichTextField;

import javax.inject.Inject;
import javax.jcr.RepositoryException;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class ArticlePromotionViewImpl implements ArticlePromotionView {

    private VerticalLayout layout = new VerticalLayout();

    private Panel alert = new Panel("Alert");

    private Panel centerPiece = new Panel("Centerpiece");

    private Panel tile = new Panel("Tile");

    private Panel listing = new Panel("Listing");

    private Panel social = new Panel("Social");

    private Listener listener;

    private JcrNodeAdapter articleItem;

    @Inject
    public ArticlePromotionViewImpl() {
    }

    @Override
    public void construct() {
        layout.addStyleName("article-promotion-layout");
        layout.setMargin(true);
        layout.setSpacing(true);

        createAlert();
        createCenterPiece();

        layout.addComponent(alert);
        layout.addComponent(centerPiece);
    }

    @Override
    public void setSiteAlertOnNode(String content) {
        try {
            articleItem.removeItemProperty("siteAlert");
            articleItem.addItemProperty("siteAlert", DefaultPropertyUtil.newDefaultProperty(String.class, content));
            articleItem.applyChanges();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCenterpieceOnNode(String content) {
        try {
            articleItem.removeItemProperty("centerpiece");
            articleItem.addItemProperty("centerpiece", DefaultPropertyUtil.newDefaultProperty(String.class, content));
            articleItem.applyChanges();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setArticleItem(JcrNodeAdapter articleItem) {
        this.articleItem = articleItem;
    }

    private void createCenterPiece() {
        HorizontalLayout centerpieceLayout = new HorizontalLayout();
        centerpieceLayout.setWidth("100%");
        centerpieceLayout.setSpacing(true);

        VerticalLayout centerpieceSettings = new VerticalLayout();
        centerpieceSettings.setWidth("100%");
        centerpieceSettings.setSpacing(true);

        Field richTextField = listener.createRichTextField();
        richTextField.setCaption("Teaser");
        richTextField.setWidth("100%");
        richTextField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    articleItem.removeItemProperty("siteAlert");
                    articleItem.addItemProperty("siteAlert", DefaultPropertyUtil.newDefaultProperty(String.class, event.getProperty().getValue().toString()));
                    articleItem.applyChanges();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        });

        centerpieceSettings.addComponent(richTextField);

        Label placementLabel = new Label("", ContentMode.HTML);
        placementLabel.setCaption("Placement");
        centerpieceSettings.addComponent(placementLabel);

        CheckBox home = listener.createCheckBox("Home Page");
        centerpieceSettings.addComponent(home);
        CheckBox news = listener.createCheckBox("News Landing");
        centerpieceSettings.addComponent(news);
        CheckBox comm = listener.createCheckBox("Community Landing");
        centerpieceSettings.addComponent(comm);
        CheckBox team = listener.createCheckBox("Team Landing");
        centerpieceSettings.addComponent(team);
        CheckBox cheer = listener.createCheckBox("Cheerleader Landing");
        centerpieceSettings.addComponent(cheer);

        centerpieceLayout.addComponent(centerpieceSettings);

        Label previewLabel = new Label("", ContentMode.HTML);
        previewLabel.setCaption("Centerpiece preview");
        centerpieceLayout.addComponent(previewLabel);

        centerPiece.setContent(centerpieceLayout);
    }

    private void createAlert() {
        VerticalLayout alertTextLayout = new VerticalLayout();
        alertTextLayout.setSpacing(true);
        alertTextLayout.setCaption("Alert Text");

        MagnoliaRichTextField textField = (MagnoliaRichTextField) listener.createRichTextField();
        textField.setSizeFull();

        HorizontalLayout periodLayout = new HorizontalLayout();
        periodLayout.setSpacing(true);
        // periodLayout.setWidth("100%");
        DateField startDate = new DateField();
        // startDate.setWidth("100%");
        DateField endDate = new DateField();
        // endDate.setWidth("100%");

        periodLayout.addComponent(new Label("START"));
        periodLayout.addComponent(startDate);
        periodLayout.addComponent(new Label("END"));
        periodLayout.addComponent(endDate);

        alertTextLayout.addComponent(textField);
        alertTextLayout.addComponent(periodLayout);

        HorizontalLayout alertLayout = new HorizontalLayout();
        alertLayout.setSpacing(true);
        alertLayout.setWidth("100%");
        alertLayout.addComponent(alertTextLayout);

        final Label alertPreview = new Label("", ContentMode.HTML);
        alertPreview.setCaption("Alert Preview");
        alertPreview.addStyleName("preview-label");
        textField.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                alertPreview.setValue(String.valueOf(event.getProperty().getValue()));
            }
        });
        alertPreview.setSizeUndefined();
        alertLayout.addComponent(alertPreview);
        alert.setContent(alertLayout);

        textField.setValue("<b>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,</b>");
    }

    @Override
    public Component asVaadinComponent() {
        return layout;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
