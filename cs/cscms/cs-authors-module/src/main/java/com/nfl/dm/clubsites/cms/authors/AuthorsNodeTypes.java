package com.nfl.dm.clubsites.cms.authors;

import info.magnolia.jcr.util.NodeTypes;

/**
 * @author arash.shokoufandeh
 */
public class AuthorsNodeTypes {

    /**
     * Represents the mgnl:author node type.
     */
    public static class Author {
        // Node type name
        public static final String NAME = NodeTypes.MGNL_PREFIX + "author";

        // Property names
        public static final String PROPERTY_FIRST_NAME = "firstName";
        public static final String PROPERTY_LAST_NAME = "lastName";
        public static final String PROPERTY_EMAIL = "email";
        public static final String IMAGE_NODE_NAME = "photo";
        public static final String PROPERTY_PHOTO_ALT_TEXT = "photoAltText";
        public static final String PROPERTY_PHOTO_CAPTION = "photoCaption";

        public static final String PROPERTY_TWITTER = "twitter";
        public static final String PROPERTY_FACEBOOK = "facebook";
        public static final String PROPERTY_WEBSITE = "website";

        public static final String PROPERTY_MOBILE_PHONE_NR = "mobilePhoneNr";
        public static final String PROPERTY_OFFICE_FAX_NR = "officeFaxNr";
        public static final String PROPERTY_OFFICE_PHONE_NR = "officePhoneNr";
    }
}
