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

import java.sql.SQLException;
import java.util.*;


/**
 * @author Pavithra Gunasekaran
 */
@Component
public class PriceAnalyticsScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceAnalyticsScreen.class);
    @Autowired
    private Scanner scanner;
    @Autowired
    private SalesService salesService;

    private ArrayList<String> validScreens;

    private Merchant merchant;

    private User user;

    @Autowired
    public PriceAnalyticsScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT,Constants.USER_MENU));
    }
    /**
     * this enables the user to enter the product name to view the price analytics based on the past history
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****PRICE ANALYTICS****");
        LOGGER.info("");
        LOGGER.info("Enter a product name to get the price analytics by every month");
        LOGGER.info("Enter exit to return to user analytics screen");
        boolean success = false;
        try {
            String input = "";
            do {

                input = scan(scanner);
                if (!input.equalsIgnoreCase("exit")) {
                    HashMap<Integer, Double> priceAnalytics = salesService.getProductLowestPriceAnalytics(input);
                    boolean chartGenerated = salesService.generateChartForPriceAnalytics(input);

                    for (Map.Entry<Integer, Double> entry : priceAnalytics.entrySet()) {
                        //LOGGER.info(" Month : {} Price : {}",entry.getKey(),entry.getValue());

                    }
                    LOGGER.info("****************************************************************************");
                    LOGGER.info("The graph can be viewed at ./productPriceAnalyticsCharts");
                } else if (input.equalsIgnoreCase("exit")) {
                    LOGGER.info("Exiting Price analytics...");
                    success = true;
                }
            }while(!input.equalsIgnoreCase("exit"));

        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
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
