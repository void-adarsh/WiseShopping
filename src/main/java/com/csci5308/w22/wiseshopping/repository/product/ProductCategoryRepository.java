package com.csci5308.w22.wiseshopping.repository.product;

import com.csci5308.w22.wiseshopping.models.products.ProductCategory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nilesh
 */
@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Integer> {

    ProductCategory findByProductCategoryId(int productCategoryId);

    /**
     * Get the product category based on id
     * @author Pavithra Gunasekaran
     * @param product_id
     * @return
     */
    ProductCategory findById(int product_id);


}
