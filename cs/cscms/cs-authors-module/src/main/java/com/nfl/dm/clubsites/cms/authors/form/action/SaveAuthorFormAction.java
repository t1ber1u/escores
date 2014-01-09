package com.nfl.dm.clubsites.cms.authors.form.action;

import com.vaadin.data.Item;
import info.magnolia.cms.core.Path;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;
import info.magnolia.ui.form.action.SaveFormAction;
import info.magnolia.ui.form.action.SaveFormActionDefinition;
import info.magnolia.ui.vaadin.integration.jcr.JcrNewNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Action for saving authors.
 * We currently can't rename the node on change.
 * This must be properly solved by passing the node identifier to {@link info.magnolia.ui.api.event.ContentChangedEvent}.
 *
 * See MGNLUI-226.
 *
 * @see SaveAuthorFormActionDefinition
 */
public class SaveAuthorFormAction extends SaveFormAction {

    public SaveAuthorFormAction(SaveFormActionDefinition definition, Item item, EditorCallback callback, EditorValidator validator) {
        super(definition, (JcrNodeAdapter) item, callback, validator);
    }

    @Override
    protected void setNodeName(Node node, JcrNodeAdapter item) throws RepositoryException {
        JcrNodeAdapter itemChanged = item;
        // Set the Node Composite Name
        if (itemChanged instanceof JcrNewNodeAdapter || !node.getName().startsWith(defineNodeName(node))) {
            final String newNodeName = generateUniqueNodeNameForAuthor(node);
            itemChanged.setNodeName(newNodeName);
            NodeUtil.renameNode(node, newNodeName);
        }
    }


    /**
     * Create a new Node Unique NodeName.
     */
    private String generateUniqueNodeNameForAuthor(final Node node) throws RepositoryException {
        String newNodeName = defineNodeName(node);
        return Path.getUniqueLabel(node.getSession(), node.getParent().getPath(), newNodeName);
    }

    /**
     * Define the Node Name. Node Name = First Char of the lastName + the full
     * firstName. lastName = eric firstName = tabli The node name is etabli
     */
    private String defineNodeName(final Node node) throws RepositoryException {
        String firstName = node.getProperty("firstName").getString().trim();
        String lastName = node.getProperty("lastName").getString().trim();
        return (firstName.charAt(0) + lastName.replaceAll("\\s+", "")).toLowerCase();
    }
}
