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
public class DemandTrendAnalyticsScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemandTrendAnalyticsScreen.class);

    @Autowired
    private Scanner scanner;
    @Autowired
    private SalesService salesService;

    private ArrayList<String> validScreens;

    private Merchant merchant;

    private User user;

    @Autowired
    public DemandTrendAnalyticsScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT));
    }
    /**
     * this generated the product demand trend analytics to the merchant and dispalys the chart path
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****PRODUCT DEMAND TREND ANALYTICS****");
        LOGGER.info("");
        boolean success = false;
        try {
            salesService.generateChartForAllProducts();
            LOGGER.info("****************************************************************************");
            LOGGER.info("Product demand charts are generated in ./productDemandCharts/ folder");
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
