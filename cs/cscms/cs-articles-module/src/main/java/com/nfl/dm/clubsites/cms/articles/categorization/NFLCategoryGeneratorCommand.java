package com.nfl.dm.clubsites.cms.articles.categorization;

import info.magnolia.cms.security.AccessDeniedException;
import info.magnolia.cms.util.NodeDataUtil;
import info.magnolia.commands.impl.BaseRepositoryCommand;
import info.magnolia.context.Context;
import info.magnolia.jcr.util.*;
import info.magnolia.module.categorization.CategorizationNodeTypes;
import info.magnolia.module.categorization.functions.CategorizationTemplatingFunctions;
import info.magnolia.module.templatingkit.templates.category.TemplateCategory;
import info.magnolia.module.templatingkit.templates.category.TemplateCategoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.*;
import java.util.*;

/**
 * @author arash.shokoufandeh
 */
public class NFLCategoryGeneratorCommand extends BaseRepositoryCommand {

    private static Logger log = LoggerFactory.getLogger(NFLCategoryGeneratorCommand.class);

    private CategorizationTemplatingFunctions categorizationTemplatingFunctions;

    private static final String TEAMS_FOLDER = "organization";
    private static final String PLAYERS_FOLDER = "person";

    private Map<String, String> categorization = new HashMap<String, String>();

    @Inject
    public NFLCategoryGeneratorCommand(CategorizationTemplatingFunctions categorizationTemplatingFunctions) {
        this.categorizationTemplatingFunctions = categorizationTemplatingFunctions;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        try {
            Node categorizationStartNode = SessionUtil.getNode("articles", "/");
            NodeUtil.visit(categorizationStartNode, new NodeVisitor() {

                @Override
                public void visit(Node node) throws RepositoryException {
                    if (node.getDepth() >= 1 && NodeUtil.isNodeType(node, NodeTypes.Content.NAME)
                            && TemplateCategoryUtil.getTemplateSubCategory(node) != null) {
                        saveCentralRepositoryCategorizationItem(categorization, node);
                        saveCentralRepositoryCategorizationItem(categorization, node.getParent());

                        saveArticleCategories(categorization, node);
                    }
                }
            });
        } catch (Exception e) {
            log.error("can't execute command", e);
            return false;
        }
        return true;
    }

    /**
     * Finds out which category should be created in the central repository depending on the template category.
     */
    protected void saveCentralRepositoryCategorizationItem(Map<String, String>  categorization, Node content) throws RepositoryException {
        Node categorizationNode = SessionUtil.getNode(this.categorizationTemplatingFunctions.getCategorizationRepository(), this.categorizationTemplatingFunctions.getCategorizationRootPath());
        Node folder = getFolder(categorizationNode, TemplateCategoryUtil.getTemplateCategory(content));

        // In case we get to an article node we set its template subcategory as category
        String category = TemplateCategoryUtil.getTemplateSubCategory(content);
        String title = category;
        if(folder.getName().equals(TEAMS_FOLDER)) {
            category = content.getName();
            title = PropertyUtil.getString(content, "title");
            if(StringUtils.isEmpty(title)) {
                title = category;
            }
        }

        Node categoryNode =  this.categorizationTemplatingFunctions.getCategoryNodeByName(category);
        if( categoryNode == null) {
            categoryNode = NodeUtil.createPath(folder, category, CategorizationNodeTypes.Category.NAME, true);
            categoryNode.setProperty("displayName", title);
            categoryNode.setProperty("name", folder.getName());
            folder.getSession().save();
        }

        categorization.put(content.getName(), categoryNode.getIdentifier());
    }

    /**
     * Create the root Categorization folder if it doesn't exist.
     * @param categorizationNode
     * @param templateCategory
     * @return
     * @throws info.magnolia.cms.security.AccessDeniedException
     * @throws RepositoryException
     */
    protected Node getFolder(Node categorizationNode, String templateCategory) throws AccessDeniedException, RepositoryException {
        String folderName = TEAMS_FOLDER;
        if(StringUtils.equals(templateCategory, TemplateCategory.CONTENT)) {
            folderName = PLAYERS_FOLDER;
        }
        return NodeUtil.createPath(categorizationNode, folderName, NodeTypes.Folder.NAME, true);
    }

