package fr.unice.polytech.cf.cookieservice.interfaces;

import fr.unice.polytech.cf.cookieservice.entities.ingredients.Ingredient;
import fr.unice.polytech.cf.partycookieservice.Requirement;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface IPastry {
    Double getPrice();
    Map<Ingredient, Integer> getIngredients();
    Duration getPreparationDuration();
    List<Requirement> getRequirements();

    String getName();
}
