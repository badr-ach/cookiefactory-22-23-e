package fr.unice.polytech.cf.connectors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.unice.polytech.cf.entities.Order;
import fr.unice.polytech.cf.entities.Store;
import fr.unice.polytech.cf.entities.SurpriseBasket;
import fr.unice.polytech.cf.interfaces.ISurpriseBasketObserver;
import org.springframework.stereotype.Component;


@Component
public class TooGoodToGoProxy {
    private List<ISurpriseBasketObserver> observers;
    private List<SurpriseBasket> surpriseBaskets;

    public TooGoodToGoProxy() {
        observers = new ArrayList<ISurpriseBasketObserver>();
        surpriseBaskets = new ArrayList<SurpriseBasket>();
    }

    public void makeSurpriseBasket(List<Order> obsoleteOrders, LocalDateTime dateTime) {
        List<SurpriseBasket> newSurpriseBaskets = new ArrayList<SurpriseBasket>();

        for (Order order : obsoleteOrders) {
            double price = order.getPrice();
            Store store = order.getStore();
            int quantity = order.getTotalItems();

            SurpriseBasket surpriseBasket = new SurpriseBasket(store, price, quantity);
            newSurpriseBaskets.add(surpriseBasket);
        }

        notifyObservers(newSurpriseBaskets, dateTime);
        surpriseBaskets.addAll(newSurpriseBaskets);
    }

    public void removeSurpriseBasket(SurpriseBasket surpriseBasket) {
        surpriseBaskets.remove(surpriseBasket);
    }

    public void addObserver(ISurpriseBasketObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(ISurpriseBasketObserver obs) {
        observers.remove(obs);
    }

    public void notifyObservers(List<SurpriseBasket> surpriseBaskets, LocalDateTime dateTime) {
        for (ISurpriseBasketObserver obs : observers) {
            obs.update(surpriseBaskets, dateTime);
        }
    }

    public List<SurpriseBasket> getSurpriseBaskets() {
        return surpriseBaskets;
    }
}
