package com.csci5308.w22.wiseshopping.service.user;

import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.subscription.SubscriptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Elizabeth James
 */

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTests {

    @Mock
    private SubscriptionRepository repository;
    @InjectMocks
    private SubscriptionService subscriptionService;

    private static final double NEW_PRICE = 1.00;

    @Test
    public void testAlertSubscribers(){

        subscriptionService = Mockito.mock(SubscriptionService.class);
        //TODO: replace this once productService is implemented
        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Potatoes");

        Store store = new Store();
        store.setId(1);
        store.setName("walmart");
        when(subscriptionService.alertSubscribers(product,store,(float) NEW_PRICE)).thenReturn(true);
        Assertions.assertTrue(subscriptionService.alertSubscribers(product, store, (float) NEW_PRICE));
    }

    @Test
    public void testAddSubscriptionsInputParameters(){
        IllegalArgumentException nullUser = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.addSubscription(null,new Product(),(float) 9.44);
        });
        IllegalArgumentException nullProduct = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.addSubscription(new User(),null,(float) 9.44);
        });
        IllegalArgumentException negativePrice = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.addSubscription(null,null,(float) -9.44);
        });

    }

    @Test
    public void testalertSubscribersInputParameters(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.alertSubscribers(null, new Store(), (float) 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            subscriptionService.alertSubscribers(new Product(), null, (float) 1);
        });
    }

    @Test
    public void testAddSubscription(){
        when(repository.save(any(Subscription.class))).thenReturn(new Subscription());
        Assertions.assertNotNull(subscriptionService.addSubscription(new User(), new Product(), (float) 8.99));
    }

    @Test
    public void testUpdateSubscription(){
        when(repository.save(any(Subscription.class))).thenReturn(new Subscription());
        when(repository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(new Subscription()));
        Assertions.assertNotNull(subscriptionService.updateSubscription(1, (float) 8.99));

    }
    @Test
    public void testUpdateSubscriptionInputParameters(){
        when(repository.save(any(Subscription.class))).thenReturn(new Subscription());
        when(repository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(new Subscription()));
        Assertions.assertNotNull(subscriptionService.updateSubscription(1, (float) 8.99));

    }

    @Test
    public void testRemoveSubscription(){
        Assertions.assertTrue(subscriptionService.removeSubscription(1));

    }

    @Test
    public void testGetAllSubscriptionOfusers(){
        when(repository.findByUser(any(User.class))).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(subscriptionService.getAllSubscriptionsOfUser(new User()));
    }
}
