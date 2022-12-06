package fr.unice.polytech.cf.entities.enums;

import fr.unice.polytech.cf.interfaces.Requirement;

public enum Theme implements Requirement {
    MUSIC("MUSIC"),
    PRINCESSES("Princesses"),
    CARTOON("Cartoon"),
    FANTASY("Fantasy"),
    OTHER("Other");

    private final String full;

    Theme(String full) {
        this.full = full;
    }

    public String getFullName() {
        return full;
    }
}
