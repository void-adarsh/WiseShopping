package com.csci5308.w22.wiseshopping.screens.signin;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
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
public class LoginScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginScreen.class);

    private ArrayList<String> validScreens;


    private Scanner scanner;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    private Merchant merchant;

    private User user;


    public LoginScreen(Scanner scanner, MerchantService merchantService, UserService userService) {
        this.scanner = scanner;
        this.merchantService = merchantService;
        this.userService = userService;

        // these denote the list of screens the user can navigate to from the current screen
        validScreens= new ArrayList<>(Arrays.asList(Constants.REGISTER, Constants.RESET_PASSWORD, Constants.LOGIN));


    }

    /**
     * this shows the login screen to the user.
     * On successful login, the user is redirected to the subsequent menu screen
     * On failure, the user is redirected to reset password screen
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     * @contributor: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("***LOGIN SCREEN****");
        LOGGER.info("use : for additional navigation");
        boolean success = false;
        try {
            String input = "";


            do {
                LOGGER.info("Are you a merchant or a user?");
                input = scan(scanner);
                //merchant login
                if (Constants.MERCHANT.equalsIgnoreCase(input)) {
                    LOGGER.info("Enter <username> <password>");
                    String username = scan(scanner);
                    String password = scan(scanner);
                    Merchant merchant = merchantService.loginMerchant(username, password);
                    Screen screen = screenFactory.getScreen(Constants.MERCHANT_MENU);
                    screen.setMerchant(merchant);
                    success = screen.render(screenFactory);
                }
                //user login
                if (Constants.USER.equalsIgnoreCase(input)) {
                    LOGGER.info("Enter <email> <password>");
                    String email = scan(scanner);
                    String password = scan(scanner);
                    User user = userService.loginUser(email, password);
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
            }
        catch (NoSuchUserException e) {
            try {
                LOGGER.warn(e.getMessage());
                LOGGER.info("Forgot Password? Reset password by entering resetPassword" + "\n" +
                        "------------------ OR ------------------ " + "\n" +
                        "Sign up to the application from the Registration screen by entering \"register\"");
                String input;
                input = scan(scanner);
                if ("resetPassword".equalsIgnoreCase(input)) {
                    Screen screen = screenFactory.getScreen(Constants.RESET_PASSWORD);
                    success = screen.render(screenFactory);
                }
                if (Constants.REGISTER.equalsIgnoreCase(input)) {
                    Screen screen = screenFactory.getScreen(Constants.REGISTER);
                    success = screen.render(screenFactory);
                }
            } catch (MenuInterruptedException e1) {
                getNavigations(screenFactory, validScreens, LOGGER, scanner);
            }

        }
        catch (MenuInterruptedException e){
            getNavigations(screenFactory,validScreens,LOGGER,scanner);
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
