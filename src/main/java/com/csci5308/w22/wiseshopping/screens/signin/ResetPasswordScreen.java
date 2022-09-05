package com.csci5308.w22.wiseshopping.screens.signin;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
import com.csci5308.w22.wiseshopping.exceptions.user.UserAlreadyRegisteredException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Pavithra Gunasekaran
 */
@Component
public class ResetPasswordScreen implements Screen{
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordScreen.class);
    @Autowired
    private Scanner scanner;

    @Autowired
    private UserService userService;
    @Autowired
    private MerchantService merchantService;
    private ArrayList<String> validScreens;

    private Merchant merchant;

    private User user;

    public ResetPasswordScreen() {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens= new ArrayList<>(Arrays.asList(Constants.REGISTER, Constants.LOGIN, Constants.RESET_PASSWORD, Constants.LOGOUT));

    }

    /**
     * this shows the reset password screen to the user.
     * On successful reset, the user is redirected to the login screen
     * On failure, the user is redirected to this screen again
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     * @contributor: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("***RESET PASSWORD SCREEN****");
        boolean success = false;
        try {
            String input = "";

            do {
                LOGGER.info("Are you a merchant or a user?");
                input = scan(scanner);
                // user reset password
                if (Constants.USER.equalsIgnoreCase(input)) {
                    LOGGER.info("***********************************************************************");
                    LOGGER.info("Forgot Password? Reset password by entering your <email id> <security code> <new password>");
                    String email = scan(scanner);
                    String securityCode = scan(scanner);
                    String newPassword = scan(scanner);
                    securityCode = Util.encode(securityCode);
                    User user = userService.resetPassword(email, securityCode, newPassword);
                    if (user.getSecurity_code().equalsIgnoreCase(securityCode)) {
                        Screen screen = screenFactory.getScreen(Constants.LOGIN);
                        screen.setUser(user);
                        success = screen.render(screenFactory);
                    } else {
                        LOGGER.warn("Invalid credentials/security code entered");
                        Screen screen = screenFactory.getScreen(Constants.RESET_PASSWORD);
                        success = screen.render(screenFactory);
                    }
                }
                //merchant reset password
                if (Constants.MERCHANT.equalsIgnoreCase(input)) {
                    LOGGER.info("***********************************************************************");
                    LOGGER.info("Forgot Password? Reset password by entering your <email id> <security code> <new password>");
                    String email = scan(scanner);
                    String securityCode = scan(scanner);
                    String newPassword = scan(scanner);
                    securityCode = Util.encode(securityCode);
                    Merchant merchant = merchantService.resetMerchantPassword(email, securityCode, newPassword);
                    if (merchant.getSecurity_code().equalsIgnoreCase(securityCode)) {
                        Screen screen = screenFactory.getScreen(Constants.LOGIN);
                        screen.setMerchant(merchant);
                        success = screen.render(screenFactory);
                    } else {
                        LOGGER.warn("Invalid credentials/security code entered");
                        Screen screen = screenFactory.getScreen(Constants.RESET_PASSWORD);
                        success = screen.render(screenFactory);
                    }
                } else if (input.equalsIgnoreCase("exit")) {
                    success = true;
                    Screen screen = screenFactory.getScreen(Constants.LOGIN);
                    success = screen.render(screenFactory);
                } else {
                    LOGGER.warn("Invalid input received...");
                }
            }
            while (!input.equalsIgnoreCase("exit"));
        } catch (NoSuchUserException e) {
            LOGGER.warn(e.getMessage());
            Screen screen = screenFactory.getScreen(Constants.RESET_PASSWORD);
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
