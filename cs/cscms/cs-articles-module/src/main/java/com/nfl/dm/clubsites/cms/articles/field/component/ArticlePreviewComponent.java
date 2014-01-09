package com.nfl.dm.clubsites.cms.articles.field.component;

import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.nfl.dm.clubsites.cms.articles.ArticlesNodeTypes.Article;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.objectfactory.Components;
import info.magnolia.ui.form.field.component.AbstractBaseItemContentPreviewComponent;
import info.magnolia.ui.imageprovider.DefaultImageProvider;
import info.magnolia.ui.imageprovider.definition.ConfiguredImageProviderDefinition;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Article implementation of {@link AbstractBaseItemContentPreviewComponent}.
 */
public class ArticlePreviewComponent extends AbstractBaseItemContentPreviewComponent {

    private final SimpleTranslator i18n;

    @Inject
    public ArticlePreviewComponent(String workspace, ComponentProvider componentProvider, SimpleTranslator i18n) {
        super(workspace);

        this.i18n = i18n;
    }

    @Override
    public void setImageProvider() {
        ConfiguredImageProviderDefinition imageProviderDefinition = new ConfiguredImageProviderDefinition();
        imageProviderDefinition.setOriginalImageNodeName(Article.PROPERTY_TOP_IMAGE);
        imageProviderDefinition.setImageProviderClass(DefaultImageProvider.class);
        this.imageProvider = Components.newInstance(imageProviderDefinition.getImageProviderClass(), imageProviderDefinition);
    }

    @Override
    public List<Component> createFieldDetail(Item fileItem) throws RepositoryException {
        List<Component> res = new ArrayList<Component>();
        res.add(createInfoLabel(i18n.translate("articles.components.preview.title"), Article.PROPERTY_TITLE, fileItem));
        res.add(createInfoLabel(i18n.translate("articles.components.preview.lead"), Article.PROPERTY_LEAD, fileItem));
        res.add(createInfoLabel(i18n.translate("articles.components.preview.topImageCaption"), Article.PROPERTY_TOP_IMAGE_CAPTION, fileItem));
        res.add(createInfoLabel(i18n.translate("articles.components.preview.topImageAltText"), Article.PROPERTY_TOP_IMAGE_ALT_TEXT, fileItem));
        return res;
    }

    private Label createInfoLabel(String caption, String propertyName, Item fileItem) throws RepositoryException {
        Label label = new Label("", ContentMode.TEXT);
        label.setCaption(caption + ": ");
        label.setValue(fileItem.getItemProperty(propertyName).getValue().toString());
        return label;
    }

}
