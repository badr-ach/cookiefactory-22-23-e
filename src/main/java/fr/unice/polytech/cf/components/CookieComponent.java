package fr.unice.polytech.cf.components;

import fr.unice.polytech.cf.entities.Account;
import fr.unice.polytech.cf.entities.enums.EAccountType;
import fr.unice.polytech.cf.entities.Cookie;
import fr.unice.polytech.cf.entities.enums.ECookieStatus;
import fr.unice.polytech.cf.exceptions.CookieNotSubmittedException;
import fr.unice.polytech.cf.exceptions.CookieNotValidatedException;
import fr.unice.polytech.cf.exceptions.UnauthorizedRoleException;
import fr.unice.polytech.cf.interfaces.CookieRecipeCreator;
import fr.unice.polytech.cf.interfaces.CookieRecipeFinder;
import fr.unice.polytech.cf.interfaces.CookieRecipeModifier;
import fr.unice.polytech.cf.interfaces.CookieRecipeValidator;
import fr.unice.polytech.cf.repositories.CookieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class CookieComponent
 * This class is the component of the cookie entity
 * It contains all the methods to manage the cookie entity
 */
@Component
public class CookieComponent implements CookieRecipeFinder, CookieRecipeValidator, CookieRecipeCreator, CookieRecipeModifier {
    private CookieRepository cookieRepository;
    @Autowired
    public CookieComponent(CookieRepository cookieRepository) {
        this.cookieRepository = cookieRepository;
    }


    /**
     * Get all the cookies
     * @return a list of cookies in the repository
     */
    @Override
    public List<Cookie> getCookies() {
    ArrayList<Cookie> cookies = new ArrayList<>();
        cookieRepository.findAll().forEach(cookies::add);
        return cookies;
    }


    /**
     * Create a Random Cookie for testing
     * @return a cookie
     */
    @Override
    public Cookie initializeACookie() {
        Cookie cookie = new Cookie();
        cookieRepository.save(cookie, cookie.getId());
        return cookie;
    }


    /**
     * Get all cookies with the status SUBMITTED in the repository
     * @return a list of cookies with the status SUBMITTED
     */
    @Override
    public ArrayList<Cookie> getCookiesSubmitted() {
        ArrayList<Cookie> cookiesSubmitted = new ArrayList<>();
        cookieRepository.findAll().forEach(cookie -> {
            if (cookie.getStatus().equals(ECookieStatus.SUBMITTED))
                cookiesSubmitted.add(cookie);
        });
        return cookiesSubmitted;

    }


    /**
     * Get all cookies with the status VALIDATED in the repository
     * @return a list of cookies with the status VALIDATED
     */
    @Override
    public ArrayList<Cookie> getCookiesActivated() {
        ArrayList<Cookie> cookiesActivated = new ArrayList<>();
        cookieRepository.findAll().forEach(cookie -> {
            if (cookie.getStatus().equals(ECookieStatus.ACTIVE))
                cookiesActivated.add(cookie);
        });
        return cookiesActivated;
    }


    /**
     * Get all cookies with the status VALIDATED in the repository
     * @return a list of cookies with the status VALIDATED
     */
    @Override
    public ArrayList<Cookie> getCookiesValidated() {
        ArrayList<Cookie> cookiesValidated = new ArrayList<>();
        cookieRepository.findAll().forEach(cookie -> {
            if (cookie.getStatus().equals(ECookieStatus.VALIDATED))
                cookiesValidated.add(cookie);
        });
        return cookiesValidated;
    }


    /**
    * Add a cookie to the repository
     */
    @Override
    public void addCookie(Cookie cookie) {
        cookieRepository.save(cookie, cookie.getId());
        }


    /**
     * Delete a cookie from the repository
     * @param cookie the cookie to delete
     * @param account the account that want to delete the cookie
     * @throws UnauthorizedRoleException if the account is not a Chef or a BrandManager
     */
    @Override
    public void deleteCookie(Cookie cookie, Account account) throws UnauthorizedRoleException {
        if(!(account.getType().equals(EAccountType.CHEF)||account.getType().equals(EAccountType.BRAND_MANAGER))) {
            throw new UnauthorizedRoleException("You are not allowed to delete a cookie");
        }
        cookieRepository.deleteById(cookie.getId());
    }

    /**
     * Submit a cookie
     * @param cookie the cookie to submit
     * @param account the account that want to submit the cookie
     */
    @Override
    public void submitCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.DRAFT)))
            throw new CookieNotSubmittedException("This cookie can't be submitted to the brand manager");
        if (!(account.getType().equals(EAccountType.CHEF)))
            throw new UnauthorizedRoleException("Your account can't submit a cookie");
        cookie.setStatus(ECookieStatus.SUBMITTED);
    }

    /**
     * Validate a cookie
     * @param cookie the cookie to validate
     * @param account the account that want to validate the cookie
     */
    @Override
    public void validateCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.SUBMITTED)))
            throw new CookieNotSubmittedException("This cookie isn't submitted to the brand manager");
        if (!(account.getType().equals(EAccountType.BRAND_MANAGER)))
            throw new UnauthorizedRoleException("Your account can't validate a cookie");
        cookie.setStatus(ECookieStatus.VALIDATED);
    }

/**
     * Activate a cookie so it becomes available for the customers
     * @param cookie the cookie to activate
     * @param account the account that want to activate the cookie
     */
    @Override
    public void activateCookie(Cookie cookie, Account account) {
        if (!(cookie.getStatus().equals(ECookieStatus.VALIDATED)))
            throw new CookieNotValidatedException("This cookie isn't validate by the brand manager");
        if (!(account.getType().equals(EAccountType.CHEF)))
            throw new UnauthorizedRoleException("Your account can't activate a cookie");
        cookie.setStatus(ECookieStatus.ACTIVE);
    }

}











