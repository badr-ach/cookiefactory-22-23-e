package fr.unice.polytech.cf.StoreService;

import fr.unice.polytech.cf.StoreService.Entities.Store;
import java.util.ArrayList;
import java.util.Optional;

public class StoreService {
  private ArrayList<Store> stores = new ArrayList<Store>();

  public StoreService() {
    Store store = new Store();
    store.setId(getStores().size()+1);
    stores.add(store);
  }
  public boolean addStore(Store store){
    return stores.add(store);
  }
  public ArrayList<Store> getStores() {
    return stores;
  }
  public Optional<Store> getStore(int id) { return stores.stream().filter(o -> o.getId() == id).findFirst();}


}
