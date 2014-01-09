package com.nfl.dm.clubsites.cms.personnel.column;

import com.nfl.dm.clubsites.cms.personnel.PersonnelNodeTypes;
import com.vaadin.ui.Table;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.ui.workbench.column.AbstractColumnFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Column formatter that displays either the name of personnel or folder.
 *
 * @see PersonnelNameColumnDefinition
 */
public class PersonnelNameColumnFormatter extends AbstractColumnFormatter<PersonnelNameColumnDefinition> {

    private static final Logger log = LoggerFactory.getLogger(PersonnelNameColumnFormatter.class);

    public PersonnelNameColumnFormatter(PersonnelNameColumnDefinition definition) {
        super(definition);
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        final Item jcrItem = getJcrItem(source, itemId);
        if (jcrItem != null && jcrItem.isNode()) {
            Node node = (Node) jcrItem;

            try {
                if (NodeUtil.isNodeType(node, NodeTypes.Folder.NAME)) {
                    return node.getName();
                }
            } catch (RepositoryException e) {
                log.warn("Unable to get name of folder for column", e);
            }

            try {
                if (NodeUtil.isNodeType(node, PersonnelNodeTypes.Personnel.NAME)) {
                    // if the node has been mark as deleted, most of its properties - e.g. lastName, firstName - has been removed
                    if(node.hasProperty("firstName") && node.hasProperty("lastName")){
                        return PropertyUtil.getString(node, "firstName", " ") + " " + PropertyUtil.getString(node, "lastName", " ");
                    }else{
                        return node.getName();
                    }
                }
            } catch (RepositoryException e) {
                log.warn("Unable to get name of author for column", e);
            }
        }
        return "";
    }
}
