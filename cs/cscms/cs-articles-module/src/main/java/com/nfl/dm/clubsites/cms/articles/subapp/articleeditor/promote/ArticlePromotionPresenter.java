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

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import info.magnolia.event.EventBus;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.form.field.definition.RichTextFieldDefinition;
import info.magnolia.ui.form.field.factory.FieldFactory;
import info.magnolia.ui.form.field.factory.FieldFactoryFactory;
import info.magnolia.ui.vaadin.integration.NullItem;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created with IntelliJ IDEA.
 * User: sasha
 * Date: 22/12/13
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public class ArticlePromotionPresenter implements ArticlePromotionView.Listener {

    private ArticlePromotionView view;

    private EventBus eventBus;

    private ComponentProvider provider;

    private FieldFactoryFactory ffFactory;

    private JcrNodeAdapter articleItem;

    @Inject
    public ArticlePromotionPresenter(ArticlePromotionView view, @Named(SubAppEventBus.NAME) EventBus eventBus, FieldFactoryFactory ffFactory, ComponentProvider provider) {
        this.view = view;
        this.eventBus = eventBus;
        this.ffFactory = ffFactory;
        this.provider = provider;
    }

    public ArticlePromotionView start(JcrNodeAdapter articleItem) {
        this.articleItem = articleItem;

        String title = "PROMOTE";

        Label previewLabel = new Label(title, ContentMode.HTML);
        previewLabel.setSizeUndefined();
        previewLabel.addStyleName("navigation-label");
        previewLabel.addStyleName("ax-shape");

        view.setListener(this);
        view.construct();
        eventBus.fireEvent(new PreviewChangedEvent(previewLabel, ViewType.Promote));

        return view;
    }

    @Override
    public Field createRichTextField() {
        RichTextFieldDefinition definition = new RichTextFieldDefinition();
        definition.setImages(true);
        FieldFactory ff = ffFactory.createFieldFactory(definition, new NullItem());
        ff.setComponentProvider(provider);
        final Field<?> field = ff.createField();
        ((AbstractField)field).setImmediate(true);
        ((CKEditorTextField)field).addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
              field.commit();
            }
        });
        return field;
    }

    @Override
    public CheckBox createCheckBox(final String caption) {
        CheckBox checkBox = new CheckBox(caption);
        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    if (!articleItem.getJcrItem().hasNode("centerpiece")) {
                        Node categoryNode = NodeUtil.createPath(articleItem.getJcrItem(), "centerpiece", NodeTypes.Content.NAME, true);
                    }
                    if (articleItem.getJcrItem().getNode("centerpiece").hasProperty(caption)) {
                        articleItem.getJcrItem().getNode("centerpiece").getProperty(caption).remove();
                    }
                    if (event.getProperty().getValue().toString().equals("true")) {
                        articleItem.getJcrItem().getNode("centerpiece").setProperty(caption,caption);
                    }
                    articleItem.applyChanges();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        });
        return checkBox;
    }
}
