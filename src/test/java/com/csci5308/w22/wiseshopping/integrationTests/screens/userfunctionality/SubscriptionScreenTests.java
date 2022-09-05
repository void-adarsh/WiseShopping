package com.csci5308.w22.wiseshopping.integrationTests.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.products.Product;
import com.csci5308.w22.wiseshopping.models.Subscription;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.repository.subscription.SubscriptionRepository;
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
 * @author Elizabeth James
 * @contributor Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubscriptionScreenTests {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private SubscriptionScreen subscriptionScreen;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;

    private User user;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    public void setUp() {
        user = userService.loginUser("zig@zag1.com","zigzag");
    }


    @Order(1)
    @Test
    public void testViewSubscriptions(){

        Mockito.when(scanner.next())
                .thenReturn("subscriptions").thenReturn("v_subscriptions").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));


    }

    @Order(2)
    @Test
    public void testAddSubscriptions(){

        Mockito.when(scanner.next())
                .thenReturn("subscriptions").thenReturn("a_subscriptions").thenReturn("1").thenReturn("1.99").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));


    }

    @Order(3)
    @Test
    public void testDeleteSubscriptions(){

        Product product = new Product();
        product.setProductId(1);
        Subscription subscription  = subscriptionRepository.findByUserAndProduct(user,product);
        Mockito.when(scanner.next())
                .thenReturn("subscriptions")
                // delete sub
                .thenReturn("d_subscriptions")
                //insert id
                .thenReturn(String.valueOf(subscription.getId()))
                //exit
                .thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));

    }

    @Order(4)
    @Test
    public void testUpdateSubscriptions(){
        Mockito.when(scanner.next())
                .thenReturn("subscriptions").thenReturn("u_subscriptions").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Order(5)
    @Test
    public void testInvalidInput(){
        Mockito.when(scanner.next())
                .thenReturn("subscriptions").thenReturn("subscribe").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
}
