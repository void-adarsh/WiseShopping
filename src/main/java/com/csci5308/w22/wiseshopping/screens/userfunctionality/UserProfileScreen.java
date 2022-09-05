package com.csci5308.w22.wiseshopping.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Elizabeth James
 * @contributor Nilesh
 * @contributor Nilesh
 */

@Component
public class UserProfileScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileScreen.class);

    @Autowired
    private Scanner scanner;

    @Autowired
    private UserService userService;

    private List<String> validScreens;

    private Merchant merchant;

    private User user;

    public UserProfileScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens = new ArrayList<>(Arrays.asList(Constants.LOGOUT,
                Constants.CART,Constants.SUBSCRIPTIONS,
                Constants.USER_PRODUCTS,Constants.PROFILE,Constants.USER_MENU,Constants.PRICE_ANALYTICS));
    }

    /**
     * this shows the subcription screen to the user.
     * the user can view and update profile
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {

        LOGGER.info("****USER PROFILE SCREEN****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";
            do{
                LOGGER.info("Choose one of the following. Press exit to exit menu");
                LOGGER.info("Use v_, u_, to view or update followed by profile");
                LOGGER.info("eg: v_profile, u_profile");
                input = scan(scanner);

                // update profile
                if (input.equalsIgnoreCase("u_profile")) {
                    LOGGER.info("Your profile:");
                    LOGGER.info("First Name: {}, Last Name: {}, emailId: {}, Contact Number: {}",user.getFirstName(),
                            user.getLastName(), user.getEmail(), user.getContact() );
                    LOGGER.info("");
                    Map<String,String> mapAttributes = new HashMap<>();
                    LOGGER.info("Pass the attributes to be updated. Enter in <key>:<value> format");
                    LOGGER.info("Acceptable keys are : firstName, lastName, contact");
                    LOGGER.info("Enter done when updation is done");
                    String keyValuePair = scan(scanner);
                    do {

                        try {
                            String[] pair = keyValuePair.split(":");
                            mapAttributes.put(pair[0], pair[1]);

                        }
                        catch (Exception e){
                            LOGGER.error("Invalid key value pair: {}", keyValuePair);
                        }
                        finally {
                            keyValuePair = scan(scanner);
                        }
                    }while (!keyValuePair.equalsIgnoreCase("done"));
                    User user1 = userService.updateUserDetails(user.getEmail(),mapAttributes);
                    success = user1!=null;

                }

                // view profile
                else if (input.equalsIgnoreCase("v_profile")){
                    user = userService.getUserByEmail(user.getEmail());
                    LOGGER.info("Name: {} {}, email : {}, contact: {}",user.getFirstName(), user.getLastName(), user.getEmail(), user.getContact());
                }
                else if (input.equalsIgnoreCase("exit")){
                    LOGGER.info("Exiting user profile menu...");
                    success = true;
                }
                else {
                    LOGGER.warn("Invalid input received...");
                }
            } while (!input.equalsIgnoreCase("exit"));


        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.PROFILE);
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

