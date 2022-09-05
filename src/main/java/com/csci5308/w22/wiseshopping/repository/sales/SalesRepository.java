package com.csci5308.w22.wiseshopping.repository.sales;

//import com.csci5308.w22.wiseshopping.models.Sales;
import com.csci5308.w22.wiseshopping.models.Sales;
//import com.google.common.collect.ArrayListMultimap;
//import com.google.common.collect.Multimap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */


@Repository
public interface SalesRepository extends CrudRepository<Sales, Integer> {
    /**
     * Get the sales data based on the product
     * @param product
     * @return sales
     * @author: Pavithra Gunasekaran
     */
    @Query(value = "SELECT * FROM sales WHERE product = ?1", nativeQuery = true)
   List<Sales> findByProduct(String product);
}
