package com.csci5308.w22.wiseshopping.repository.cart;

import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Adarsh Kannan
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {
    /**
     * this method returns a cart for the given user and status
     * @author : Elizabeth
     * @param user
     * @param status
     * @return Cart
     */
    Cart findByUserAndStatus(User user, String status);

    /**
     * this method returns a cart for the given user, status and shared status
     * @param user1
     * @param status
     * @param sharedStatus
     * @return cart
     * @author : Elizabeth
     */
    Cart findByUserAndSharedStatusAndStatus(User user1, String status, String sharedStatus);
}