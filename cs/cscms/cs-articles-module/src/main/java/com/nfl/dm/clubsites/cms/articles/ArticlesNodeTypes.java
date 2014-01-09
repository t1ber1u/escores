package com.nfl.dm.clubsites.cms.articles;

import info.magnolia.jcr.util.NodeTypes;

/**
 * Node types defined by the articles app.
 */
public class ArticlesNodeTypes {

    /**
     * Represents the mgnl:article node type.
     */
    public static class Article {
        // Node type name
        public static final String NAME = NodeTypes.MGNL_PREFIX + "article";

        // Property names
        public static final String PROPERTY_TITLE = "title";
        public static final String PROPERTY_BYLINE = "byline";
        public static final String PROPERTY_LEAD = "lead";

        public static final String PROPERTY_TOP_IMAGE = "topImage";
        public static final String PROPERTY_TOP_IMAGE_CAPTION = "topImageCaption";
        public static final String PROPERTY_TOP_IMAGE_ALT_TEXT = "topImageAltText";

        public static final String PROPERTY_BODY_IMAGE_1 = "bodyImage1";
        public static final String PROPERTY_BODY_IMAGE_1_POSITION = "bodyImage1Position";
        public static final String PROPERTY_BODY_IMAGE_1_CAPTION = "bodyImage1Caption";
        public static final String PROPERTY_BODY_IMAGE_1_ALT_TEXT = "bodyImage1AltText";

        public static final String PROPERTY_BODY_IMAGE_2 = "bodyImage2";
        public static final String PROPERTY_BODY_IMAGE_2_POSITION = "bodyImage2Position";
        public static final String PROPERTY_BODY_IMAGE_2_CAPTION = "bodyImage2Caption";
        public static final String PROPERTY_BODY_IMAGE_2_ALT_TEXT = "bodyImage2AltText";
    }
}
