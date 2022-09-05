package com.csci5308.w22.wiseshopping.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.NoCartCreatedForUserException;
import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.products.ProductInCart;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Elizabeth James
 */
@Component
public class CartScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartScreen.class);

    @Autowired
    private Scanner scanner;


    private ArrayList<String> validScreens;

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    private Merchant merchant;

    private User user;


    @Autowired
    public CartScreen() {
        // these denote the list of screens the user can navigate to from the current screen
        validScreens = new ArrayList<>(Arrays.asList(Constants.LOGOUT,
                Constants.CART,Constants.SUBSCRIPTIONS,
                Constants.USER_PRODUCTS,Constants.PROFILE,Constants.USER_MENU,Constants.PRICE_ANALYTICS));
    }

    /**
     * this shows the cart screen to the user.
     * the user can view, share, and checkout cart from this scree
     * @param screenFactory
     * @return true, on success
     * @author: Elizabeth James
     */
    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****CART SCREEN****");
        LOGGER.info("");


        boolean success = false;
        try {
            String input = "";
            do {
                LOGGER.info("Choose one of the following");
                LOGGER.info("Use v_, s_, c_ to view, share, checkout followed by cart");
                LOGGER.info("eg: v_cart, s_cart, c_cart");
                input = scan(scanner);
                //view cart
                if (input.equalsIgnoreCase("v_cart")) {
                    Set<Cart> carts = cartService.getCartsBelongingToAUser(user);
                    //cart is not shared. Only one cart is owned by user.
                    if (carts.isEmpty()){
                        LOGGER.warn("No active carts");
                    }
                    for (Cart cart : carts){
                       Map<String, Integer> cartContents = cartService.getProductsByCart(cart);
                        LOGGER.info("Cart belongs to {} ", cart.getUser().getEmail());
                        for (Map.Entry<String, Integer> entry : cartContents.entrySet()) {
                            LOGGER.info("Product: {} -> Quantity: {}", entry.getKey(), entry.getValue());
                        }
                    }
                }
                // share cart
                else if (input.equalsIgnoreCase("s_cart")) {
                    LOGGER.info("Enter the email id of user, one per line and \"done\" when completed");
                    String email = scan(scanner);
                    List<User> users = new ArrayList<>();
                    try {
                        while (!email.equalsIgnoreCase("done")) {
                            users.add(userService.getUserByEmail(email));
                            email = scan(scanner);
                            users.add(user);
                            success = cartService.shareCart(users);
                        }
                    }
                     catch(IllegalArgumentException | NoSuchUserException | NoCartCreatedForUserException e){
                        LOGGER.warn(e.getMessage());
                }
                }
                // checkout cart
                else if (input.equalsIgnoreCase("c_cart")){
                    Set<Cart> carts = cartService.getCartsBelongingToAUser(user);
                    List<ProductInCart> products = new ArrayList<>();
                    Map<Store, Double> cheapStore = new HashMap<>();
                    cartService.getCheapestCart(carts);

                    for (Cart cart : carts) {
                        cartService.checkoutCart(cart);
                        LOGGER.info("cart {} checked out", cart.getId());
                    }

                }

                else if (input.equalsIgnoreCase("exit")) {
                    LOGGER.info("Exiting cart menu...");
                } else {
                    LOGGER.warn("Invalid input received...");
                }
            } while (!input.equalsIgnoreCase("exit"));


        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.CART);
            screen.setUser(user);
            success = screen.render(screenFactory);
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

