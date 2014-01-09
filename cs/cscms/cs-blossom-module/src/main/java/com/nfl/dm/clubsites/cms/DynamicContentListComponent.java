package com.nfl.dm.clubsites.cms;

import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Component for dynamic content lists
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "Dynamic Content List", id = "cs-blossom-module:components/dcl")
public class DynamicContentListComponent {

    @RequestMapping("/dcl")
    public String render(ModelMap model, HttpServletRequest request) throws RepositoryException {
        return "components/dcl.jsp";
    }

    @TabFactory("Content Type")
    public void contentTypeTab(UiConfig cfg, TabBuilder tab) {
        Collection<String> options = new ArrayList<String>(Arrays.asList(new String[]{
                "Authors", "Articles", "Photos", "Personnel"
        }));
        tab.fields(
                cfg.fields.select("type").label("Type of Content").required().options(options)
        );

    }
}
