package com.nfl.dm.clubsites.cms.personnel;

import info.magnolia.jcr.util.NodeTypes;

/**
 * @author arash.shokoufandeh
 */
public class PersonnelNodeTypes {

    /**
     * Represents the mgnl:personnel node type.
     */
    public static class Personnel {
        // Node type name
        public static final String NAME = NodeTypes.MGNL_PREFIX + "personnel";

        // Property names
        public static final String PROPERTY_FIRST_NAME = "firstName";
        public static final String PROPERTY_LAST_NAME = "lastName";
        public static final String PROPERTY_TYPE = "type";
        public static final String IMAGE_NODE_NAME = "photo";
        public static final String PROPERTY_PHOTO_ALT_TEXT = "photoAltText";
        public static final String PROPERTY_PHOTO_CAPTION = "photoCaption";

        public static final String PROPERTY_ELIAS = "eliasId";
    }
}
