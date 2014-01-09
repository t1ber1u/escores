package com.nfl.dm.clubsites.cms.authors.field.component;

import com.nfl.dm.clubsites.cms.authors.AuthorsNodeTypes.Author;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
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
 * Author implementation of {@link AbstractBaseItemContentPreviewComponent}.
 */
public class AuthorPreviewComponent extends AbstractBaseItemContentPreviewComponent {

    private final SimpleTranslator i18n;

    @Inject
    public AuthorPreviewComponent(String workspace, ComponentProvider componentProvider, SimpleTranslator i18n) {
        super(workspace);
        this.i18n = i18n;
    }

    @Override
    public void setImageProvider() {
        ConfiguredImageProviderDefinition imageProviderDefinition = new ConfiguredImageProviderDefinition();
        imageProviderDefinition.setOriginalImageNodeName(Author.IMAGE_NODE_NAME);
        imageProviderDefinition.setImageProviderClass(DefaultImageProvider.class);
        this.imageProvider = Components.newInstance(imageProviderDefinition.getImageProviderClass(), imageProviderDefinition);
    }

    @Override
    public List<Component> createFieldDetail(Item fileItem) throws RepositoryException {
        List<Component> res = new ArrayList<Component>();
        res.add(createInfoLabel(i18n.translate("authors.components.preview.lastName"), Author.PROPERTY_LAST_NAME, fileItem));
        res.add(createInfoLabel(i18n.translate("authors.components.preview.firstName"), Author.PROPERTY_FIRST_NAME, fileItem));
        res.add(createInfoLabel(i18n.translate("authors.components.preview.twitter"), Author.PROPERTY_TWITTER, fileItem));
        res.add(createInfoLabel(i18n.translate("authors.components.preview.email"), Author.PROPERTY_EMAIL, fileItem));
        return res;
    }

    private Label createInfoLabel(String caption, String propertyName, Item fileItem) throws RepositoryException {
        Label label = new Label("", ContentMode.TEXT);
        label.setCaption(caption + ": ");
        label.setValue(fileItem.getItemProperty(propertyName).getValue().toString());
        return label;
    }

}
