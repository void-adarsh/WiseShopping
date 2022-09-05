package com.csci5308.w22.wiseshopping.service.products;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductCategory;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.product.ProductCategoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInventoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductRepository;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import com.csci5308.w22.wiseshopping.service.user.SubscriptionService;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TagsRepository tagsRepository;


    /**
     * @author Harsh Hariramani
     * Merchant can add products with their name and description
     * @param name
     * @param description
     * @return
     */
    public Product addProduct (String name, String description) {

        if (!Util.isValidString(name)){
            throw new IllegalArgumentException("product name cannot be null or empty or blank");
        }

        Product product = new Product(name,description);
        // Calling the object of the product table to add the product
        productRepository.save(product);
        LOGGER.info("Product {} with id {} added ",product.getProductName(), product.getProductId());

        return product;
    }

    /**
     * @author Pavithra Gunasekaran
     * @contributor Harsh Hariramani
     * Enables the merchant to add products to the inventory
     * @param store
     * @param product
     * @param price
     * @param stock
     * @return ProductInventory
     */

    public ProductInventory addProductInventory(Store store, Product product, float price , int stock){
        if(store == null){
            throw new IllegalArgumentException("store cannot be null");
        }
        if(product == null){
            throw new IllegalArgumentException("product cannot be null");
        }
        ProductInventory productInventory = new ProductInventory(store, product, price, stock);
        productInventoryRepository.save(productInventory);
        return  productInventory;
    }

    /**
     * Merchant can update  the Product Price for particular product
     * @param product
     * @param store
     * @param price
     * @author nilesh
     * @contributor Pavithra Gunasekaran
     * @return
     */
    @Transactional
    public ProductInventory updateProductPrice(Product product, Store store, float price) {

        ProductInventory productInventory = productInventoryRepository.findByProductAndStore(product, store);
        //Check to see if product exist in store
        if (productInventory == null) {
            throw new IllegalArgumentException("Could not find inventory with given Product in store");
        }

        productInventory.setPrice(price);
        LOGGER.info("updated price : {}",productInventory.getPrice());
        productInventoryRepository.save(productInventory);
        subscriptionService.alertSubscribers(product, store, price);
        return productInventory;
    }

    /**
     * Merchant can update the Product stock for particular product
     * @param product
     * @param store
     * @param stock
     * @author nilesh
     * @contributor Pavithra Gunasekaran
     * @return
     */
    @Transactional
    public ProductInventory updateProductStock(Product product, Store store, int stock) {
        ProductInventory productInventory = productInventoryRepository.findByProductAndStore(product, store);

        //Check to see if product exist in store
        if (productInventory == null) {
            throw new IllegalArgumentException("Could not find inventory with given Product in store:");
        }

        productInventory.setStock(stock);
        productInventoryRepository.save(productInventory);
        return productInventory;
    }

    /**
     * Merchant can update  the Product Categoryname for particular product
     * @param productCategoryId
     * @param name
     * @author nilesh
     * @return
     */
    @Transactional
    public ProductCategory updateProductCategoryName(int productCategoryId, String name) {
        ProductCategory category = productCategoryRepository.findByProductCategoryId(productCategoryId);

        //Check to see if product category exist for that particular product
        if (category == null) {
            throw new IllegalArgumentException("Could not find category with given Id: " + productCategoryId);
        }

        category.setCategoryName(name);
        productCategoryRepository.save(category);
        return category;
    }

    /**
     * Merchant can update  the Product Category description for particular product
     * @param productCategoryId
     * @param description
     * @author nilesh
     * @return
     */
    @Transactional
    public ProductCategory updateProductCategoryDescription(int productCategoryId, String description) {
        ProductCategory category = productCategoryRepository.findByProductCategoryId(productCategoryId);

        if (category == null) {
            throw new IllegalArgumentException("Could not find category with given Id: " + productCategoryId);
        }

        category.setCategoryDesc(description);
        productCategoryRepository.save(category);
        return category;
    }

    /**
     * @author Pavithra Gunasekaran
     * Get the available stock for a product in a given location
     * @param product_name
     * @param location
     * @return
     */
    @Transactional
    public List<ProductInventory> getProductStockAvailability(String product_name,String location){
        if(product_name.equalsIgnoreCase("")){
            throw  new IllegalArgumentException("product name cannot be empty or null or blank");
        }
        Product productIDList= productRepository.findByProductName(product_name);
        int productId = productIDList.getProductId();
        List<ProductInventory> productInventoryList=productInventoryRepository.findByProductId(productId,location);
        return  productInventoryList;
    }

    /**
     * Merchant can get Product tags
     * @param tagName
     * @author Nilesh
     * @return
     */
    @Transactional
    public List<Tags> getProductTags(String tagName){
        List<Tags> productTagList = tagsRepository.findByTagName(tagName);
        return productTagList;

    }

    //UpdateTags
    /**
     * @author Pavithra Gunasekaran
     * @contributor Nilesh Gupta
     * @param tagId
     * @param name
     * @return
     */
    @Transactional
    public Tags updateProductTags(int tagId, String name) {
        Tags tag = tagsRepository.findById(tagId);
        tag.setTagName(name);
        tagsRepository.save(tag);
        return tag;
    }

    /**
     * Compare the product price in different stores
     * @author Pavithra Gunasekaran
     * @param productName
     * @return
     */

    @Transactional
    public HashMap<String, Float> compareProductPrice(String productName){
        Product products = getProductListByName(productName);
        HashMap<String,Float> listProductPrice = new HashMap<String, Float>();
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProduct(products);
        for(ProductInventory pi:productInventoryList){
            listProductPrice.put(pi.getStore().getName(),pi.getPrice());
        }


        // Create a list from the elements of HashMap
        List<Map.Entry<String, Float> > list
                = new LinkedList<Map.Entry<String, Float> >(
                listProductPrice.entrySet());

        // Sort the list using lambda expression
        Collections.sort(
                list,
                (i1,
                 i2) -> i1.getValue().compareTo(i2.getValue()));

        // put data from sorted list to hashmap
        HashMap<String, Float> compareProductPrice
                = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            compareProductPrice.put(aa.getKey(), aa.getValue());
        }

        return compareProductPrice;
    }

    /**
     * get the list of products that have stock >= 1
     * @return list of prod inventory
     * @author: Elizabeth James
     */
    public List<ProductInventory> getAvailableProducts() {
        return productInventoryRepository.findAvailableProducts();
    }

    /**
     * get a specific product for the given product id
     * @return product
     * @author: Pavithra Gunasekaran
     */
    public Product getProductById(int productId) {
        return productRepository.findByProductId(productId);
    }

    /**
     * get a specific product for the given product name
     * @return product
     * @author: Pavithra Gunasekaran
     */
    public Product getProductListByName(String productName) { return productRepository.findByProductName(productName);}

    /**
     * get a specific product for the given product name
     * @return product
     * @author: Pavithra Gunasekaran
     */
    public Product getProductByName(String productName) { return productRepository.findByProductName(productName);}

    /**
     * get the list of product inventory for a specific product
     * @return list of prod inventory
     * @author: Pavithra Gunasekaran
     */
    public List<ProductInventory> getProductInventory(Product product){ return  productInventoryRepository.findByProduct(product);}

    /**
     * delete the given product
     * @return true if the data is deleted
     * @author: Pavithra Gunasekaran
     */
    @Transactional
    public boolean remove(Product product) {
        if (product == null){
            throw new IllegalArgumentException("product cannot be null");
        }
        productRepository.delete(product);
        return true;
    }
    /**
     * delete the given product inventory
     * @return true if the data is deleted
     * @author: Pavithra Gunasekaran
     */
    @Transactional
    public boolean removeProductInventory(ProductInventory productInventory) {
        if (productInventory == null){
            throw new IllegalArgumentException("productInventory cannot be null");
        }
        productInventoryRepository.delete(productInventory);
        return true;
    }


    /**
     * get products by their store
     * @param s
     * @return
     * @author: Elizabeth James
     */
    public List<ProductInventory> getProductByStore(Store s) {
        return productInventoryRepository.findByStore(s);
    }

    public List<Product> getAllProducts() {
        return (List<Product>) productRepository.findAll();
    }
}



