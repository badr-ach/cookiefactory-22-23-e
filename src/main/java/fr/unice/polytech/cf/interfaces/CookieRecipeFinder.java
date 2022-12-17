package fr.unice.polytech.cf.interfaces;

import fr.unice.polytech.cf.entities.Cookie;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

public interface CookieRecipeFinder {
    List<Cookie> getCookies();
    List<Cookie> getCookiesSubmitted();
    List<Cookie> getCookiesValidated();
    List<Cookie> getCookiesActivated();
}
