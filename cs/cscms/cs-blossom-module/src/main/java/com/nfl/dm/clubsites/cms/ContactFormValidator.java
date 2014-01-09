package com.nfl.dm.clubsites.cms;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for validating contact forms.
 */
public class ContactFormValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return clazz.equals(ContactForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required", "Name is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "E-mail is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "required", "Message is required");
    }
}
