package com.nfl.dm.clubsites.cms;

import info.magnolia.cms.util.QueryUtil;
import info.magnolia.context.MgnlContext;
import info.magnolia.link.LinkUtil;
import info.magnolia.module.blossom.annotation.Template;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jcr.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Component for the player prototypical page.
 *
 * @author arash.shokoufandeh
 */

@Controller
@Template(title = "Player", id = "cs-blossom-module:components/player")
public class PlayerComponent {

    public static final int CONNECTION_TIMEOUT = 5000;
    public static final String cdsPlayerUrl = "http://feeds.nfl.com/feeds-rs/playerStats/";
    private final Log logger = LogFactory.getLog(PlayerComponent.class);

    @RequestMapping("/player")
    public String render(ModelMap model, HttpServletRequest request) throws RepositoryException {
        String playerId = MgnlContext.getParameter("contentPath");
        Node node = MgnlContext.getJCRSession("personnel").getNode("/" + playerId);

        Map<String, String> playerProperties = new LinkedHashMap<String, String>();
        PropertyIterator pi = node.getProperties();
        while (pi.hasNext()) {
            Property p = pi.nextProperty();
            playerProperties.put(p.getName(), p.getValue().getString());
        }

        // Add player photo
        String query = "select * from [nt:base] where isdescendantnode('/" + playerId + "')";
        NodeIterator ni = QueryUtil.search("personnel", query, "JCR-SQL2", "nt:base");
        while (ni.hasNext()) {
            Node n = ni.nextNode();
            playerProperties.put(n.getName(), LinkUtil.createLink(n));
        }

        model.put("player", playerProperties);
        model.put("contentPath", playerId);

        // Get player stats from CDS
        String playerStats = "";
        if (playerProperties.containsKey("eliasId")) {
            playerStats = getDataFromCDS(cdsPlayerUrl + playerProperties.get("eliasId"));
        }
        model.put("playerStats", playerStats);

        return "components/player.jsp";
    }

    protected String getDataFromCDS(String cdsURL) {
        logger.info("Getting " + cdsURL);
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);

        GetMethod get = new GetMethod(cdsURL);
        get.setRequestHeader("Content-Type", "application/json");
        try {
            client.executeMethod(get);
            return get.getResponseBodyAsString();
        } catch (IOException ioe) {
            logger.error("cdsURL: " + cdsURL);
            throw new RuntimeException("cdsURL: " + cdsURL, ioe);
        }
    }

}
