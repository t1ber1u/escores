package com.nfl.dm.clubsites.cms.articles.form.action;

import com.vaadin.data.Item;
import info.magnolia.cms.core.Path;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.location.LocationController;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;
import info.magnolia.ui.form.action.SaveFormAction;
import info.magnolia.ui.form.action.SaveFormActionDefinition;
import info.magnolia.ui.vaadin.integration.jcr.JcrNewNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Action for saving articles.
 * We currently can't rename the node on change.
 * This must be properly solved by passing the node identifier to {@link info.magnolia.ui.api.event.ContentChangedEvent}.
 *
 * See MGNLUI-226.
 *
 * @see SaveArticleFormActionDefinition
 */
public class SaveArticleFormAction extends SaveFormAction {

    private static final Logger log = LoggerFactory.getLogger(SaveArticleFormAction.class);

    private Map<String, ArrayList> categorization;
    private SubAppContext subAppContext;
    private LocationController locationController;

    public SaveArticleFormAction(SaveFormActionDefinition definition, Item item, EditorCallback callback, EditorValidator validator, SubAppContext subAppContext, LocationController locationController) {
        super(definition, (JcrNodeAdapter) item, callback, validator);
        this.subAppContext = subAppContext;
        this.locationController = locationController;
    }

    @Override
    protected void setNodeName(Node node, JcrNodeAdapter item) throws RepositoryException {
        JcrNodeAdapter itemChanged = item;
        // Set the Node Composite Name
        if (itemChanged instanceof JcrNewNodeAdapter || !node.getName().startsWith(defineNodeName(node))) {
            final String newNodeName = generateUniqueNodeNameForArticle(node);
            itemChanged.setNodeName(newNodeName);
            NodeUtil.renameNode(node, newNodeName);
        }
    }

    /**
     * Create a new Node Unique NodeName.
     */
    private String generateUniqueNodeNameForArticle(final Node node) throws RepositoryException {
        String newNodeName = defineNodeName(node);
        return Path.getUniqueLabel(node.getSession(), node.getParent().getPath(), newNodeName);
    }

    /**
     * Define the Node Name. Node Name = Title, lower case, with spaces replaced with dashes
     */
    private String defineNodeName(final Node node) throws RepositoryException {
        String name = node.getProperty("title").getString().trim().replaceAll(" ", "-");
        return name.toLowerCase();
    }
}
