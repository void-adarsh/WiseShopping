package com.csci5308.w22.wiseshopping.integrationTests.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.Subscription;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.repository.subscription.SubscriptionRepository;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.CartScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.SubscriptionScreen;
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
 * @author Nilesh
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartScreenTests {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private CartScreen cartScreen;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;

    private User user;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        user = userService.loginUser("zig@zag1.com","zigzag");
    }

    @Test
    public void testViewCart(){

        Mockito.when(scanner.next())
                .thenReturn("cart").thenReturn("v_cart").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }


    @Test
    public void testShareCart(){

        Mockito.when(scanner.next())
                .thenReturn("cart").thenReturn("s_cart").thenReturn("test@test.com").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testCheckoutCart(){

        Mockito.when(scanner.next())
                .thenReturn("cart").thenReturn("c_cart").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
}
