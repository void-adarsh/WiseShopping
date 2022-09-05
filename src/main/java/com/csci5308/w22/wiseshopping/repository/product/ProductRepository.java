package com.csci5308.w22.wiseshopping.repository.product;

import com.csci5308.w22.wiseshopping.models.products.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
@Repository
public interface ProductRepository extends CrudRepository<Product,Integer> {

    Product findByProductId(int productId);
    //List<Product> findByProductNameList(String product_name);
    /**
     * Get the products based on the product id
     * @param product_name
     * @return product
     * @author: Pavithra Gunasekaran
     */
    Product findByProductName(String product_name);
}
