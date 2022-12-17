package fr.unice.polytech.cf.services;

import fr.unice.polytech.cf.interfaces.StoreCookModifier;
import fr.unice.polytech.cf.interfaces.StoreScheduleModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreManagerService {
    private StoreScheduleModifier storeScheduleModifier;
    private StoreCookModifier storeCookModifier;

    @Autowired
    StoreManagerService(StoreScheduleModifier storeScheduleModifier, StoreCookModifier storeCookModifier){
        this.storeScheduleModifier = storeScheduleModifier;
        this.storeCookModifier = storeCookModifier;
    }
}
