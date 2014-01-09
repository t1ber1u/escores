package com.nfl.dm.clubsites.cms;

import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.annotation.TemplateDescription;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Simple component for adding text to a page.
 */
@Controller
@Template(title = "Text", id = "cs-blossom-module:components/text")
@TemplateDescription("Simple text block")
@Promo
public class TextComponent {

    @RequestMapping("/text")
    public String render() {
        return "components/text.jsp";
    }

    @TabFactory("Content")
    public void contentTab(UiConfig cfg, TabBuilder tab) {
        tab.fields(
                cfg.fields.text("heading").label("Heading"),
                cfg.fields.richText("body").label("Text body")
        );
    }
}
