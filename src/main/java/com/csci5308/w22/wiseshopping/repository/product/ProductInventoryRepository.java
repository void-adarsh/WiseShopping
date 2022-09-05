package com.csci5308.w22.wiseshopping.repository.product;

import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nilesh
 *
 */
@Repository
public interface ProductInventoryRepository extends CrudRepository<ProductInventory, Integer> {


    ProductInventory findByProductAndStore(Product product, Store store);

    /**
     * get the products from inventory based on the
     * @param product
     * @param location
     * @return product inventory
     * @author: Pavithra Gunasekaran
     */
    @Query(value = "SELECT * FROM product_inventory as inv JOIN store as stores JOIN location as locations on inv.store_id=stores.store_id AND stores.location_id=locations.location_id  where inv.product_id = ?1 AND locations.location_name= ?2", nativeQuery = true)
    List<ProductInventory> findByProductId(int product, String location);

    /**
     * get the available products from inventory when the stock is available
     * @return product inventory
     * @author: Pavithra Gunasekaran
     */
    @Query(value = "SELECT * FROM product_inventory where stock >= 1", nativeQuery = true)
    List<ProductInventory> findAvailableProducts();

    /**
     * find specific products from inventory
     * @return product inventory
     * @author: Pavithra Gunasekaran
     */
    List<ProductInventory> findByProduct(Product product);

    /**
     * find product by store
     * @param s
     * @return
     * @author Elizabeth James
     */
    List<ProductInventory> findByStore(Store s);

    @Query(value = "SELECT * from product_inventory where product_id = ?1", nativeQuery = true)
    ProductInventory findByOnlyProductId(int productId);
}
