package com.csci5308.w22.wiseshopping.integrationTests.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserProductsScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserMenuScreen;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Scanner;

/**
 * @author Elizabeth James
 * @contributor Nilesh
 * @contributor Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductScreenTests {

    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private UserProductsScreen userProductsScreen;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;

    private User user;

    private Merchant merchant;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {

        user = userService.loginUser("zig@zag1.com","zigzag");
        userMenuScreen.setUser(user);
    }

    @Test
    public void testAddProducts(){
        Mockito.when(scanner.next())
                .thenReturn("product").thenReturn("a_product").thenReturn("1").thenReturn("1").thenReturn("done").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testViewProducts(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("v_product").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
    @Test
    public void testFilterProductsBySeller() {
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("f_seller").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
    @Test
    public void testFilterProductsByZipcodeSeller() {
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("f_zipcode_seller").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
    @Test
    public void testFilterProductsByZipcode() {
        Mockito.when(scanner.next())
                // go tto products screen
                .thenReturn("products")
                // filter by zipcode
                .thenReturn("f_zipcode")
                // pass invalid input
                .thenReturn("asdf")
                // exit screen
                .thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testSearchtags(){
        Mockito.when(scanner.next())
                .thenReturn("tags").thenReturn("s_tags").thenReturn("milk").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testComparePrice(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("c_product").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testToggleProductAvailability(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("t_product").thenReturn("milk").thenReturn("halifax").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
    @Test
    public void testInvalidInput(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("exit").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
}
