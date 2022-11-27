package fr.unice.polytech.cf.partycookieservice.entities;

import fr.unice.polytech.cf.cookieservice.interfaces.IPastry;
import fr.unice.polytech.cf.cookieservice.entities.Cookie;
import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.partycookieservice.Requirement;
import fr.unice.polytech.cf.partycookieservice.enums.Occasion;
import fr.unice.polytech.cf.partycookieservice.enums.Size;
import fr.unice.polytech.cf.partycookieservice.enums.Theme;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasePartyCookie implements IPastry {
    private Cookie cookie;
    private Size size;
    private Theme theme;
    private Occasion occasion;


    public BasePartyCookie(Cookie cookie, Size size, Theme theme, Occasion occasion) {
        this.cookie = cookie;
        this.size = size;
        this.theme = theme;
        this.occasion = occasion;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
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
        return cookie.getPrice() * (size.getMultiplier() + 0.25);
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
