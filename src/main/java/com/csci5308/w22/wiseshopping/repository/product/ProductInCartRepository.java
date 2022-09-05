package com.csci5308.w22.wiseshopping.repository.product;

import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInCart;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
@Repository
public interface ProductInCartRepository extends CrudRepository<ProductInCart,Integer> {

    @Query(value="delete from product_in_cart where product_name = ?1",nativeQuery = true)
    Integer deleteByProductName(String productName);

    /**
     * get the productInCart object depending upon the store and product and cart
     * @param cart
     * @param prod
     * @param store
     * @return product in cart
     * @author: Elizabeth James
     */
    ProductInCart findByCartAndProductAndStore(Cart cart, Product prod, Store store);

    /**
     * find the DB entry on basis of cart
     * @param cart
     * @return product in cart
     * @author: Elizabeth James
     */
    List<ProductInCart> findByCart(Cart cart);
}



