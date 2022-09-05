package com.csci5308.w22.wiseshopping.screens.signin;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.exceptions.user.UserAlreadyRegisteredException;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Elizabeth James
 */
@Component
public class RegistrationScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationScreen.class);

    @Autowired
    private Scanner scanner;

    private ArrayList<String> validScreens;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserService userService;

    private Merchant merchant;
    private User user;


    @Autowired
    public RegistrationScreen() {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGIN, Constants.RESET_PASSWORD, Constants.REGISTER));

    }


    /**
     * this shows the registration screen to the user.
     * On successful registration, the user is redirected to the subsequent menu screen
     * On failure, the user is redirected to reset password screen
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        boolean success = false;
        LOGGER.info("***REGISTRATION****");
        LOGGER.info("use : for additional navigation");
        try {

            String input = "";
            do{
            LOGGER.info("Are you a merchant or a user?");
            input = scan(scanner);

            // merchant registration
            if (Constants.MERCHANT.equalsIgnoreCase(input)) {
                LOGGER.info("Enter <name> <email> <password> <security code>");
                String name = scan(scanner);
                String email = scan(scanner);
                String password = scan(scanner);
                String securityCode = scan(scanner);
                merchant = merchantService.registerMerchant(name, email, password,securityCode);
                Screen screen = screenFactory.getScreen(Constants.MERCHANT_MENU);
                screen.setMerchant(merchant);
                success = screen.render(screenFactory);
            }

            // user registration
            if (Constants.USER.equalsIgnoreCase(input)) {
                LOGGER.info("Enter <first name> <last name> <email> <password> <contact> <security code>");
                String firstName = scan(scanner);
                String secondName = scan(scanner);
                String email = scan(scanner);
                String password = scan(scanner);
                String contact = scan(scanner);
                String securityCode = scan(scanner);
                user = userService.registerUser(firstName, secondName, email, password,contact,securityCode);
                Screen screen = screenFactory.getScreen(Constants.USER_MENU);
                screen.setUser(user);
                success = screen.render(screenFactory);
            }
            else  if (input.equalsIgnoreCase("exit")){
                success = true;
            }
            else {
                LOGGER.warn("Invalid input received...");
            }
        }while (!input.equalsIgnoreCase("exit"));

        } catch (UserAlreadyRegisteredException e) {
            LOGGER.warn(e.getMessage());
            Screen screen = screenFactory.getScreen(Constants.LOGIN);
            success = screen.render(screenFactory);
        }
        catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
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
