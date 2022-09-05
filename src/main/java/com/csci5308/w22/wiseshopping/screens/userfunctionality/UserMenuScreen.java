package com.csci5308.w22.wiseshopping.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Elizabeth James
 */

@Component
public class UserMenuScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMenuScreen.class);

    @Autowired
    private Scanner scanner;

    private List<String> validScreens;

    private Merchant merchant;

    private User user;


    public UserMenuScreen() {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens = new ArrayList<>(Arrays.asList(Constants.LOGOUT,
                Constants.CART,Constants.SUBSCRIPTIONS,
                Constants.USER_PRODUCTS,Constants.PROFILE,Constants.USER_MENU,Constants.PRICE_ANALYTICS));
    }

    /**
     * this shows the user menu screen to the user.
     * the user can further move to the products, cart, subscriptions, and analytics screen
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     * @contributor: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****USER MENU****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";

            do {
                LOGGER.info("Choose one of the following pages: products, subscriptions, profile, price_analytics, cart");
                input = scan(scanner);
                // go to products screen
                if (input.equalsIgnoreCase(Constants.PRODUCTS)) {
                    Screen screen = screenFactory.getScreen(Constants.USER_PRODUCTS);
                    screen.setUser(user);
                    success = screen.render(screenFactory);

                }
                // go to profile screen
                else if (input.equalsIgnoreCase(Constants.PROFILE)) {
                    Screen screen = screenFactory.getScreen(Constants.PROFILE);
                    screen.setUser(user);
                    success = screen.render(screenFactory);
                }
                // go to subscriptions screen
                else if (input.equalsIgnoreCase(Constants.SUBSCRIPTIONS)) {
                    Screen screen = screenFactory.getScreen(Constants.SUBSCRIPTIONS);
                    screen.setUser(user);
                    success = screen.render(screenFactory);
                }
                // go to analytics screen
                else if (input.equalsIgnoreCase(Constants.PRICE_ANALYTICS)) {
                    Screen screen = screenFactory.getScreen(Constants.PRICE_ANALYTICS);
                    screen.setUser(user);
                    success = screen.render(screenFactory);
                }
                // go to cart screen
                else if (input.equalsIgnoreCase(Constants.CART)) {
                    Screen screen = screenFactory.getScreen(Constants.CART);
                    screen.setUser(user);
                    success = screen.render(screenFactory);
                }

                else if (input.equalsIgnoreCase("exit")) {
                    LOGGER.info("Exiting user menu...");
                    success = true;
                } else {
                    LOGGER.warn("Invalid input received...");
                }
            } while (!input.equalsIgnoreCase("exit"));
        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.USER_MENU);
            screen.setUser(user);
            success = screen.render(screenFactory);
        }

        return success;
    }

    @Override
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }



}

