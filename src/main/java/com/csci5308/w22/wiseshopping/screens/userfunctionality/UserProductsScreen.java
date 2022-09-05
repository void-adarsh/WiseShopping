package com.csci5308.w22.wiseshopping.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.products.TagsService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Elizabeth James
 * @contributor Nilesh
 */
@Component
public class UserProductsScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProductsScreen.class);
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
    private TagsService tagsService;

    public UserProductsScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens = new ArrayList<>(Arrays.asList(Constants.LOGOUT,
                Constants.CART,Constants.SUBSCRIPTIONS,
                Constants.USER_PRODUCTS,Constants.PROFILE,Constants.USER_MENU,Constants.PRICE_ANALYTICS));
    }

    /**
     * this shows the subcription screen to the user.
     * the user can search products, filter products, compare prices of product, and add products to cart
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     * @contributor: Pavithra Gunasekaran
     * @contributor:Nilesh
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****PRODUCTS SCREEN****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";

            do{
            LOGGER.info("To add product, enter a_product. ");
            LOGGER.info("To filter products, enter f_zipcode, f_seller, f_zipcode_seller ");
            LOGGER.info("To search by tags , enter s_tags");
            LOGGER.info("To view all products, enter v_product. ");
            LOGGER.info("To toggle product availability based on location, enter t_product. ");
            LOGGER.info("To compare the product prices, enter c_product");
            LOGGER.info("Enter \"exit\" to exit the product menu");
            input = scan(scanner);

            // compare products
            if(input.equalsIgnoreCase("c_product")){
                LOGGER.info("Enter a product name to compare price");
                List<Product> productList = productService.getAllProducts();
                String allproductNames = "";
                if (!productList.isEmpty()){
                    for (Product p : productList){
                        allproductNames+=p.getProductName() +", ";
                    }
                    allproductNames = allproductNames.substring(0, allproductNames.length() -2);
                }
                LOGGER.info("Possible names that you can enter :  {}",allproductNames);
                input = scan(scanner);
                HashMap<String,Float> comparePrice = productService.compareProductPrice(input);
                for( Map.Entry<String, Float> entry : comparePrice.entrySet() ){
                    LOGGER.info( entry.getKey() + " => " + entry.getValue() );
                }
            }
            //add products to cart
            if (input.equalsIgnoreCase("a_product")){
                LOGGER.info("To add product to cart, enter <id> <quantity>");
                LOGGER.info("Enter \"done\" when finished adding products to cart");
                input = scan(scanner);
                do{
                    try {
                        int id = Integer.parseInt(input);
                        int quantity = Integer.parseInt(scan(scanner));
                        Product product = productService.getProductById(id);
                        Cart cart = cartService.getCartByUserAndStatus(user, Constants.INPROGRESS);
                        if (cart == null) {
                            cart = cartService.addCart(user, Constants.NOTSHARED);
                        }
                        cartService.addProductInCart(cart, product, quantity);
                        input = scan(scanner);
                    }
                    catch (NumberFormatException e){
                        LOGGER.warn("Incorrect input received, hit \"done\" to exit");
                        input = scan(scanner);
                    }
                } while (!input.equalsIgnoreCase("done"));

            }
            // filter by zipcode
            else if (input.equalsIgnoreCase("f_zipcode")){
                getProductsWithFilter(true,false);
            }

            //filter by seller
            else if (input.equalsIgnoreCase("f_seller")){
                getProductsWithFilter(false,true);
            }

            //filter by zipcode and seller
            else if (input.equalsIgnoreCase("f_zipcode_seller")){
                getProductsWithFilter(true,true);
            }

            // view all products
            else if (input.equalsIgnoreCase("v_product")){
                LOGGER.info("********LIST OF PRODUCTS************");
                List<ProductInventory> products = productService.getAvailableProducts();
                for (ProductInventory p : products){
                    LOGGER.info("Product Name: {}", p.getProduct().getProductName());
                    LOGGER.info("Id: {}", p.getProduct().getProductId());
                    LOGGER.info("Available in: {} - {},{}", p.getStore().getName(), p.getStore().getLocation().getName(), p.getStore().getLocation().getZipcode());
                    LOGGER.info("Price: {}", p.getPrice());
                    LOGGER.info("****************************************************************************");
                }
            }

            else if(input.equalsIgnoreCase("t_product")){
                toggleProductAvailability();
            }

            //search by tags
            else if(input.equalsIgnoreCase("s_tags")){
                getProductsByTags();

            }
            else if (input.equalsIgnoreCase("exit")) {
                LOGGER.info("Exiting user menu...");
                success = true;
            }
        } while (!input.equalsIgnoreCase("exit"));


        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.USER_PRODUCTS);
            screen.setUser(user);
            success = screen.render(screenFactory);
        }
        return success;
    }

    /**
     * this methods gets the product by tags
     * @author: Pavithra Gunasekaran
     */
    private void getProductsByTags(){

        LOGGER.info("enter a tag name. Eg: milk");
        String tagInput = "";
        tagInput = scan(scanner);
        List<Tags> products = tagsService.getProducts(tagInput);
        if (products.isEmpty()){
            LOGGER.warn("No products found for the given tag");
        }
        else{
        for(Tags t:products) {
            List<ProductInventory> productInventoryList = productService.getProductInventory(t.getProduct());
            if (productInventoryList.isEmpty()) {
                LOGGER.warn("No products found for the given tag");
            }
            for (ProductInventory availableProductsByTag : productInventoryList) {
                LOGGER.info("Product Name: {}", availableProductsByTag.getProduct().getProductName());
                LOGGER.info("Id: {}", availableProductsByTag.getProduct().getProductId());
                LOGGER.info("Available in: {} - {},{}", availableProductsByTag.getStore().getName(), availableProductsByTag.getStore().getLocation().getName(), availableProductsByTag.getStore().getLocation().getZipcode());
                LOGGER.info("Price: {}", availableProductsByTag.getPrice());
                LOGGER.info("****************************************************************************");

            }
        }
    }
        }

    /**
     * this methods get the products based on the filter condition
     * @param filterByZipCode
     * @param filterBySeller
     * @author: Elizabeth James
     */
    private void getProductsWithFilter(boolean filterByZipCode, boolean filterBySeller) {
        String sellerInput = "", zipcodeInput = "";
        List<Location> locations = new ArrayList<>();
        List<ProductInventory> pList = new ArrayList<>();
        List<Merchant> merchants = new ArrayList<>();

        // apply the filter by zipcode
        if (filterByZipCode) {
            LOGGER.info("List of serviceable zipcodes");
            List<Store> storeList = storeService.getAllStores();
            Set<String> zipcodes = new HashSet<>();
            storeList.stream().forEach(s -> zipcodes.add(s.getLocation().getZipcode()));
            zipcodes.stream().forEach(z -> LOGGER.info(z));
            LOGGER.info("Enter the zipcode");
            zipcodeInput = scan(scanner);

            locations = locationService.getLocationByZipCode(zipcodeInput);

            // get the list of stores for the given zip code
            List<Store> filteredStores = new ArrayList<>();
            for (Location loc : locations) {
                filteredStores.addAll(storeService.getStoresByLocationAndMerchant(loc, null));
            }
            // add the products available in those stores
            for (Store s : filteredStores) {
                pList.addAll(productService.getProductByStore(s));
            }


        }

        // filter by seller
        if (filterBySeller) {
            LOGGER.info("List of sellers: ");
            List<Merchant> sellerList = (List<Merchant>) merchantService.getAllMerchants();
            Set<String> merchantsSet = new HashSet<>();
            sellerList.stream().forEach(s -> merchantsSet.add(s.getName()));
            merchantsSet.stream().forEach(m -> LOGGER.info(m));
            LOGGER.info("Enter the seller");
            sellerInput = scan(scanner);
            merchants = merchantService.getMerchantByName(sellerInput);
            List<Store> filteredStores = new ArrayList<>();

            // get the list of stores belonging to the merchant
            for (Merchant m : merchants) {
                filteredStores.addAll(storeService.getAllStoresBelongingToAMerchant(m));
            }
            // add the products available in those stores

            for (Store s : filteredStores) {
                pList.addAll(productService.getProductByStore(s));
            }
        }

        // filter by both seller and zipcode
        if (filterBySeller && filterByZipCode){
            List<Store> filteredStores = new ArrayList<>();
            //get the stores belonging to the zipcode and the merchant
            for (Location loc : locations) {
                for (Merchant m : merchants) {
                    filteredStores.addAll(storeService.getStoresByLocationAndMerchant(loc, m));
                }
            }
            for (Store s : filteredStores) {
                pList.addAll(productService.getProductByStore(s));
            }
        }


        // display the filtered products

        for (ProductInventory prodInv : pList) {
            LOGGER.info("Product Name: {}", prodInv.getProduct().getProductName());
            LOGGER.info("Id: {}", prodInv.getProduct().getProductId());
            Store store = prodInv.getStore();
            Location location = store.getLocation();
            LOGGER.info("Available in: {} - {},{}", store.getName(),
                    location.getName(), location.getZipcode());
            LOGGER.info("****************************************************************************");
        }

    }

    /**
     * Gets the product availability in a given location
     * @author Pavithra Gunasekaran
     */

    public void toggleProductAvailability(){
        LOGGER.info("Enter product name");
        String productName = scan(scanner);
        LOGGER.info("Enter location");
        String location = scan(scanner);
        List<ProductInventory> products = productService.getProductStockAvailability(productName,location);
        if(products!=null) {
            LOGGER.info("********LIST OF PRODUCTS************");
            for (ProductInventory p : products) {
                LOGGER.info("Product Name: {}", p.getProduct().getProductName());
                LOGGER.info("Id: {}", p.getProduct().getProductId());
                LOGGER.info("Available in: {} - {},{}", p.getStore().getName(), p.getStore().getLocation().getName(), p.getStore().getLocation().getZipcode());
                LOGGER.info("Price: {}", p.getPrice());
                LOGGER.info("Stock: {}", p.getStock());
                LOGGER.info("****************************************************************************");
            }
        }
        else
        {
            LOGGER.info("{} not available in {}",productName,location);
        }
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
