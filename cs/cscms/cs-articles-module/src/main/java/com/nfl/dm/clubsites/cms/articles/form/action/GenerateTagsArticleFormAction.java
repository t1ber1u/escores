package com.nfl.dm.clubsites.cms.articles.form.action;

import com.nfl.dm.clubsites.cms.articles.categorization.NFLCategoryGeneratorCommand;
import com.nfl.dm.clubsites.cms.articles.categorization.OpenCalaisRESTPost;
import info.magnolia.commands.CommandsManager;
import info.magnolia.ui.api.action.ActionExecutionException;
import info.magnolia.ui.api.app.SubAppContext;
import info.magnolia.ui.api.location.LocationController;
import info.magnolia.ui.form.EditorCallback;
import info.magnolia.ui.form.EditorValidator;
import info.magnolia.ui.form.action.SaveFormAction;
import info.magnolia.ui.form.action.SaveFormActionDefinition;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author arash.shokoufandeh
 */
public class GenerateTagsArticleFormAction extends SaveFormAction {

    private static final Logger log = LoggerFactory.getLogger(GenerateTagsArticleFormAction.class);

    private Map<String, ArrayList<String>> categorization;
    private SubAppContext subAppContext;
    private LocationController locationController;

    public GenerateTagsArticleFormAction(SaveFormActionDefinition definition, JcrNodeAdapter item, EditorCallback callback, EditorValidator validator, SubAppContext subAppContext, LocationController locationController) {
        super(definition, item, callback, validator);
        this.subAppContext = subAppContext;
        this.locationController = locationController;
    }

    @Override
    public void execute() throws ActionExecutionException {
        // First Validate
        validator.showValidation(true);
        if (validator.isValid()) {
            try {
                final Node node = item.applyChanges();
                // Set the Node name.
                setNodeName(node, item);
                //node.getSession().save();

                // Get categorization
                OpenCalaisRESTPost openCalaisRESTPost = new OpenCalaisRESTPost();
                categorization = openCalaisRESTPost.execute(node);

                CommandsManager cm = CommandsManager.getInstance();
                NFLCategoryGeneratorCommand command = (NFLCategoryGeneratorCommand) cm.getCommand("categorization-nflGenerateCategories");
                command.setRepository("articles");
                command.setPath("/");
                command.execute(categorization, node);
            } catch (final RepositoryException e) {
                throw new ActionExecutionException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Open the preview SubApp
            locationController.goTo(subAppContext.getLocation());
            //subAppContext.close();
        } else {
            log.info("Validation error(s) occurred. No save performed.");
        }
    }
}
