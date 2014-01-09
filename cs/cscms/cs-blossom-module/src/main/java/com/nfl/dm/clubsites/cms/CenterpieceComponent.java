package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Component for centerpieces
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "Centerpiece", id = "cs-blossom-module:components/centerpiece")
public class CenterpieceComponent {

    private static final Logger log = LoggerFactory.getLogger(CenterpieceComponent.class);

    @RequestMapping("/centerpiece")
    public String render(Node pageNode, Node componentNode, ModelMap model, HttpServletRequest request) throws RepositoryException {
        CenterpieceType centerpieceType = CenterpieceType.fromString(componentNode.getProperty("type").getString());
        String type = centerpieceType.getType();
        String query = "select * from [nt:base] where centerpiece = '" + type + "'";
        NodeIterator articles = QueryUtil.search("articles", query, "JCR-SQL2", "mgnl:article");

        Map<String, String> articlePaths = new LinkedHashMap<String, String>();
        while (articles.hasNext()) {
            Node node = articles.nextNode();
            articlePaths.put(node.getPath(), PropertyUtil.getString(node, "title", ""));
        }

        model.put("articles", articlePaths);

        model.put("params", MgnlContext.getParameters());
        model.put("contentPath", MgnlContext.getParameter("contentPath"));

        return "components/centerpiece.jsp";
    }

    @TabFactory("Centerpiece Type")
    public void centerpieceTypeTab(UiConfig cfg, TabBuilder tab) {
        Collection<String> options = new ArrayList<String>(Arrays.asList(new String[]{
                "Home Page", "News Landing", "Community Landing", "Team Landing", "Cheerleader Landing"
        }));
        tab.fields(
                cfg.fields.select("type").label("Type of Centerpiece").required().options(options)
        );
    }
}
