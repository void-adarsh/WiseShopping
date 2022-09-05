package com.csci5308.w22.wiseshopping.integrationTests.service.product;

import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInCart;
import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.service.products.ProductService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Set;

/**
 * @author Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private LocationService locationService;

    private ProductInCart productInCart;

    private Cart cart;

    private Store store;

    private User user;

    private Product product;

    private Merchant merchant;

    private Location location;

    @BeforeAll
    public void setUp(){


        user =userService.registerUser("test","test","test@test.com","test","test","test");
        cart = cartService.addCart(user,"inprogress");

        product=productService.addProduct("test","test");

        merchant = merchantService.registerMerchant("test", "test@test.com", "test","test");
        location =  locationService.addLocation("test","test","test","test");
        store=storeService.addStore("test","test","8","23","test",merchant,location);

        productInCart = new ProductInCart(cart,product,store,2,12);

    }


   @Test
   @Order(1)
    public void testAddProdInCart(){
       cartService.addProductInCart(cart,product,2);
       //System.out.println(productInCart.getCart().getId());
       Assertions.assertTrue(cartService.addProductInCart(cart,product,2));

    }

//    @Test
//    @Order(2)
    public void testRemoveExistingProductInCart(){
        cartService.addProductInCart(cart,product,2);
        Assertions.assertTrue(cartService.remove(productInCart));
    }

//    @Test
//    @Order(2)
//    public void testGetProductsByCart(){
//        Cart cart = productInCart.getCart();
//        System.out.println(cart);
//        System.out.println("id "+cart.getId());
//        Map<String, Integer> productsInCart =  cartService.getProductsByCart(cart);
//        System.out.println(productsInCart.keySet());
//        Assertions.assertTrue(productsInCart.size()>=1);
//    }

    @Test
    @Order(2)
    public void testGetCartsBelongingToAUser(){
        Assertions.assertNotNull(cartService.getCartsBelongingToAUser(user));
    }

    @Test
    @Order(3)
    public void testGetCartByUserAndStatus(){
        Assertions.assertNotNull(cartService.getCartByUserAndStatus(user,"inprogress"));
    }




    @Test
    @Order(4)
    public void testGetRecommendations(){
        Assertions.assertNotNull(cartService.getRecommendations("milk",1000));
    }

    @Test
    @Order(5)
    public void testCheckoutCart(){
        Assertions.assertNotNull(cartService.checkoutCart(cart));
    }
    @AfterAll
    public void cleanUp(){
        cartService.remove(productInCart);
        cartService.remove(cart);
        userService.removeUser(user.getEmail());



        storeService.remove(store);
        locationService.remove(location);
        merchantService.removeMerchant(merchant.getEmail());

        productService.remove(product);


    }

    //@Test
    // TODO
//    public void testShareCart(){
//        User currentUser = userRepository.findById(1).orElse(null);
//        List<User> otherUsers = new ArrayList<>();
//        otherUsers.add(userRepository.findById(2).orElse(null));
//        otherUsers.add(currentUser);
//        Assertions.assertNotNull(cartService.shareCart(currentUser,otherUsers));
//    }

//    //@Test
//    public void test2(){
//        User u = new User(5);
//        System.out.println(u);
//
//        Set<Cart> carts = cartService.getCartsBelongingToAUser(u);
//
//        for (Cart cart: carts){
//            cartService.getCheapestCart(cart);
//        }
//
//    }


}

