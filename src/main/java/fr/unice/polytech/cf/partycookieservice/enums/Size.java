package fr.unice.polytech.cf.partycookieservice.enums;

public enum Size {
    L("Large","L",4),
    XL("Extra Large","XL",5),
    XXL("Extra Extra Large","XXL",6);

    private final String full;
    private final String abbr;
    private final int multiplier;

    Size(String full, String abbr, int impact) {
        this.full = full;
        this.abbr = abbr;
        this.multiplier = impact;
    }

    public String getFullName() {
        return full;
    }

    public String getAbbreviatedName() {
        return abbr;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
