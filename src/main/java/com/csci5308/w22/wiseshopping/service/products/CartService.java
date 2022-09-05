package com.csci5308.w22.wiseshopping.service.products;

import com.csci5308.w22.wiseshopping.exceptions.NoCartCreatedForUserException;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.cart.SharedCart;
import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInCart;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.cart.CartRepository;
import com.csci5308.w22.wiseshopping.repository.cart.SharedCartRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInCartRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInventoryRepository;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SharedCartRepository sharedCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private TagsRepository tagsRepository;


    /**
     * this method shares the cart among the list of users passed as parameters
     * @param otherUsersToShare list of users that want to share a cart
     * @return true, if marked as share
     * @Contributor: Elizabeth James
     */
    @Transactional
    public boolean shareCart(List<User> otherUsersToShare){
        Set<Cart> carts = new HashSet<>();
            for (User user : otherUsersToShare){
                Cart cart1 = cartRepository.findByUserAndStatus(user,Constants.INPROGRESS);
                if (cart1 == null) {
                    throw new NoCartCreatedForUserException("no cart for user " + user.getEmail());
                }
                cart1.setSharedStatus(Constants.SHARED);
                cartRepository.save(cart1);
                carts.add(cart1);
            }

            for (User user : otherUsersToShare){

                for (Cart cart : carts){

                    SharedCart sharedCart = sharedCartRepository.findSharedCartByCartAndUser(cart, user);
                    if (sharedCart == null){
                        sharedCart = new SharedCart(user, cart);
                    }
                    sharedCartRepository.save(sharedCart);
                }
            }

            LOGGER.info("We have clubbed your carts now!");
        return true;
    }

    /**
     * this method add the cart to the database
     * @param user
     * @param status
     * @return true, on success
     * @contributor: Elizabeth James
     */
    @Transactional
    public Cart addCart( User user, String status ){
        Cart cart = new Cart(user,status);
        cartRepository.save(cart);
        return cart;
    }

    /**
     * this method removes a cart from DB. this method is used only in cleanUp() methods of testcases
     * @param cart
     * @return true
     * @author: Elizabeth James
     */
    @Transactional
    public boolean remove(Cart cart) {
        if (cart == null){
            throw new IllegalArgumentException("cart cannot be null");
        }
        cartRepository.delete(cart);
        return true;
    }

    /**
     *
     * This method adds products to cart. If an existing product is added, the quantity is replaced.
     * @author: Elizabeth James
     * @param cart cart
     * @param product product
     * @param quantity quantity
     * @return true, if successfully added
     */
    @Transactional
    public boolean addProductInCart(Cart cart, Product product, int quantity){

        // validate arguments
        if (cart ==  null){
            throw new IllegalArgumentException("cart id cannot be null or empty or blank");
        }
        if (product == null ){
            throw new IllegalArgumentException("product id cannot be null or empty or blank");
        }

        if (quantity < 1 ){
            throw new IllegalArgumentException("quantity cannot be less than 1");
        }

        boolean success= false;
        // get the inventory from all stores for the given product
        List<ProductInventory> productInventories = productInventoryRepository.findByProduct(product);
        for ( ProductInventory productInventory : productInventories){

            Product prod = productInventory.getProduct();
            Store store = productInventory.getStore();

            // get the minimum quantity and insert it in table because we can provide only as much as the stock
            int quant = Math.min (quantity, productInventory.getStock());

            // check if the product is already existing in the cart
            ProductInCart productInCart = productInCartRepository.findByCartAndProductAndStore(cart,prod,store);

            // if existing in cart already, update the quantity
            if (productInCart!=null){
                productInCart.setQuantity(quant);
            }
            // else, add a new product in cart
            else{
                productInCart = new ProductInCart(cart, productInventory.getProduct(), productInventory.getStore(), quant, productInventory.getPrice());
            }

            productInCartRepository.save(productInCart);
            LOGGER.info("Product {}  from store {} is added to cart for a price {} each ",productInCart.getProduct().getProductName(), productInCart.getStore().getName(),productInCart.getPrice());
        }
        success = true;
        return success;
    }


    /**
     * this method removes a products from a cart from DB. this method is used only in cleanUp() methods of testcases
     * @param productInCart
     * @return true
     * @author: Elizabeth James
     */
    @Transactional
    public boolean remove(ProductInCart productInCart) {
        if (productInCart == null){
            throw new IllegalArgumentException("product cannot be null");
        }
        productInCartRepository.delete(productInCart);
        return true;
    }


    /**
     * This method returns the list of products in a cart
     * @param cart
     * @return
     * @author: Elizabeth James
     */
    public Map<String, Integer> getProductsByCart(Cart cart) {
        List<ProductInCart> products = productInCartRepository.findByCart(cart);
        Map <String, Integer> productQuantity = new HashMap<>();
        for (ProductInCart p : products){
            productQuantity.put(p.getProduct().getProductName(), p.getQuantity());
        }
        return productQuantity;

    }


    /**
     * this method returns the cart that belongs to a user (both shared and unshared) that has not been checkedout yet
     * @param user
     * @return set of cart
     */
    public Set<Cart> getCartsBelongingToAUser (User user){
        Set<Cart> carts = new HashSet<>();
        // get the inprogress cart of the user
        Cart cart = getCartByUserAndStatus(user, Constants.INPROGRESS);
        if(cart!=null) {
            String status = cart.getSharedStatus();
            Set<Integer> userIds = new HashSet<>();
            if (status.equalsIgnoreCase(Constants.SHARED)) {
                // if multiple users have shared the cart, get all the carts belonging to the group of shared users
                userIds.addAll(findSharedCarts(cart));
                for (Integer id : userIds) {
                    User user1 = userRepository.findById(id).orElse(null);
                    if (user1 != null) {
                        Cart cart1 = cartRepository.findByUserAndSharedStatusAndStatus(user1, Constants.SHARED, Constants.INPROGRESS);
                        if (cart1 != null)
                            carts.add(cart1);
                    }
                }
            }
            // add the user's shared cart to the list of shared carts as well
            carts.add(cart);
        }

        return carts;
    }

    /**
     * this method returns the cart for a given user and status
     * @param user
     * @param status
     * @return cart
     * @author: Elizabeth James
     */
    public Cart getCartByUserAndStatus(User user, String status) {
        return cartRepository.findByUserAndStatus(user, status);
    }

    /**
     * this method gets the list of userid that share a given cart
     * @param cart
     * @return list of user ids
     * @author : Elizabeth James
     */
    public List<Integer> findSharedCarts(Cart cart) {
        return sharedCartRepository.findSharedCartsByCart(cart.getId());
    }


    /**
     * this method marks the cart as checked out
     * @param cart cart to be checked out
     * @return true, if marked checked out
     * @author : Elizabeth James
     */
    public boolean checkoutCart(Cart cart) {
        cart.setStatus(Constants.CHECKEDOUT);
        cartRepository.save(cart);
        return true;
    }

    /**
     * this method returns the cheapest store to buy from for the list of products in cart
     * @param carts
     * @return Map of cheapest store
     * @author: Elizabeth James
     */
    public Map<Store, Double> getCheapestCart(Set<Cart> carts) {
        Set<ProductInCart> products = new HashSet<>();
        for (Cart cart : carts) {
            products.addAll(productInCartRepository.findByCart(cart));
        }
        // map store to its total price of products
        Map<Store, Double> mapOfStoreToPrice = new HashMap<>();

        // map store to its list of in-cart products
        Map<Store, List<Product>> mapOfStoreToProducts = new HashMap<>();

        // compute the total price of the products from all the available stores
        for (ProductInCart productInCart : products) {
            Store store = productInCart.getStore();
            double price = 0;
            List<Product> productsList = new ArrayList<>();
            if (mapOfStoreToPrice.containsKey(store)) {
                price = mapOfStoreToPrice.get(store);
            }
            price += productInCart.getPrice() * productInCart.getQuantity();
            mapOfStoreToPrice.put(store, price);

            if (mapOfStoreToProducts.containsKey(store)) {
                mapOfStoreToProducts.get(store).add(productInCart.getProduct());
            } else {
                Product p = productInCart.getProduct();
                productsList.add(p);
                mapOfStoreToProducts.put(store, productsList);
            }
        }

        // get the cheapest store
        Map<Store, Double> cheapestStore = getStoreWithMinimumPrice(mapOfStoreToPrice);
        return cheapestStore;
    }

    /**
     * this method gets the store with the minimum price
     * @param mapOfStoreToPrice
     * @return map of cheapest store
     * @author: Elizabeth James
     */
    private Map<Store, Double> getStoreWithMinimumPrice(Map<Store, Double> mapOfStoreToPrice) {
        // compute the store having the cheapest price
        double minPrice = Double.MAX_VALUE;
        Store minStore = null;
        LOGGER.info("Your total price is:");
        for (Map.Entry<Store, Double> entry : mapOfStoreToPrice.entrySet()) {
            if (minPrice > entry.getValue()) {
                minPrice = entry.getValue();
                minStore = entry.getKey();
            }
            LOGGER.info("${} from {}", entry.getValue(), entry.getKey().getName());
        }

        // store the cheapest store and its price in a map and return it
        Map<Store, Double> cheapestStore = new HashMap<>();
        cheapestStore.put(minStore, minPrice);
        if (minStore == null){
            return null;
        }
        LOGGER.info("Cheapest store: {} for ${}", minStore.getName(),minPrice);
        return cheapestStore;
    }

    /**
     * Gets the lowest price recommendations when user is adding the products to cart
     * @param productName
     * @param price
     * @return
     */

    @Transactional
    public HashMap<String,Float> getRecommendations(String productName, float price){
        List<Tags> getProductTags = tagsRepository.findByTagName(productName);
        HashMap<String,Float> recommendations = new HashMap<>();
        for(Tags t:getProductTags) {
            ProductInventory getProductsInventory = productInventoryRepository.findByOnlyProductId(t.getProduct().getProductId());
            if(price>getProductsInventory.getPrice()){
                recommendations.put(getProductsInventory.getStore().getName(),getProductsInventory.getPrice());
            }
        }
        return  recommendations;
    }

}