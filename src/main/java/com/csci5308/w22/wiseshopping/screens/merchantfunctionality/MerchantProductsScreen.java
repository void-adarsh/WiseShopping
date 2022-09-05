package com.csci5308.w22.wiseshopping.screens.merchantfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductCategory;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Elizabeth James
 */
@Component
public class MerchantProductsScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantProductsScreen.class);
    @Autowired
    private Scanner scanner;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private LocationService locationService;
    private ArrayList<String> validScreens;
    private Merchant merchant;
    private User user;

    @Autowired
    public MerchantProductsScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT, Constants.MERCHANT_MENU,Constants.PRODUCTS,Constants.STORE_MENU,Constants.MERCHANT_ANALYTICS));
    }

    /**
     * this provides options to merchant to add and update products in the inventory
     * @param screenFactory
     * @return true, on success
     * @author: Pavithra Gunasekaran
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("**** PRODUCTS SCREEN****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";

            do {
                LOGGER.info("Enter a_product to add products to inventory");
                LOGGER.info("Enter u_product to update products");
                LOGGER.info("Enter exit to exit the merchant products menu");
                input = scan(scanner);
                //add products
                if (input.equalsIgnoreCase("a_product")) {
                        try {
                            success = addProducts();

                        }
                        catch (Exception e){
                            LOGGER.info("Error in adding products to inventory");
                            LOGGER.warn(e.getMessage());
                        }



                }

                // update products
                else if(input.equalsIgnoreCase("u_product")){
                    LOGGER.info("Enter u_price to update product price");
                    LOGGER.info("Enter u_stock to update product stock");
                    LOGGER.info("Enter u_categoryname to update product category name");
                    LOGGER.info("Enter u_categorydesc to update product category description");
                    LOGGER.info("Enter u_tags to update product tags");

                    input = scan(scanner);
                    // update price
                    if(input.equalsIgnoreCase("u_price")){
                        ProductInventory productInventory = updatePrice();
                        success = productInventory!=null;
                    }
                    // update stock
                    else if(input.equalsIgnoreCase("u_stock")){
                        ProductInventory  productInventory = updateProductInventory();
                        success = productInventory!=null;
                    }
                    // update category name
                    else if(input.equalsIgnoreCase("u_categoryname")){
                        ProductCategory productcategory = updateProductCategoryName();
                        success = productcategory!=null;
                    }

                    // update category description
                    else if(input.equalsIgnoreCase("u_categorydesc")){
                        ProductCategory productcategory = updateProductCategoryDescription();
                        success = productcategory!=null;
                    }
                    // update tags
                    else if(input.equalsIgnoreCase("u_tags")){
                        LOGGER.info("Enter a product name");
                        input = scan(scanner);
                        List<Tags> tagsList= productService.getProductTags(input);
                        LOGGER.info("Available tags {}", input);
                        for(Tags eachTag:tagsList){
                            LOGGER.info("Tag ID {} Tag Name {}",eachTag.getTagId(),eachTag.getTagName());
                        }
                        LOGGER.info("Enter the tag id and the tag name to be updated");
                        int tagId= Integer.parseInt(scan(scanner));
                        String updatedTagName = scan(scanner);
                        Tags tagUpdated = productService.updateProductTags(tagId,updatedTagName);
                        LOGGER.info("Tag name updated for Tag{}",tagUpdated.getTagId());
                        success = tagUpdated!=null;
                    }

                }else if (input.equalsIgnoreCase("exit")) {
                        LOGGER.info("Exiting Merchant Products menu...");
                        success = true;
                    } else {
                        LOGGER.warn("Invalid input received...");
                    }
                }
                while (!input.equalsIgnoreCase("exit")) ;
        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (IllegalStateException e){
            LOGGER.warn("No smtp server on this host");
            Screen screen = screenFactory.getScreen(Constants.MERCHANT_PRODUCTS);
            screen.setMerchant(merchant);
            success = screen.render(screenFactory);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.MERCHANT_PRODUCTS);
            screen.setUser(user);
            success = screen.render(screenFactory);
        }

        return success;
    }

    /**
     * @author Pavithra Gunasekaran
     * @return
     */

    private ProductInventory updatePrice() {

        List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant);
        LOGGER.info("****** List of available stores *****");
        for(Store s:storeList){
            LOGGER.info("Store ID {} Store Name {}",s.getId(),s.getName());
        }
        LOGGER.info("Enter the store id");
        int storeId=Integer.parseInt(scan(scanner));
        Store store= storeService.getStoreById(storeId);
        List<ProductInventory> productList = productService.getProductByStore(store);
        LOGGER.info("****** List of available products *****");
        for(ProductInventory p :productList){
            LOGGER.info("Product  ID - {} Product Name - {} Price - {}",p.getProduct().getProductId(),p.getProduct().getProductName(),p.getPrice());
        }
        LOGGER.info("Enter the product id");
        int productId = Integer.parseInt(scan(scanner));
        Product productUpdated = productService.getProductById(productId);
        LOGGER.info("Enter the price to be added ");
        String price = scan(scanner);
        ProductInventory updatedProductPrice = productService.updateProductPrice(productUpdated,store,Float.parseFloat(price));
        LOGGER.info("Price of {} is updated to {}",updatedProductPrice.getProduct().getProductName(),updatedProductPrice.getPrice());
        return  updatedProductPrice;
    }

    /**
     * @author Pavithra Gunasekaran
     * @return
     */

    private ProductInventory updateProductInventory() {

        List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant);
        LOGGER.info("****** List of available stores *****");
        for(Store s:storeList){
            LOGGER.info("Store ID {} Store Name {}",s.getId(),s.getName());
        }
        LOGGER.info("Enter the store id");
        int storeId=Integer.parseInt(scan(scanner));
        Store store= storeService.getStoreById(storeId);
        List<ProductInventory> productList = productService.getProductByStore(store);
        LOGGER.info("****** List of available products *****");
        for(ProductInventory p :productList){
            LOGGER.info("Product  ID - {} Product Name - {} Stock - {}",p.getProduct().getProductId(),p.getProduct().getProductName(),p.getStock());
        }
        LOGGER.info("Enter the product id");
        int productId = Integer.parseInt(scan(scanner));
        Product productUpdated = productService.getProductById(productId);
        LOGGER.info("Enter the stock to be updated ");
        String stock = scan(scanner);
        ProductInventory updatedProductStock = productService.updateProductStock(productUpdated,store, Integer.parseInt(stock));
        LOGGER.info("Stock of {} is updated to {}",updatedProductStock.getProduct().getProductName(),updatedProductStock.getStock());
        return  updatedProductStock;
    }

    private ProductCategory updateProductCategoryDescription() {
        LOGGER.info("Enter the updated categorydesc for the product");
        LOGGER.info("Example : <product_category_id> <description>");
        int productcategoryid = Integer.parseInt(scan(scanner));
        String product_category_name  = scan(scanner);
        ProductCategory productcategory=productService.updateProductCategoryDescription(productcategoryid,product_category_name);
        LOGGER.info("Category description of {} is updated to {}",productcategory.getProductCategoryId(),productcategory.getCategoryDesc());
        return productcategory;
    }

    private ProductCategory updateProductCategoryName() {
        LOGGER.info("Enter the updated categoryname for the product");
        LOGGER.info("Example : <product_category_id> <name>");
        int productcategoryid = Integer.parseInt(scan(scanner));
        String product_category_name  = scan(scanner);
        ProductCategory productcategory=productService.updateProductCategoryName(productcategoryid,product_category_name);
        LOGGER.info("Category name of {} is updated to {}",productcategory.getProductCategoryId(),productcategory.getCategoryName());

        return productcategory;
    }

    private boolean addProducts(){
        LOGGER.info("Enter product_name, store_name, price, stock");
        LOGGER.info("Example : <product_name> <store_name> <price> <stock>");
        String productName = scan(scanner);
        String store_name = scan(scanner);
        float price = Float.parseFloat(scan(scanner));
        int stock = Integer.parseInt(scan(scanner));
        Store store = storeService.getStoreByName(store_name);

        Product product = productService.addProduct(productName, productName);
        Product addedProduct = productService.getProductByName(product.getProductName());
        ProductInventory addProducts = productService.addProductInventory(store, addedProduct, price, stock);

        boolean success = addProducts != null;
        if (success) {
            LOGGER.info("Product {} from store {} is added", addProducts.getProduct().getProductName(), store.getName());
        }
        return success;
    }

    @Override
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }


}
