package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.core.Path;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.module.blossom.annotation.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.*;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Component for the News landing page
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "News", id = "cs-blossom-module:components/news")
public class NewsComponent {

    private static final Logger log = LoggerFactory.getLogger(NewsComponent.class);

    @RequestMapping("/news")
    public String render(ModelMap model, HttpServletRequest request) throws RepositoryException {
        String query = "select * from [nt:base] where [mgnl:deleted] is null";
        NodeIterator articles = QueryUtil.search("articles", query, "JCR-SQL2", "mgnl:article");

        Map<String, Map<String, String>> articleNodes = new LinkedHashMap<String, Map<String, String>>();
        while (articles.hasNext()) {
            Map<String, String> articleProperties = new LinkedHashMap<String, String>();
            Node node = articles.nextNode();
            PropertyIterator pi = node.getProperties();
            while (pi.hasNext()) {
                Property p = pi.nextProperty();
                if (p.getName().equals("centerpiece") || p.getName().equals("categories")) {
                    articleProperties.put(p.getName(), p.getValues().toString());
                } else {
                    articleProperties.put(p.getName(), p.getValue().getString());
                }
            }
            articleNodes.put(node.getPath(), articleProperties);
        }

        model.put("articles", articleNodes);
        model.put("params", MgnlContext.getParameters());
        model.put("contentPath", MgnlContext.getParameter("contentPath"));

        return "components/news.jsp";
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
