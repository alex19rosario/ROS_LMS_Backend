package com.ros.lms.domain.enums;

public enum GenreType {

    MYSTERY("MYSTERY"),
    SCIENCE_FICTION("SCIENCE FICTION"),
    ROMANCE("ROMANCE"),
    FANTASY("FANTASY"),
    THRILLER("THRILLER"),
    HISTORICAL_FICTION("HISTORICAL FICTION"),
    HORROR("HORROR"),
    BIOGRAPHY("BIOGRAPHY"),
    SELF_HELP("SELF-HELP"),
    ADVENTURE("ADVENTURE"),
    CHILDREN_LITERATURE("CHILDREN'S LITERATURE"),
    NON_FICTION("NON-FICTION"),
    GRAPHIC_NOVEL("GRAPHIC NOVEL"),
    POETRY("POETRY"),
    DYSTOPIAN("DYSTOPIAN"),
    CRIME("CRIME"),
    AUTOBIOGRAPHY("AUTOBIOGRAPHY"),
    ESSAYS("ESSAYS"),
    PHILOSOPHY("PHILOSOPHY"),
    RELIGION("RELIGION"),
    SCIENCE("SCIENCE"),
    ART("ART"),
    COOKING("COOKING"),
    TRAVEL("TRAVEL"),
    SPORTS("SPORTS"),
    TECHNOLOGY("TECHNOLOGY"),
    DRAMA("DRAMA"),
    POLITICAL_FICTION("POLITICAL FICTION"),
    LANGUAGE("LANGUAGE"),
    HISTORY("HISTORY"),
    MAGAZINE("MAGAZINE"),
    FICTION("FICTION");

    private final String label;

    GenreType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static GenreType fromLabel(String label) {
        for (GenreType genre : values()) {
            if (genre.label.equalsIgnoreCase(label)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre label: " + label);
    }
}
