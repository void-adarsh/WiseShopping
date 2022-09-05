package com.csci5308.w22.wiseshopping.screens.merchantfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.sales.SalesService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Elizabeth James
 */
@Component
public class MerchantMenuScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantMenuScreen.class);

    @Autowired
    private Scanner scanner;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private SalesService salesService;

    @Autowired
    private LocationService locationService;
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;
    private List<String> validScreens;


    private Merchant merchant;

    private User user;


    @Autowired
    public MerchantMenuScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT, Constants.MERCHANT_MENU,Constants.PRODUCTS,Constants.STORE_MENU,Constants.MERCHANT_ANALYTICS));
    }

    /**
     * this shows the user menu screen to the user.
     * the user can further move to the products, store, and analytics screen
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     * @contributor: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****MERCHANT MENU****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";

            do {
                LOGGER.info("Choose one of the following");
                LOGGER.info("products, store, analytics");
                input = scan(scanner);

                // go to products screen
                if (input.equalsIgnoreCase(Constants.PRODUCTS)){
                    Screen screen = screenFactory.getScreen(Constants.MERCHANT_PRODUCTS);
                    screen.setMerchant(merchant);
                    success = screen.render(screenFactory);
                }

                // go to products screen
                else if ( input.equalsIgnoreCase(Constants.STORE_MENU)){
                    Screen screen = screenFactory.getScreen(Constants.STORE_MENU);
                    screen.setMerchant(merchant);
                    success = screen.render(screenFactory);
                }

                // go to products screen
                else if(input.equalsIgnoreCase("analytics")){
                    Screen screen = screenFactory.getScreen(Constants.MERCHANT_ANALYTICS);
                    screen.setMerchant(merchant);
                    success = screen.render(screenFactory);
                }
                else if (input.equalsIgnoreCase("exit")){
                    LOGGER.info("Exiting merchant menu...");
                    success = true;
                }
                else {
                    LOGGER.warn("Invalid input received...");
                }
            } while (!input.equalsIgnoreCase("exit"));
        }
        catch(MenuInterruptedException e){
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
