package fr.unice.polytech.cf.services;

import fr.unice.polytech.cf.interfaces.CookieRecipeFinder;
import fr.unice.polytech.cf.interfaces.CookieRecipeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandManagerService {
    BrandManagerService(){}
    private CookieRecipeValidator cookieRecipeValidator;
    private CookieRecipeFinder cookieRecipeFinder;

    @Autowired
    BrandManagerService(CookieRecipeValidator cookieRecipeValidator, CookieRecipeFinder cookieRecipeFinder){
        this.cookieRecipeValidator = cookieRecipeValidator;
        this.cookieRecipeFinder = cookieRecipeFinder;
    }
}