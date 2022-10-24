package fr.unice.polytech.cf.StoreService;

import fr.unice.polytech.cf.StoreService.Entities.Store;
import java.util.ArrayList;

public class StoreService {
  private ArrayList<Store> stores = new ArrayList<Store>();

  public StoreService() {
    for (int i = 0; i < 10; i++) {
      Store store = new Store();
      stores.add(store);
    }
  }

  public ArrayList<Store> getStores() {
    return stores;
  }

}
