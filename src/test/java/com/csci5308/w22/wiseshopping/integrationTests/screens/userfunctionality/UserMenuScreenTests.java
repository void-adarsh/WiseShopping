package com.csci5308.w22.wiseshopping.integrationTests.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.analytics.PriceAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.*;
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
 * @contributor Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserMenuScreenTests {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;

    @Autowired
    @InjectMocks
    private UserProductsScreen userProductScreen;

    @Autowired
    @InjectMocks
    private CartScreen cartScreen;

    @Autowired
    @InjectMocks
    private UserProfileScreen userProfileScreen;

    @Autowired
    @InjectMocks
    private SubscriptionScreen subscriptionScreen;

    @Autowired
    @InjectMocks
    private PriceAnalyticsScreen priceAnalyticsScreen;



    private User user;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {

        user = userService.loginUser("zig@zag1.com","zigzag");
        userMenuScreen.setUser(user);
    }


    @Test
    public void testProducts(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }


    @Test
    public void testProfile(){

        Mockito.when(scanner.next())
                .thenReturn("profile").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }


    @Test
    public void testSubscriptions(){

        Mockito.when(scanner.next())
                .thenReturn("subscriptions").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }


    @Test
    public void testPriceAnalytics(){

        Mockito.when(scanner.next())
                .thenReturn("analytics").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testCart(){

        Mockito.when(scanner.next())
                .thenReturn("cart").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
}
