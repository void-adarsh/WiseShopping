package com.csci5308.w22.wiseshopping.integrationTests.service.product;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.vendor.StoreRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductCategoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInventoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductRepository;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class ProductServiceTests {
    @Autowired
    ProductService productService;

    @Autowired
    MerchantService merchantService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private StoreService storeService;

    private Product product;
    private Merchant merchant;
    private Location location;

    @BeforeEach
    public void setUp() {
        product = new Product("Wired Headphones");
        merchant = new Merchant("John Doe", "johndoe@xyz.com", "password123");
        location = locationService.addLocation("testLocation", "testZIp", "testProvince", "testCountry");
    }

    @AfterEach
    public void cleanUp() {
        merchantService.removeMerchant(merchant.getEmail());
        locationService.remove(location);
    }


    /**
     * @author nilesh
     * Integration Test Get product avaialability
     * @throws SQLException
     */
    @Test
    public void testGetProductStockAvailability() throws SQLException {
        List<ProductInventory> productList = productService.getProductStockAvailability("milk", "halifax");
        Assertions.assertTrue(productList.size() > 0);

    }

    /**
     * Integration Test Get product Price
     * @author Nilesh
     */
    @Test
    public void testUpdateProductPrice() {
        Product p = productRepository.findByProductId(1);
        Store store = storeRepository.findById(1);
        if (store == null) {
            store = storeService.addStore("Timbuktu", "private", "11", "12", "John Doe", merchant, location);
        }

        productService.updateProductPrice(p, store, 1000);
        Assertions.assertEquals(1000, productInventoryRepository.findByProductAndStore(p, store).getPrice());
    }

    /**
     * Integration Test update product avaialability
     * @author Nilesh
     */
    @Test
    public void testUpdateProductStock() {
        Product p = productRepository.findByProductId(1);
        Store store = storeRepository.findById(1);
        if (store == null) {
            store = storeService.addStore("Timbuktu", "private", "11", "12", "John Doe", merchant, location);
        }
        productService.updateProductStock(p, store, 4);
        Assertions.assertEquals(4, productInventoryRepository.findByProductAndStore(p, store).getStock());


    }

    /**
     * Integration test for updating product tags
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testUpdateTags(){
       // Tags tagUpdated ;
        Tags tagUpdated = productService.updateProductTags(1,"updated_tag");
        Assertions.assertEquals(tagUpdated.getTagName(),"updated_tag");
    }

    /**
     * Integration Test Get updating product Category name
     * @author Nilesh
     */
    @Test
    public void testUpdateProductCategoryName() {
        productService.updateProductCategoryName(1, "updated_category");
        Assertions.assertEquals("updated_category", productCategoryRepository.findByProductCategoryId(1).getCategoryName());


    }

    /**
     * Integration Test for updating product Description
     * @author Nilesh
     */
    @Test
    public void testUpdateProductCategoryDesc() {
        productService.updateProductCategoryDescription(1, "updated_description");
        Assertions.assertEquals("updated_description", productCategoryRepository.findByProductCategoryId(1).getCategoryDesc());


    }

    /**
     * Integration test for adding products
     * @author Pavithra Gunasekaran
     *
     */

    @Test
    public void testAddProduct(){
        Product expectedProduct  = new Product("test","test");
        Product actualProduct = productService.addProduct("test","test");
        expectedProduct.setProductId(actualProduct.getProductId());
        Assertions.assertEquals(expectedProduct.getProductName(), actualProduct.getProductName());
        productService.remove(actualProduct);

    }

    /**
     * Integration test for adding products to inventory
     * @author Pavithra Gunasekaran
     *
     */
    @Test
    public void testAddProductInventory(){
        Product product = productService.addProduct("test","test");

        Merchant merchant = merchantService.registerMerchant("test", "test@test.com", "test","test");
        Location location =  locationService.addLocation("test","test","test","test");
        Store store=storeService.addStore("test","test","8","23","test",merchant,location);


        ProductInventory expectedProductInventory = new ProductInventory(store, product,10,2);
        ProductInventory actualProductInventory = productService.addProductInventory(store,product,10,2);
        Assertions.assertEquals(actualProductInventory.getProduct().getProductName(),expectedProductInventory.getProduct().getProductName());

        productService.removeProductInventory(actualProductInventory);
        storeService.remove(store);
        locationService.remove(location);
        merchantService.removeMerchant(merchant.getEmail());

        productService.remove(product);
    }

    /**
     * Integration test for comparing the product price between sellers
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testCompareProductPrice() {
        HashMap<String,Float> compareProductPrice = productService.compareProductPrice("milk");
        Assertions.assertTrue(compareProductPrice.size()>0);
    }

}
