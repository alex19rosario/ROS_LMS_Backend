package com.ros.lms.domain.enums;

public enum GenreType {

    ADVENTURE("ADVENTURE"),
    ART("ART"),
    AUTOBIOGRAPHY("AUTOBIOGRAPHY"),
    BIOGRAPHY("BIOGRAPHY"),
    CHILDREN_LITERATURE("CHILDREN LITERATURE"),
    COOKING("COOKING"),
    CRIME("CRIME"),
    DRAMA("DRAMA"),
    DYSTOPIAN("DYSTOPIAN"),
    ESSAYS("ESSAYS"),
    FANTASY("FANTASY"),
    FICTION("FICTION"),
    GRAPHIC_NOVEL("GRAPHIC NOVEL"),
    HISTORICAL_FICTION("HISTORICAL FICTION"),
    HISTORY("HISTORY"),
    HORROR("HORROR"),
    LANGUAGE("LANGUAGE"),
    MAGAZINE("MAGAZINE"),
    MYSTERY("MYSTERY"),
    NON_FICTION("NON-FICTION"),
    PHILOSOPHY("PHILOSOPHY"),
    POETRY("POETRY"),
    POLITICAL_FICTION("POLITICAL FICTION"),
    RELIGION("RELIGION"),
    ROMANCE("ROMANCE"),
    SCIENCE("SCIENCE"),
    SCIENCE_FICTION("SCIENCE FICTION"),
    SELF_HELP("SELF-HELP"),
    SPORTS("SPORTS"),
    TECHNOLOGY("TECHNOLOGY"),
    THRILLER("THRILLER"),
    TRAVEL("TRAVEL");

    private final String val;

    GenreType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public static GenreType fromLabel(String label) {
        for (GenreType genre : values()) {
            if (genre.val.equalsIgnoreCase(label)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre label: " + label);
    }
}
