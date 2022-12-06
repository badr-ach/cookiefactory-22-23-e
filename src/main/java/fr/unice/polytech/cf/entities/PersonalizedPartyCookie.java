package fr.unice.polytech.cf.entities;

import fr.unice.polytech.cf.interfaces.IPastry;
import fr.unice.polytech.cf.interfaces.Requirement;
import fr.unice.polytech.cf.entities.enums.Occasion;
import fr.unice.polytech.cf.entities.enums.Theme;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalizedPartyCookie implements IPastry {
    private Cookie cookie;
    private Theme theme;
    private Occasion occasion;

    public PersonalizedPartyCookie(Cookie cookie, Theme theme, Occasion occasion) {
        this.cookie = cookie;
        this.theme = theme;
        this.occasion = occasion;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    @Override
    public String getName() {
        return cookie.getName();
    }

    @Override
    public Double getPrice() {
        return cookie.getPrice() * (1 + 0.4);
    }

    @Override
    public Map<Ingredient, Integer> getIngredients() {
        return cookie.getIngredients();
    }

    @Override
    public Duration getPreparationDuration() {
        return cookie.getPreparationDuration();
    }

    public List<Requirement> getRequirements(){
        List<Requirement> requirements = new ArrayList<>();
        requirements.add(theme);
        requirements.add(occasion);
        return requirements;
    }
}
