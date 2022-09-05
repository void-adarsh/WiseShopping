package com.csci5308.w22.wiseshopping.repository.subscription;

import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.Subscription;
import com.csci5308.w22.wiseshopping.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Elizabeth James
 */
@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    /**
     * find subscription given a user and product
     * @param user
     * @param product
     * @return subscription
     * @author: Elizabeth James
     */
    Subscription findByUserAndProduct(User user, Product product);

    /**
     * get the list of subscriptions for a given product
     * @param productId
     * @return list of subscriptions
     * @author: Elizabeth James
     */
    @Query(value = "SELECT * FROM subscription where product_id = ?1",nativeQuery = true)
    List<Subscription> findByProductId (int productId);

    /**
     * find the list of subscriptions for a user
     * @param user
     * @return list of subscriptions
     * @author: Elizabeth James
     */
    List<Subscription> findByUser(User user);
}

