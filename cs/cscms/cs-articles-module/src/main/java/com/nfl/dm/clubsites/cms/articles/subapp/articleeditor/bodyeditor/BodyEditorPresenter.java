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
package com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.bodyeditor;

import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.ViewType;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.ArticleContentChangedEvent;
import com.nfl.dm.clubsites.cms.articles.subapp.articleeditor.event.PreviewChangedEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Label;
import info.magnolia.context.MgnlContext;
import info.magnolia.dam.app.assets.field.translator.AssetCompositeIdKeyTranslator;
import info.magnolia.dam.asset.functions.DamTemplatingFunctions;
import info.magnolia.event.EventBus;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.SubAppEventBus;
import info.magnolia.ui.api.view.View;
import info.magnolia.ui.form.field.LinkField;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;
import info.magnolia.ui.form.field.definition.RichTextFieldDefinition;
import info.magnolia.ui.form.field.factory.FieldFactory;
import info.magnolia.ui.form.field.factory.FieldFactoryFactory;
import info.magnolia.ui.vaadin.integration.jcr.DefaultPropertyUtil;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * BodyEditorPresenter.
 */
public class BodyEditorPresenter implements BodyEditorView.Listener {

    private static final String SECTION_TEMPLATE = "<div class=\"section-title-wrapper\" contenteditable=\"false\"><div class=\"section-logo\" contenteditable=\"false\">&sect;</div><span class=\"section-title-text\">%s</span></div><p>";

    private BodyEditorView view;
    private EventBus eventBus;
    private FieldFactoryFactory ffFactory;
    private ComponentProvider provider;
    private CKEditorTextField ckEditor;
    private JcrNodeAdapter articleItem;
    private DamTemplatingFunctions damTemplatingFunctions;

    @Inject
    public BodyEditorPresenter(final BodyEditorView view, final @Named(SubAppEventBus.NAME) EventBus eventBus, final FieldFactoryFactory ffFactory, final ComponentProvider provider, final DamTemplatingFunctions damTemplatingFunctions) {
        this.view = view;
        this.eventBus = eventBus;
        this.ffFactory = ffFactory;
        this.provider = provider;
        this.damTemplatingFunctions = damTemplatingFunctions;
        this.eventBus.addHandler(ArticleContentChangedEvent.class, new ArticleContentChangedEvent.Handler() {
            @Override
            public void onContentChanged(ArticleContentChangedEvent event) {
                // initContent();
            }
        });
    }

    public View start(JcrNodeAdapter articleItem) {
        this.articleItem = articleItem;
        Label preview = new Label("Body Editor");
        eventBus.fireEvent(new PreviewChangedEvent(preview, ViewType.BodyEditor));
        initContent(articleItem);

        view.setListener(this);
        view.createToolbar();
        view.addEditor(ckEditor);
        return view;
    }

    @Override
    public void createSection(String value) {
        ckEditor.insertHtml(String.format(SECTION_TEMPLATE, value));
    }

    private void initContent(JcrNodeAdapter articleItem) {
        if (articleItem.getItemProperty("body") == null) {
            articleItem.addItemProperty("body", DefaultPropertyUtil.newDefaultProperty(String.class, ""));
        }
        ckEditor = createRichTextField(articleItem);
        ckEditor.focus();

    }

    public CKEditorTextField createRichTextField(final JcrNodeAdapter item) {
        RichTextFieldDefinition definition = new RichTextFieldDefinition();
        definition.setName("body");
        FieldFactory ff = ffFactory.createFieldFactory(definition, item);
        ff.setComponentProvider(provider);
        final CKEditorTextField field = (CKEditorTextField) ff.createField();
        field.setImmediate(true);
        field.addBlurListener(new FieldEvents.BlurListener() {
            @Override
            public void blur(FieldEvents.BlurEvent event) {
                field.commit();
            }
        });

        field.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                eventBus.fireEvent(new ArticleContentChangedEvent());
            }
        });
        field.setSizeFull();

        CKEditorConfig config = new CKEditorConfig();
        config.addCustomToolbarLine("{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike' ] }, { name: 'paragraph', items: [ 'NumberedList', 'BulletedList', 'Outdent', 'Indent' ]}");
        config.setAllowedContentAll(); // or we can't insert divs and images via insertHtml() method
        config.setContentsCss(MgnlContext.getWebContext().getContextPath() + "/.resources/nfl-poc-vaadin/styles.css");
        config.setForcePasteAsPlainText(true);
        config.setBodyClass("nfl-article-template");
        field.setConfig(config);

        return field;
    }

    @Override
    public void openMediaGallery() {

        LinkFieldDefinition definition = new LinkFieldDefinition();
        definition.setName("image");
        definition.setAppName("assets");
        definition.setTargetWorkspace("dam");
        // translate path to identifier
        definition.setIdentifierToPathConverter(new AssetCompositeIdKeyTranslator());

        FieldFactory ff = ffFactory.createFieldFactory(definition, articleItem);
        ff.setComponentProvider(provider);
        final LinkField linkField = (LinkField) ff.createField();
        linkField.setImmediate(true);
        linkField.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                linkField.commit();
                String assetLink = damTemplatingFunctions.getAssetLinkForId(linkField.getConvertedValue().toString());
                ckEditor.insertHtml(String.format("<img src=\"%s\"//>", assetLink));
                eventBus.fireEvent(new ArticleContentChangedEvent());
            }
        });
        // we need to add the component to the layout else it won't work. Of course, we make the button and the text field invisible (the visible button is the one we create in the view).
        view.addHiddenComponent(linkField);
        linkField.getSelectButton().click();
    }

}
