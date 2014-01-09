package com.nfl.dm.clubsites.cms;

import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.AvailableComponentClasses;
import info.magnolia.module.blossom.annotation.Template;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Component with two areas arranged as columns.
 */
@Controller
@Template(id="myModule:components/twoColumn", title="two column layout")
public class TwoColumnComponent {

    /**
     * Left column.
     */
    @Area("left")
    @Controller
    @AvailableComponentClasses({TextComponent.class, ArticleComponent.class})
    public static class LeftArea {

        @RequestMapping("/twoColumn/left")
        public String render() {
            return "areas/left.jsp";
        }
    }

    /**
     * Right column.
     */
    @Area("right")
    @Controller
    @AvailableComponentClasses({TextComponent.class})
    public static class RightArea {

        @RequestMapping("/twoColumn/right")
        public String render() {
            return "areas/right.jsp";
        }
    }

    @RequestMapping("/twoColumn")
    public String render() {
        return "components/twoColumns.jsp";
    }
}
