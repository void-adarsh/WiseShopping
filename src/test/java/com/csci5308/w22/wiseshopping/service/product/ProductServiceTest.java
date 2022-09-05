package com.csci5308.w22.wiseshopping.service.product;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductCategory;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.product.ProductCategoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInventoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductRepository;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.user.SubscriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Elizabeth James
 * @contributor Nilesh
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductCategoryRepository mockedCategoryRepository;

    @Mock
    private ProductInventoryRepository mockedInventoryRepository;

    @Mock
    private TagsRepository mockedTagsRepository;

    @Mock
    private ProductRepository mockedProductRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private ProductService productService;

    Product product;



    /**
     * Unit test to check the updated product stock
     * @author Nilesh
     */
    @Test
    public void testUpdateProductStock(){
        Product product = new Product();
        Store store = new Store();
        ProductInventory inventory = new ProductInventory( store, product, 123, 456);
        when(mockedInventoryRepository.findByProductAndStore(product,store)).thenReturn(inventory);

        ProductInventory updated = productService.updateProductStock(product, store, 8888);

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(8888, updated.getStock());
    }




    /**
     * Unit test to check the updated product stock invalid in store
     * @author Nilesh
     */
    @Test
    public void testUpdateProductStockInvalidProductStore(){
        Product product = new Product();
        Store store = new Store();
        when(mockedInventoryRepository.findByProductAndStore(product,store)).thenReturn(null);

        IllegalArgumentException ex = Assertions.assertThrows( IllegalArgumentException.class,
                () -> {
                    productService.updateProductStock(product, store, 1000);
                }, "Exception not thrown");
        Assertions.assertTrue(ex.getMessage().contains("Could not find inventory with given Product in store"));
    }


    /**
     * Unit test to check the updated product Description
     * @author Nilesh
     */
    @Test
    public void testUpdateProductCategoryDesc(){
        Product product = new Product();
        ProductCategory category = new ProductCategory( product, "Category A", "Category A Desc");
        when(mockedCategoryRepository.findByProductCategoryId(any(Integer.class))).thenReturn(category);

        ProductCategory updated = productService.updateProductCategoryDescription(1, "Category Desc Updated");

        Assertions.assertNotNull(updated);
        Assertions.assertEquals("Category Desc Updated", updated.getCategoryDesc());
    }

    /**
     * Unit test to check the updated categor Description for invalid product
     * @author Nilesh
     */

    @Test
    public void testUpdateProductCategoryDescInvalidProduct(){
        when(mockedCategoryRepository.findByProductCategoryId(any(Integer.class))).thenReturn(null);

        IllegalArgumentException ex = Assertions.assertThrows( IllegalArgumentException.class,
                () -> {
                    productService.updateProductCategoryDescription(1, "Category Desc Updated");
                }, "Exception not thrown");
        Assertions.assertTrue(ex.getMessage().contains("Could not find category with given Id:"));
    }

    /**
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testGetProductStockAvailablity(){
        IllegalArgumentException productNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductStockAvailability("","");
        });
        Assertions.assertEquals("product name cannot be empty or null or blank",productNullException.getMessage());
    }



    @Test
    public void testAddProduct(){
        when(mockedProductRepository.save(any(Product.class))).thenReturn(new Product());
        Assertions.assertNotNull(productService.addProduct("dummy", "dummy"));
    }
    @Test
    public void testGetProductByStore(){
        when(mockedInventoryRepository.findByStore(any(Store.class))).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(productService.getProductByStore(new Store()));
    }

    @Test
    public void testGetAvailableProducts(){
        when(mockedInventoryRepository.findAvailableProducts()).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(productService.getAvailableProducts());
    }

    @Test
    public void testGetProductByID(){
        when(mockedProductRepository.findByProductId(any(Integer.class))).thenReturn(new Product());
        Assertions.assertNotNull(productService.getProductById(1));
    }

    @Test
    public void testRemoveProduct(){
        Product p = new Product();
        p.setProductId(1);
        Assertions.assertTrue(productService.remove(p));
    }

    @Test
    public void testRemoveProductInputParameters(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.remove(null);
        });
    }

    @Test
    public void testAddProductInputParameters(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct("","");
        });
    }




}
