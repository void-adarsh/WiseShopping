package com.csci5308.w22.wiseshopping.integrationTests.screens.merchantfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantProductsScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserMenuScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserProductsScreen;
import com.csci5308.w22.wiseshopping.service.user.UserService;
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
 * @contributor Nilesh
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MerchantProductScreenTests {

    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private MerchantProductsScreen merchantProductsScreen;

    @Autowired
    @InjectMocks
    private MerchantMenuScreen merchantMenuScreen;

    private User user;

    private Merchant merchant;

    @Autowired
    private MerchantService merchantService;

    @BeforeEach
    public void setUp() {

        merchant = merchantService.loginMerchant("tom@test.com","tom");
        merchantMenuScreen.setMerchant(merchant);
    }

    @Test
    public void testAddProducts(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("a_product").thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProducts(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product").thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProductPrice(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product")
                .thenReturn("u_price")
                .thenReturn("1")
                .thenReturn("1")
                .thenReturn("10.99")
                .thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProductStock(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product")
                .thenReturn("u_stock")
                .thenReturn("1")
                .thenReturn("1")
                .thenReturn("10")
                .thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProductCategoryName(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product")
                .thenReturn("u_categoryname")
                .thenReturn("1")
                .thenReturn("updated_category_name")
                .thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProductCategoryDescription(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product")
                .thenReturn("u_categorydesc")
                .thenReturn("1")
                .thenReturn("updated_category_description")
                .thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

    @Test
    public void testUpdateProductTags(){
        Mockito.when(scanner.next())
                .thenReturn("products").thenReturn("u_product")
                .thenReturn("u_tags")
                .thenReturn("milk")
                .thenReturn("2")
                .thenReturn("tag_updated")
                .thenReturn("exit");
        merchantMenuScreen.setUser(user);
        Assertions.assertTrue(merchantMenuScreen.render(screenFactory));
    }

}
