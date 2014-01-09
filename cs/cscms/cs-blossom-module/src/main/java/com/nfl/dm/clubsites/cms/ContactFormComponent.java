package com.nfl.dm.clubsites.cms;

import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.annotation.TemplateDescription;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Displays a contact form and a "Thank You" page after the contact form is submitted.
 */
@Controller
@Template(title = "Contact Form", id = "cs-blossom-module:components/contactForm")
@TemplateDescription("A contact form where visitors can get in contact with a sales person by filling in a form")
public class ContactFormComponent {

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String viewForm(@ModelAttribute ContactForm contactForm) {
        return "components/contactForm.ftl";
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String handleSubmit(@ModelAttribute ContactForm contactForm, BindingResult result, Node content) throws RepositoryException {

        new ContactFormValidator().validate(contactForm, result);

        if (result.hasErrors()) {
            return "components/contactForm.ftl";
        }

        return "website:" + content.getProperty("successPage").getString();
    }

    @TabFactory("Content")
    public void contentTab(UiConfig cfg, TabBuilder tab) {
        tab.fields(
                cfg.fields.pageLink("successPage").label("Success page")
        );
    }
}
