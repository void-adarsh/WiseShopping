package com.csci5308.w22.wiseshopping.screens;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

/**
 * @author Elizabeth James
 * This interface denotes the various screen used in the applicatin
 */
public interface Screen {
    Logger LOGGER = LoggerFactory.getLogger(CartService.class);
    void setMerchant(Merchant merchant);
    void setUser(User user);
    boolean render(ScreenFactory screenFactory);

    /**
     * this method shows the list of screens that the user can navigate to from the current screen
     * @param screenFactory
     * @param validScreens
     * @param logger
     * @param scanner
     * @return true on success
     * @author: Elizabeth James
     */
    default boolean getNavigations(ScreenFactory screenFactory, List<String> validScreens, Logger logger, Scanner scanner) {
        String possibleScreens = "";

        for (String s : validScreens){
            possibleScreens += s + ", ";
        }
        logger.info("For additional navigation enter \":\"");
        logger.info("Enter the page name");
        logger.info("Possible pages: {}", possibleScreens.substring(0,possibleScreens.length() -2));

        String screen = null;
        try {
            screen = scan(scanner);
            if(screen.equalsIgnoreCase(Constants.HELP)){
                showHelp();
                getNavigations(screenFactory,validScreens,logger,scanner);
            }
            while (!validScreens.contains(screen)){
                logger.error("Invalid screen. Please try again");
                screen = scan(scanner);
            }
            screenFactory.getScreen(screen).render(screenFactory);
        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory,validScreens,logger,scanner);
        }
        return true;
    }

    /**
     * this method gets the input from the user. It redirects to the additional screen on
     * entering ":"
     * @param scanner
     * @return
     * @throws MenuInterruptedException
     * @author: Elizabeth James
     */
    default String scan (Scanner scanner) throws MenuInterruptedException {
        String s = scanner.next();
        if (":".equals(s)){
            throw new MenuInterruptedException("");
        }
        if (s.equalsIgnoreCase(Constants.QUIT)){
            System.exit(0);
        }

        return s;
    }

    default void showHelp() {
        LOGGER.info("Use \":\" for additional naviagtion on any page");
        LOGGER.info("Use quit to exit the application");
        LOGGER.info("Following is the map");
        LOGGER.info("User menu screen -> Products menu");
        LOGGER.info("                 -> Cart menu");
        LOGGER.info("                 -> Analytics menu");
        LOGGER.info("                 -> Subscriptions menu");
        LOGGER.info("                 -> Profile menu");
        LOGGER.info("Merchant menu screen -> store menu");
        LOGGER.info("                     -> products menu");
        LOGGER.info("                     -> analytics menu");
    }
}
