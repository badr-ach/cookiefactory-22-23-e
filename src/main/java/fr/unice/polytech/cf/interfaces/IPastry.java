package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Ingredient;

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
