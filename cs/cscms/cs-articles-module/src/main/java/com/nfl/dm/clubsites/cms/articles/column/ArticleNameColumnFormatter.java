package com.nfl.dm.clubsites.cms.articles.column;

import com.vaadin.ui.Table;
import com.nfl.dm.clubsites.cms.articles.ArticlesNodeTypes;
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
 * Column formatter that displays either the name of a article or a folder.
 *
 * @see ArticleNameColumnDefinition
 */
public class ArticleNameColumnFormatter extends AbstractColumnFormatter<ArticleNameColumnDefinition> {

    private static final Logger log = LoggerFactory.getLogger(ArticleNameColumnFormatter.class);

    public ArticleNameColumnFormatter(ArticleNameColumnDefinition definition) {
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
                if (NodeUtil.isNodeType(node, ArticlesNodeTypes.Article.NAME)) {
                    // if the node has been marked as deleted, most of its properties - e.g. title, lead - have been removed
                    if (node.hasProperty("title")) {
                        return PropertyUtil.getString(node, "title", "deleted-article");
                    } else{
                        return node.getName();
                    }
                }
            } catch (RepositoryException e) {
                log.warn("Unable to get name of article for column", e);
            }
        }
        return "";
    }
}
