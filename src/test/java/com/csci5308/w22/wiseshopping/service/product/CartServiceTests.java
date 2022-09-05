package com.csci5308.w22.wiseshopping.service.product;

import com.csci5308.w22.wiseshopping.exceptions.NoCartCreatedForUserException;
import com.csci5308.w22.wiseshopping.models.*;
import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.cart.SharedCart;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.products.ProductInCart;
import com.csci5308.w22.wiseshopping.models.products.ProductInventory;
import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.cart.CartRepository;
import com.csci5308.w22.wiseshopping.repository.cart.SharedCartRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInCartRepository;
import com.csci5308.w22.wiseshopping.repository.product.ProductInventoryRepository;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.service.products.CartService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Pavithra Gunasekaran
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTests {
    @Mock
    private ProductInCartRepository mockedProductInCartRepository;
    @Mock
    private ProductInventoryRepository mockedProInvRepository;

    @Mock
    private CartRepository mockedCartRepository;

    @Mock
    private SharedCartRepository mockedSharedCartRepository;


    @InjectMocks
    private CartService cartService;

    private ProductInCart productInCart;

    private Cart cart;

    private Store store;

    private User user;

    private Product product;

    private ProductInventory productInventory;

    private Merchant merchant;

    private Location location;

    @BeforeEach
    public void setUp(){
        user=new User(10);
        cart = new Cart(1,user, Constants.INPROGRESS, Constants.NOTSHARED);
        product = new Product(1);
        store = new Store();
        store.setId(1);
        productInCart = new ProductInCart(cart,product,2);
        productInventory = new ProductInventory();
        productInventory.setProduct(product);
        productInventory.setPrice(1);
        productInventory.setStock(1);
        productInventory.setStore(store);



        user =new User("testuser","testuser","testuser@test.com","test","test","123");
        cart = new Cart(user,"inprogress");

        product=new Product("test","test");

        merchant = new Merchant("test", "test@test.com", "test");
        location =  new Location("test","test","test","test");
        store=new Store("test", Util.parseTime("8"),Util.parseTime("23"),"test","test",location,merchant);

        productInCart = new ProductInCart(cart,product,2);
    }

    @Test
    public void testAddProdCart() {
        when(mockedProductInCartRepository.save(any(ProductInCart.class))).thenReturn(productInCart);
        List<ProductInventory> productInventoryList = new ArrayList<>();
        productInventoryList.add(productInventory);
        when(mockedProInvRepository.findByProduct(product)).thenReturn(productInventoryList);
        Assertions.assertNotNull(cartService.addProductInCart(cart,product,2));
    }

    @Test
    public void testInputParametersForAddProductInCart(){
        IllegalArgumentException exceptionForCart = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductInCart(null,product,2);
        });
        Assertions.assertEquals("cart id cannot be null or empty or blank",exceptionForCart.getMessage());
        IllegalArgumentException exceptionForProduct = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductInCart(cart,null,2);
        });
        Assertions.assertEquals("product id cannot be null or empty or blank",exceptionForProduct.getMessage());

        IllegalArgumentException exceptionForQuantity = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductInCart(cart,product,0);
        });
        Assertions.assertEquals("quantity cannot be less than 1",exceptionForQuantity.getMessage());

    }


    @Test
    public void testRemoveProductInCart(){
        Assertions.assertTrue(cartService.remove(productInCart));
    }

    @Test
    public void testRemoveCart(){

        Assertions.assertTrue(cartService.remove(new Cart()));
    }

    @Test
    public void testRemoveProductInCartInputParameters(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cartService.remove((Cart) null);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cartService.remove((ProductInCart) null);;
        });
    }

    @Test
    public void testAddCart(){
        when(mockedCartRepository.save(any(Cart.class))).thenReturn(new Cart());
        Assertions.assertNotNull(cartService.addCart(user, Constants.NOTSHARED));
    }

    @Test
    public void testSharedCart() {
        User otherUser = new User(1);
        List<User> otherUsers = new ArrayList<>();
        otherUsers.add(otherUser);
//        when(mockedCartRepository.findByUserAndSharedStatusAndStatus(any(User.class),any(String.class),any(String.class))).thenReturn(cart);
//        when(mockedSharedCartRepository.findByUserAndCart(any(User.class),any(Cart.class))).thenReturn(new SharedCart());
//        when(mockedCartRepository.save(any(Cart.class))).thenReturn(cart);
        NoCartCreatedForUserException ex = Assertions.assertThrows(NoCartCreatedForUserException.class, () -> {
            cartService.shareCart(otherUsers);
        });
        Assertions.assertEquals("no cart for user null", ex.getMessage());
    }

    @Test
    public void testGetProductsByCart(){
        when(mockedProductInCartRepository.findByCart(cart)).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(cartService.getProductsByCart(cart));
    }

    @Test
    public void testGetCartsBelongingToAUser (){
        Cart c = new Cart();
        c.setSharedStatus(Constants.SHARED);

        when(mockedCartRepository.findByUserAndStatus(user,Constants.INPROGRESS)).thenReturn(c);
//       when(mockedUserRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.ofNullable(user));
      //  when(mockedCartRepository.findByUserAndSharedStatusAndStatus(user, Constants.SHARED, Constants.INPROGRESS)).thenReturn(new Cart());
        Assertions.assertNotNull(cartService.getCartsBelongingToAUser(user));
    }

    @Test
    public void testFindSharedCarts(){
        when(mockedSharedCartRepository.findSharedCartsByCart(cart.getId())).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(cartService.findSharedCarts(cart));
    }
    @Test
    public void testCheckoutCart(){
        Assertions.assertTrue(cartService.checkoutCart(cart));
    }

//    @Test
//    public void testCheapCart(){
//        when(mockedProductInCartRepository.findByCart(cart)).thenReturn(new HashSet<>());
//        Assertions.assertNotNull(cartService.getCheapestCart(cart));
//    }


}
