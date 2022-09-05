package com.csci5308.w22.wiseshopping.integrationTests.screens.analytics;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.analytics.DemandTrendAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.analytics.MerchantAnalyticsScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.StoreScreen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
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
public class MerchantAnalyticsScreenTests {

    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private MerchantAnalyticsScreen merchantAnalyticsScreen;

    @Autowired
    @InjectMocks
    private MerchantMenuScreen merchantMenuScreen;

    @Autowired
    private MerchantService merchantService;
    private User user;

    private Merchant merchant;

    @BeforeEach
    public void setUp() {
        merchant = merchantService.getMerchantByEmail("dummy@dummy.com");
        merchantMenuScreen.setUser(user);
        merchantMenuScreen.setMerchant(merchant);
        merchantAnalyticsScreen.setMerchant(merchant);
        merchantAnalyticsScreen.setUser(user);
    }

    @Order(1)
    @Test
    public void testMerchantDemandTrendAnalytics() {
        Mockito.when(scanner.next()).thenReturn("analytics")
                .thenReturn("demand_trend")
                .thenReturn("exit");
        merchantMenuScreen.setMerchant(merchant);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testNavigations(){
        Mockito.when(scanner.next()).thenReturn(":")
                .thenReturn("merchant")
                .thenReturn("exit");
        merchantAnalyticsScreen.render(screenFactory);
    }

}
