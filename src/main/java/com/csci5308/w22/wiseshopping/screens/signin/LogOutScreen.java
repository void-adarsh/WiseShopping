package com.csci5308.w22.wiseshopping.screens.signin;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Elizabeth James
 */
@Component
public class LogOutScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginScreen.class);

    private List<String> validScreens;

    @Autowired
    private Scanner scanner;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;


    private Merchant merchant;

    private User user;

    public LogOutScreen(Scanner scanner, MerchantService merchantService, UserService userService) {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens= new ArrayList<>(Arrays.asList(Constants.REGISTER, Constants.LOGIN, Constants.RESET_PASSWORD));

    }

    /**
     * this logouts the user and is redirected to login screen
     * @param screenFactory
     * @return true on success
     * @author: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        boolean success = false;
        LOGGER.info("***LOGGED OUT****");
        Screen screen = screenFactory.getScreen( Constants.LOGIN);
        success = screen.render(screenFactory);
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
