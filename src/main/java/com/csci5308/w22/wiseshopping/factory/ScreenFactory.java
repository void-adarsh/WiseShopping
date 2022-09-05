package com.csci5308.w22.wiseshopping.factory;

import com.csci5308.w22.wiseshopping.exceptions.screen.InvalidScreenException;
import com.csci5308.w22.wiseshopping.screens.*;
import com.csci5308.w22.wiseshopping.screens.analytics.DemandTrendAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.analytics.MerchantAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.analytics.PriceAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantProductsScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.StoreScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LogOutScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LoginScreen;
import com.csci5308.w22.wiseshopping.screens.signin.RegistrationScreen;
import com.csci5308.w22.wiseshopping.screens.signin.ResetPasswordScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.*;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Elizabeth James
 */
@Service
public class ScreenFactory {

    @Autowired
    private LoginScreen loginScreen;
    @Autowired
    private RegistrationScreen registrationScreen;
    @Autowired
    private StoreScreen storeScreen;

    @Autowired
    private LogOutScreen logOutScreen;

    @Autowired
    private UserProductsScreen userProductsScreen;


    @Autowired
    private MerchantProductsScreen merchantProductsScreen;

    @Autowired
    private SubscriptionScreen subscriptionScreen;

    @Autowired
    private UserMenuScreen userMenuScreen;


    @Autowired
    private MerchantMenuScreen merchantMenu;


    @Autowired
    private PriceAnalyticsScreen priceAnalyticsScreen;

    @Autowired
    private ResetPasswordScreen resetPasswordScreen;

    @Autowired
    private CartScreen cartScreen;


    @Autowired
    private MerchantAnalyticsScreen merchantAnalyticsScreen;

    @Autowired
    private DemandTrendAnalyticsScreen demandTrendAnalyticsScreen;

    @Autowired
    private UserProfileScreen userProfileScreen;


    /**
     * this method returns an instance of the screen based on the input
     * @author: Elizabeth James
     * @param screen
     * @return
     */
    public Screen getScreen(String screen){
        switch (screen){
            case Constants.LOGIN: return loginScreen;
            case Constants.REGISTER: return registrationScreen;
            case Constants.STORE_MENU: return storeScreen;
            case Constants.USER_MENU: return userMenuScreen;
            case Constants.MERCHANT_MENU: return merchantMenu;
            case Constants.LOGOUT: return logOutScreen;
            case Constants.USER_PRODUCTS: return userProductsScreen;
            case Constants.SUBSCRIPTIONS: return subscriptionScreen;
            case Constants.PRICE_ANALYTICS: return priceAnalyticsScreen;
            case Constants.RESET_PASSWORD: return resetPasswordScreen;
            case Constants.CART: return cartScreen;
            case Constants.DEMAND_TREND_ANALYTICS: return demandTrendAnalyticsScreen;
            case Constants.MERCHANT_ANALYTICS: return merchantAnalyticsScreen;
            case Constants.PROFILE: return userProfileScreen;
            case Constants.MERCHANT_PRODUCTS: return merchantProductsScreen;
            default: throw new InvalidScreenException("No such screen");
        }
    }


}
