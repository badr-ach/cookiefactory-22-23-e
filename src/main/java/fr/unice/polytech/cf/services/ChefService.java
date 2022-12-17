package fr.unice.polytech.cf.services;

import fr.unice.polytech.cf.interfaces.CookieRecipeCreator;
import fr.unice.polytech.cf.interfaces.CookieRecipeModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChefService {
  private CookieRecipeModifier cookieRecipeModifier;
  private CookieRecipeCreator cookieRecipeCreator;

  public ChefService(){

  }

  @Autowired
  public ChefService(CookieRecipeCreator cookieRecipeCreator, CookieRecipeModifier cookieRecipeModifier){
    this.cookieRecipeCreator = cookieRecipeCreator;
    this.cookieRecipeModifier = cookieRecipeModifier;
  }

}
