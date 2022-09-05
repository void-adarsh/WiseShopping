package com.csci5308.w22.wiseshopping.service.user;

import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.utils.MailNotifier;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.repository.subscription.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Elizabeth James
 */
@Service
public class SubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantService.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MailNotifier mailNotifier;

    /**
     * this method gets the subscription details from the user and add it to the table
     * @param user
     * @param product
     * @param priceAlert
     * @return subscription
     * @author: Elizabeth James
     */
    public Subscription addSubscription(User user, Product product, float priceAlert){
        //input validation
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        if(priceAlert < 0){
            throw new IllegalArgumentException("price alert amount cannot be less than 0");
        }

        // get the subscription from DB
        Subscription subscription = subscriptionRepository.findByUserAndProduct(user, product);

        // if no subscription is existing in DB, then create a new subscription; else update the existing one
        if (subscription == null){
            subscription = new Subscription(product, user);
        }
        subscription.setPriceAlert(priceAlert);
        subscriptionRepository.save(subscription);
        LOGGER.info("Subscription with user: {}, productId: {}, priceAlert: {}  saved", subscription.getUser().getEmail(), subscription.getProduct().getProductName(), subscription.getPriceAlert());
        return subscription;
    }

    /**
     * this method sends an alert to the subscribers when the price of a product in a store is les than equal to the price alert they set up
     * @param product
     * @param store
     * @param price
     * @return true
     * @author: Elizabeth James
     */
    public boolean alertSubscribers(Product product, Store store, float price) {
        boolean success = false;

        // validate inputs
        if(product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (store == null){
            throw new IllegalArgumentException("Store cannot be null");
        }
        if(price < 0){
            throw new IllegalArgumentException("price amount cannot be less than 0");
        }

        // get all the price alerts set up on the given product
        List<Subscription> subscriptionList = subscriptionRepository.findByProductId(product.getProductId());

        // get he lis of user for whom the price alert set by user >= the product price
        // this means that product price has gone down and the user would like to be notified
        List<User> usersToBeAlerted = new ArrayList<>();
        for (Subscription subscription : subscriptionList){
            if (subscription.getPriceAlert() >= price){
                usersToBeAlerted.add(subscription.getUser());
            }
        }

        // notify the users
        success = mailNotifier.sendPriceAlerts(usersToBeAlerted, product.getProductName(), store.getName(), price);
        return success;
    }

    /**
     * this method gets all the subscriptions of a user
     * @param user
     * @return list of subs
     * @author: Elizabeth James
     */
    public List<Subscription> getAllSubscriptionsOfUser(User user) {
        return subscriptionRepository.findByUser(user);
    }

    /**
     * this deletes a subscription from the DB. if the id is not existing, then the state of the system is same as the id being deleted
     * @param id
     * @return true
     * @author: Elizabeth James
     */
    public boolean removeSubscription(int id) {
        // if the id is not existing, then the state of the system is same as the id being deleted
        // hence return true;
        subscriptionRepository.deleteById(id);
        return true;
    }

    /**
     * this method updates the subscriptions
     * @param subId
     * @param priceAlert
     * @return true, if success
     * @author: Elizabeth James
     */
    public boolean updateSubscription(int subId, float priceAlert) {
        Subscription subscription = subscriptionRepository.findById(subId).orElse(null);
        if (subscription!=null){
            subscription.setPriceAlert(priceAlert);
            subscriptionRepository.save(subscription);
            return true;
        }
        else {
            LOGGER.error("No such subscription id");
            return false;
        }

    }
}

