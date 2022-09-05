package com.csci5308.w22.wiseshopping.integrationTests.screens.signin;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.StoreScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LogOutScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LoginScreen;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import com.csci5308.w22.wiseshopping.screens.signin.RegistrationScreen;
import com.csci5308.w22.wiseshopping.screens.signin.ResetPasswordScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserMenuScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserProductsScreen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
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
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginScreenTests {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private MerchantMenuScreen merchantMenuScreen;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;
    @Autowired
    @InjectMocks
    private LoginScreen loginScreen;
    @Autowired
    @InjectMocks
    private LogOutScreen logOut;

    @Autowired
    @InjectMocks
    private ResetPasswordScreen resetPasswordScreen;

    @Autowired
    @InjectMocks
    private RegistrationScreen registrationScreen;

    private User user;

    private Merchant merchant;

    @Autowired
    private UserService userService;

    @BeforeAll
    public void setUp()
    {
        loginScreen.setMerchant(merchant);
        loginScreen.setUser(user);
        logOut.setMerchant(merchant);
        logOut.setUser(user);
        merchantMenuScreen.setMerchant(merchant);
        merchantMenuScreen.setUser(user);
        userMenuScreen.setMerchant(merchant);
        userMenuScreen.setUser(user);
        resetPasswordScreen.setMerchant(merchant);
        resetPasswordScreen.setUser(user);

    }


    @Test
    public void testUserResetPassword(){
        Constants constants = new Constants();
        Mockito.when(scanner.next()).thenReturn("user")
                //email
                .thenReturn("zigzig@zag.com")
                //password
                .thenReturn("zigzag1")
                // reset password
                .thenReturn("resetPassword")
                .thenReturn("user")
                .thenReturn("zigzig@zag.com")
                .thenReturn("123")
                .thenReturn("zigzag")
                .thenReturn("exit");
        Assertions.assertTrue(loginScreen.render(screenFactory));

    }


    @Test
    public void testUserLoginPositive(){
        Mockito.when(scanner.next()).thenReturn("user")
                //email
                .thenReturn("zig@zag.com")
                //password
                .thenReturn("zigzag")
                //password
                .thenReturn(":").thenReturn("logout").thenReturn("exit");
        Assertions.assertTrue(loginScreen.render(screenFactory));

    }

    @Test
    public void testMerchantLoginPositive(){
        Mockito.when(scanner.next()).thenReturn("merchant")
                //email
                .thenReturn("zig@zag.com")
                //password
                .thenReturn("zigzag")
                // exit
                .thenReturn(":").thenReturn("logout").thenReturn("exit");
        Assertions.assertTrue(loginScreen.render(screenFactory));

    }
    @Test
    public void testMerchantResetPassword2(){
        Mockito.when(scanner.next()).thenReturn("merchant")
                //email
                .thenReturn("zigzig@zag.com")
                //password
                .thenReturn("zigzag1")
                // reset password
                .thenReturn("register")
                .thenReturn("exit");
        Assertions.assertTrue(loginScreen.render(screenFactory));

    }

    @Test
    public void testNavigations(){
        Mockito.when(scanner.next()).thenReturn(":")
                .thenReturn("register")
                .thenReturn("exit");
        loginScreen.render(screenFactory);
    }




}
