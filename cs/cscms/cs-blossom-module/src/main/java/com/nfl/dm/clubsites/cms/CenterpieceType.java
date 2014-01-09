package com.nfl.dm.clubsites.cms;

/**
 * @author arash.shokoufandeh
 */
public enum CenterpieceType {
    HOME_PAGE("Home Page") {
        @Override
        public String getType() {
            return "home";
        }
    },
    NEWS_LANDING("News Landing") {
        @Override
        public String getType() {
            return "news";
        }
    },
    COMMUNITY_LANDING("Community Landing") {
        @Override
        public String getType() {
            return "community";
        }
    },
    TEAM_LANDING("Team Landing") {
        @Override
        public String getType() {
            return "team";
        }
    },
    CHEERLEADER_LANDING("Cheerleader Landing") {
        @Override
        public String getType() {
            return "cheerleader";
        }
    },
    DOES_NOT_EXIST("DNE") {
        @Override
        public String getType() {
            return "dne";
        }
    };

    private String value;

    private CenterpieceType(String value) {
        this.value = value;
    }

    public abstract String getType();

    public static CenterpieceType fromString(String text) {
        if (text != null) {
            for (CenterpieceType ct : CenterpieceType.values()) {
                if (text.equalsIgnoreCase(ct.value)) {
                    return ct;
                }
            }
        }
        return DOES_NOT_EXIST;
    }
}
