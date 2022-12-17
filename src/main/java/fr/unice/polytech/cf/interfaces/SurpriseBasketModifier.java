package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.connectors.TooGoodToGoProxy;

import java.time.LocalDateTime;

public interface SurpriseBasketModifier {
    void updateSurpriseBaskets(LocalDateTime currentDate, TooGoodToGoProxy toGoodToGo);
}
