package com.nfl.dm.clubsites.cms;

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
 * Component for site alerts (breaking news)
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "Alert", id = "cs-blossom-module:components/alert")
public class SiteAlertComponent {

    private static final Logger log = LoggerFactory.getLogger(NewsComponent.class);

    @RequestMapping("/alert")
    public String render(ModelMap model, HttpServletRequest request) throws RepositoryException {
        String query = "select * from [nt:base] where [mgnl:deleted] is null and siteAlertalertText is not null";
        NodeIterator articles = QueryUtil.search("articles", query, "JCR-SQL2", "mgnl:article");

        Map<String, String> articleNodes = new LinkedHashMap<String, String>();
        while (articles.hasNext()) {
            Node node = articles.nextNode();
            PropertyIterator pi = node.getProperties();
            while (pi.hasNext()) {
                Property p = pi.nextProperty();
                if (p.getName().equals("centerpiece") || p.getName().equals("categories")) {
                    articleNodes.put(p.getName(), p.getValues().toString());
                } else {
                    articleNodes.put(p.getName(), p.getValue().getString());
                }
            }
        }

        model.put("articles", articleNodes);

        model.put("params", MgnlContext.getParameters());
        model.put("contentPath", MgnlContext.getParameter("contentPath"));

        return "components/alert.jsp";
    }
}