    protected void saveArticleCategories(final Map<String, String> categorization, Node subNode) throws RepositoryException {
        List<Value> categories = new ArrayList<Value>();

        if(subNode.hasNode("categories")) {
            PropertyIterator pi = subNode.getNode("categories").getProperties();
            while (pi.hasNext()) {
                categories.add(pi.nextProperty().getValue());
            }
        }

        checkAndUpdateCategoriesList(categorization, subNode, categories);
        checkAndUpdateCategoriesList(categorization, subNode.getParent(), categories);

        subNode.setProperty("categories", categories.toArray(new Value[categories.size()]));
        subNode.getSession().save();
    }

    /**
     * need to check if the category is already in the page property 'categories', if not, updates the list with
     * new value.
     */
    protected void checkAndUpdateCategoriesList(final Map<String, String> categorization, Node webContent, List<Value> categories)
            throws PathNotFoundException, RepositoryException {
        if(webContent.getDepth() > 1 && categorization.containsKey(webContent.getName()) ) {
            final String uuid = categorization.get(webContent.getName());
            Value value = NodeDataUtil.createValue(uuid, PropertyType.STRING);

            if(!isExistValue(categories, uuid, value)) {
                categories.add(value);
            }
        }
    }

    /**
     * checks if this new value is already in the categories list.
     */
    protected boolean isExistValue(List<Value> categories, final String uuid, Value value) {
        Object obj = CollectionUtils.find(categories, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                try {
                    return StringUtils.equals(((Value) object).getString(), uuid);
                } catch (Exception e) {
                    log.error("Couldn't access value", e);
                    return false;
                }
            }
        });

        return (obj != null);
    }

    public Map<String, String> getCategorization() {
        return categorization;
    }

    public void setCategorization(Map<String, String> categorization) {
        this.categorization = categorization;
    }

    public boolean execute(Map<String, ArrayList<String>> map, Node articleNode) throws Exception {
        if (articleNode.getDepth() >= 1 && NodeUtil.isNodeType(articleNode, NodeTypes.Content.NAME)
                && TemplateCategoryUtil.getTemplateSubCategory(articleNode) != null) {
            Node categorizationNode = SessionUtil.getNode(this.categorizationTemplatingFunctions.getCategorizationRepository(), this.categorizationTemplatingFunctions.getCategorizationRootPath());

            ArrayList<String> person = map.get("person");
            Node playerFolder = NodeUtil.createPath(categorizationNode, PLAYERS_FOLDER, NodeTypes.Folder.NAME, true);
            for (String category : person) {
                saveCategoryToCentralRepository(category, articleNode, playerFolder);
                saveCategory(category, articleNode);
            }

            ArrayList<String> organization = map.get("organization");
            Node organizationFolder = NodeUtil.createPath(categorizationNode, TEAMS_FOLDER, NodeTypes.Folder.NAME, true);
            for (String category : organization) {
                saveCategoryToCentralRepository(category, articleNode, organizationFolder);
                saveCategory(category, articleNode);
            }
        }
        return true;
    }

    /**
     * Finds out which category should create in central repository depending on the template category.
     */
    protected void saveCategoryToCentralRepository(String category, Node articleNode, Node folder) throws RepositoryException {
        Node categoryNode =  this.categorizationTemplatingFunctions.getCategoryNodeByName(category);
        if( categoryNode == null) {
            categoryNode = NodeUtil.createPath(folder, category, CategorizationNodeTypes.Category.NAME, true);
            categoryNode.setProperty("displayName", category);
            categoryNode.setProperty("name", folder.getName());
            folder.getSession().save();
        }

        categorization.put(category, categoryNode.getIdentifier());
    }

    protected void saveCategory(final String category, Node articleNode) throws RepositoryException {
        Node categoryNode = NodeUtil.createPath(articleNode, "categories", NodeTypes.Content.NAME, true);
        categoryNode.setProperty(category, category);
        articleNode.getSession().save();
    }

}
