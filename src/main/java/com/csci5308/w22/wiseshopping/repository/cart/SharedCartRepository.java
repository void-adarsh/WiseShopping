package com.csci5308.w22.wiseshopping.repository.cart;

import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.cart.SharedCart;
import com.csci5308.w22.wiseshopping.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Adarsh Kannan
 */
@Repository
public interface SharedCartRepository extends CrudRepository<SharedCart, Integer> {

    /**
     * get all the users that shared the cart
     * @author: Elizabeth James
     * @param cartID
     * @return list of user ids
     */
    @Query(value = "select user_id from cart_shared where cart_id = ?1", nativeQuery = true)
    List<Integer> findSharedCartsByCart(int cartID);

    /**
     * get the shared cart object by the given cart and user
     * @param cart
     * @param user
     * @return shared cart
     * @author: Elizabeth James
     */
    SharedCart findSharedCartByCartAndUser(Cart cart, User user);
}