package fr.unice.polytech.cf.toogoodtogo.interfaces;
import java.time.LocalDateTime;
import java.util.List;

import fr.unice.polytech.cf.toogoodtogo.entities.SurpriseBasket;

public interface ISurpriseBasketObserver{
  void update(List<SurpriseBasket> surpriseBaskets, LocalDateTime dateTime);
}
