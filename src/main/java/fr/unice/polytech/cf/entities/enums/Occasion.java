package fr.unice.polytech.cf.entities.enums;

import fr.unice.polytech.cf.interfaces.Requirement;

public enum Occasion implements Requirement {
    BIRTHDAY("Birthday"),
    WEDDING("Wedding"),
    CHRISTMAS("Christmas"),
    HALLOWEEN("Halloween"),
    EASTER("Easter"),
    VALENTINE("Valentine"),
    NEW_YEAR("New Year"),
    OTHER("Other");

    private final String full;

    Occasion(String full) {
        this.full = full;
    }

    public String getFullName() {
        return full;
    }
}
