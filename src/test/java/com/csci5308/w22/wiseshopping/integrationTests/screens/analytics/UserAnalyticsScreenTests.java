package com.csci5308.w22.wiseshopping.integrationTests.screens.analytics;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.analytics.PriceAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LoginScreen;
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
 * @author Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserAnalyticsScreenTests {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;

    @Autowired
    @InjectMocks
    private PriceAnalyticsScreen priceAnalyticsScreen;

    @Autowired
    @InjectMocks
    private LoginScreen loginScreen;

    @Autowired
    private UserService userService;
    private User user;

    private Merchant merchant;
    @BeforeEach
    public void setUp() {
        user = userService.getUserByEmail("tom@test.com");
        userMenuScreen.setUser(user);
        priceAnalyticsScreen.setUser(user);
        priceAnalyticsScreen.setMerchant(merchant);
    }

    @Order(1)
    @Test
    public void testUserAnalytics() {
        Mockito.when(scanner.next())
                .thenReturn("analytics")
                //.thenReturn("iPhone")
                .thenReturn("exit")
        ;
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }

    @Test
    public void testNavigations(){
        Mockito.when(scanner.next()).thenReturn(":")
                .thenReturn("user_menu")
                .thenReturn("exit");
        priceAnalyticsScreen.render(screenFactory);
    }
}
