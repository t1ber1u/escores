package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.core.MgnlNodeType;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.annotation.*;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@Template(title = "Main", id = "cs-blossom-module:pages/main")
public class CSTemplate {

    @Area("main")
    @Controller
    @AvailableComponentClasses({TextComponent.class, TwoColumnComponent.class, ContactFormComponent.class,
            ArticleComponent.class, NewsComponent.class, CenterpieceComponent.class,
            SiteAlertComponent.class, PlayerComponent.class, CustomModuleESIComponent.class
    })
    public static class MainArea {

        @RequestMapping("/mainTemplate/main")
        public String render() {
            return "areas/main.jsp";
        }
    }

    @Area("alert")
    @Controller
    @AvailableComponentClasses({SiteAlertComponent.class})
    public static class AlertArea {

        @RequestMapping("/mainTemplate/alert")
        public String render() {
            return "areas/alert.jsp";
        }
    }

    @Controller
    @Area(value = "promos", title = "Promos", optional = TernaryBoolean.TRUE)
    @Inherits
    @AvailableComponentClasses({Promo.class})
    public static class PromosArea {

        @RequestMapping("/mainTemplate/promos")
        public String render() {
            return "areas/promos.jsp";
        }
    }

    @RequestMapping("/mainTemplate")
    public String render(Node page, ModelMap model) throws RepositoryException {

        Map<String, String> navigation = new LinkedHashMap<String, String>();

        String path="";
        for (Node node : NodeUtil.getNodes(page.getSession().getNode("/"), MgnlNodeType.NT_PAGE)) {
            path = node.getPath();
            break;
        }

        for (Node node : NodeUtil.getNodes(page.getSession().getNode(path), MgnlNodeType.NT_PAGE)) {
            if (!PropertyUtil.getBoolean(node, "hideInNavigation", false)) {
                navigation.put(node.getPath(), PropertyUtil.getString(node, "title", ""));
            }
        }
        model.put("navigation", navigation);

        return "pages/main.jsp";
    }

    @TabFactory("Content")
    public void contentTab(UiConfig cfg, TabBuilder tab) {
        tab.fields(
                cfg.fields.text("title").label("Title"),
                cfg.fields.checkbox("hideInNavigation").label("Hide in navigation").description("Check this box to hide this page in navigation")
        );
    }
}
