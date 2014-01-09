package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.cms.core.Path;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.vaadin.integration.jcr.JcrNewNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Component for the article prototypical page.
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "Article", id = "cs-blossom-module:components/article")
public class ArticleComponent {

    private static final Logger log = LoggerFactory.getLogger(ArticleComponent.class);

    @RequestMapping("/article")
    public String render(ModelMap model, HttpServletRequest request) throws RepositoryException {
        String articleId = MgnlContext.getParameter("contentPath");
        Node node = MgnlContext.getJCRSession("articles").getNode("/" + articleId);

        if (StringUtils.equals("true", request.getParameter("ajax"))) {
            // doAjax
            String articleTitle = StringUtils.defaultIfEmpty(request.getParameter("title"), "");
            String articleLead = StringUtils.defaultIfEmpty(request.getParameter("lead"), "");
            String articleBody = StringUtils.defaultIfEmpty(request.getParameter("body"), "");

            try {
                if (!articleTitle.isEmpty()) PropertyUtil.setProperty(node, "title", articleTitle);
                if (!articleLead.isEmpty()) PropertyUtil.setProperty(node, "lead", articleLead);
                if (!articleBody.isEmpty()) PropertyUtil.setProperty(node, "body", articleBody);

                JcrNodeAdapter itemChanged = new JcrNodeAdapter(node);

                // Set the Node Composite Name
                if (itemChanged instanceof JcrNewNodeAdapter || !node.getName().startsWith(defineNodeName(node))) {
                    final String newNodeName = generateUniqueNodeNameForArticle(node);
                    itemChanged.setNodeName(newNodeName);
                    NodeUtil.renameNode(node, newNodeName);
                }

                node.getSession().save();
            } catch (RepositoryException e) {
                log.error("RepositoryException caught: {}", e);
            }

            MgnlContext.setAttribute("contentPath", articleTitle);
            return "components/article.jsp";
        }

        // Auto-tagging player names
        String query = "select * from [nt:base] where [mgnl:deleted] is null";
        NodeIterator personnel = QueryUtil.search("personnel", query, "JCR-SQL2", "mgnl:personnel");

        ArrayList<String> allPersonnel = new ArrayList<String>();
        while (personnel.hasNext()) {
            Node personnelNode = personnel.nextNode();
            allPersonnel.add(personnelNode.getProperty("firstName").getString().toLowerCase() + " " + personnelNode.getProperty("lastName").getString().toLowerCase());
        }

        String linkedArticleBody = node.getProperty("body").getString();

        if (hasProperty(node, "categories")) {
            ServerConfiguration sc = ServerConfiguration.getInstance();
            String url = sc.getDefaultBaseUrl();

            Property nodeCategories = node.getProperty("categories");
            Value[] categories = nodeCategories.getValues();

            for (Value value : categories) {
                Node category = NodeUtil.getNodeByIdentifier("category", value.getString());
                String toCheck = category.getProperty("name").getString();
                if (allPersonnel.contains(toCheck.toLowerCase())) {
                    String lowerCaseName = toCheck.toLowerCase();

                    char firstLetterOfFirstName = lowerCaseName.charAt(0);
                    String[] splitName = lowerCaseName.split(" ");
                    String linkedName = firstLetterOfFirstName + splitName[1];

                    if (!linkedArticleBody.contains(toCheck + "<")) {
                        linkedArticleBody = linkedArticleBody.replaceFirst(toCheck + "(?!<)", "<a href='" + url + "/player?contentPath=" + linkedName + "'>" + toCheck + "</a>");
                    }
                }
            }
        }

        PropertyUtil.setProperty(node, "body", linkedArticleBody);
        node.getSession().save();

        /*
        query = "select * from [nt:base] where [mgnl:deleted] is null";
        NodeIterator articles = QueryUtil.search("articles", query, "JCR-SQL2", "mgnl:article");

        Map<String, String> articlePaths = new LinkedHashMap<String, String>();
        while (articles.hasNext()) {
            Node articleNode = articles.nextNode();
            articlePaths.put(articleNode.getPath(), PropertyUtil.getString(node, "title", ""));
        }

        model.put("articles", articlePaths);
        */

        model.put("params", MgnlContext.getParameters());
        model.put("contentPath", articleId);

        if (hasProperty(node, "author")) {
            Property nodeAuthor = node.getProperty("author");
            Value author = nodeAuthor.getValue();
            Node authorNode = NodeUtil.getNodeByIdentifier("authors", author.getString());
            model.put("authorFirstName", authorNode.getProperty("firstName").getString());
            model.put("authorLastName", authorNode.getProperty("lastName").getString());

            query = "select * from [nt:base] where isdescendantnode('" + NodeUtil.getNodePathIfPossible(authorNode) + "')";
            NodeIterator ni = QueryUtil.search("authors", query, "JCR-SQL2", "nt:base");
            while (ni.hasNext()) {
                Node n = ni.nextNode();
                model.put("authorImageLink", LinkUtil.createLink(n));
            }
        }

        return "components/article.jsp";
    }

    private boolean hasProperty(Node node, String name) throws RepositoryException {
        boolean hasProperty = false;
        PropertyIterator pi = node.getProperties();
        while (pi.hasNext()) {
            if (pi.nextProperty().getName().equals(name)) {
                hasProperty = true;
            }
        }
        return hasProperty;
    }

    private boolean hasNode(Node node, String name) throws RepositoryException {
        boolean hasNode = false;
        NodeIterator pi = node.getNodes();
        while (pi.hasNext()) {
            if (pi.nextNode().getName().equals(name)) {
                hasNode = true;
            }
        }
        return hasNode;
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
        String name = node.getProperty("title").getString();
        return name;
    }
}
