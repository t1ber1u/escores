package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.cms.core.Path;
import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
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
import java.util.*;

/**
 * Component for custom module esi.
 */

@Controller
@Template(title = "CustomModuleESI", id = "cs-blossom-module:components/customESI")
public class CustomModuleESIComponent {

    private static final Logger log = LoggerFactory.getLogger(CustomModuleESIComponent.class);

    @RequestMapping("/customESI")
    public String render(Node componentNode, ModelMap model) throws RepositoryException {
        String exId = componentNode.getProperty("cid").getString();
        model.put("moduleURL", "/cda-web/custom-module.htm?id=" + exId);
        return "components/custom-module-esi.jsp";
    }

    @TabFactory("Custom Content ID")
    public void contentId(UiConfig cfg, TabBuilder tab) {
        tab.fields(
                cfg.fields.basicTextCode("cid").label("Content Id").required()
        );
    }

}
