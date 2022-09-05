package com.csci5308.w22.wiseshopping.screens.analytics;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.sales.SalesService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @author Pavithra Gunasekaran
 */
@Component
public class MerchantAnalyticsScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantAnalyticsScreen.class);
    @Autowired
    private Scanner scanner;
    @Autowired
    private SalesService salesService;

    private ArrayList<String> validScreens;

    private Merchant merchant;

    private User user;

     public MerchantAnalyticsScreen() {
         // these denote the list of screens the user can navigate to from the current scree
         validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT,Constants.USER_ANALYTICS,Constants.MERCHANT_MENU));
    }
    /**
     * this enables the merchant to enter the type of analytics to be viewed
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****ANALYTICS****");
        LOGGER.info("");
        LOGGER.info("Enter demand_trend to view the demand trend of a product");
        boolean success = false;
        try {
        String input = "";
        input = scan(scanner);

            if((input.equalsIgnoreCase(Constants.DEMAND_TREND_ANALYTICS))) {
                Screen screen = screenFactory.getScreen(Constants.DEMAND_TREND_ANALYTICS);
                screen.setMerchant(merchant);
                success = screen.render(screenFactory);
            }

        } catch (MenuInterruptedException e) {
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
