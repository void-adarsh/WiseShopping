package com.csci5308.w22.wiseshopping.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.user.SubscriptionService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Elizabeth James
 */
@Component
public class SubscriptionScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionScreen.class);

    @Autowired
    private Scanner scanner;

    private ArrayList<String> validScreens;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ProductService productService;

    private Merchant merchant;

    private User user;

    public SubscriptionScreen() {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens = new ArrayList<>(Arrays.asList(Constants.LOGOUT,
                Constants.CART,Constants.SUBSCRIPTIONS,
                Constants.USER_PRODUCTS,Constants.PROFILE,Constants.USER_MENU,Constants.PRICE_ANALYTICS));
    }

    /**
     * this shows the subscription screen to the user.
     * the user can add, view, and delete subscriptions from this screen
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****SUBSCRIPTION SCREEN****");
        LOGGER.info("");


        boolean success = false;
        try {
            String input = "";
            LOGGER.info("Choose one of the following");
            LOGGER.info("Use v_, a_, d_ to add, update or delete followed subscription");
            LOGGER.info("eg: v_subscriptions, a_subscriptions, d_subscriptions, etc.");
            LOGGER.info("Use exit to exit the subscriptions screen");
            do{
            input = scan(scanner);
            // view subs
            if (input.equalsIgnoreCase("v_subscriptions")) {
                List<Subscription> subscriptionList = subscriptionService.getAllSubscriptionsOfUser(user);
                if (subscriptionList.isEmpty()){
                    LOGGER.info("No active subscriptions");
                }
                subscriptionList.stream().forEach( s -> LOGGER.info("Product name: {}, price alert: {}", s.getProduct().getProductName(), s.getPriceAlert()));
                success = true;
            }
            //delete subscriptions
            else if (input.equalsIgnoreCase("d_subscriptions")) {
                List<Subscription> subscriptionList = subscriptionService.getAllSubscriptionsOfUser(user);
                subscriptionList.stream().forEach( s -> LOGGER.info("SubscriptionId: {}, Product name: {}, price alert: {}", s.getId(), s.getProduct().getProductName(), s.getPriceAlert()));
                LOGGER.info("Enter the <id> to be deleted");
                int id = Integer.parseInt(scan(scanner));
                success = subscriptionService.removeSubscription(id);
                LOGGER.info("Subscription deleted");
            }
            // add subscriptions
            else if (input.equalsIgnoreCase("a_subscriptions")) {
                // TODO : what can be done to remove user recall
                LOGGER.info("Enter <product_id> <price_alert>");
                int productId = Integer.parseInt(scan(scanner));
                float priceAlert = Float.parseFloat(scan(scanner));
                Product product = productService.getProductById(productId);
                Subscription subscription = subscriptionService.addSubscription(user, product, priceAlert);
                success = subscription!=null;
            }
            // update subscriptions
            else if (input.equalsIgnoreCase("u_subscriptions")){
                List<Subscription> subscriptionList = subscriptionService.getAllSubscriptionsOfUser(user);
                subscriptionList.stream().forEach( s -> LOGGER.info("SubscriptionId: {}, Product name: {}, price alert: {}", s.getId(), s.getProduct().getProductName(), s.getPriceAlert()));
                LOGGER.info("Enter the <id> <new_price_alert> to be updated");
                int subId = Integer.parseInt(scan(scanner));
                float priceAlert = Float.parseFloat(scan(scanner));
                success = subscriptionService.updateSubscription(subId, priceAlert);
                LOGGER.info("Subscription updated");
            }
            else if (input.equalsIgnoreCase("exit")){
                LOGGER.info("Exiting subscription menu...");
                success= true;
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
            Screen screen = screenFactory.getScreen(Constants.SUBSCRIPTIONS);
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

