package com.csci5308.w22.wiseshopping.integrationTests.screens.signin;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.repository.vendor.MerchantRepository;
import com.csci5308.w22.wiseshopping.screens.merchantfunctionality.MerchantMenuScreen;
import com.csci5308.w22.wiseshopping.screens.signin.LoginScreen;
import com.csci5308.w22.wiseshopping.screens.signin.RegistrationScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserMenuScreen;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
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
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationScreenTests {
    @Mock
    private Scanner scanner;


    @Autowired
    private MerchantService merchantService;

    @Autowired
    @InjectMocks
    private RegistrationScreen registrationScreen;

    @Autowired
    @InjectMocks
    private LoginScreen loginScreen;

    @Autowired
    @InjectMocks
    private MerchantMenuScreen merchantMenuScreen;

    @Autowired
    @InjectMocks
    private UserMenuScreen userMenuScreen;
    @Autowired
    private ScreenFactory screenFactory;

    private UserService userService;

    private User user;

    private Merchant merchant;

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setUp()
    {
        registrationScreen.setMerchant(merchant);
        registrationScreen.setUser(user);
    }
    @Test
    public void testUserRegistration(){
        Mockito.when(scanner.next()).thenReturn("user")
                // first name
                .thenReturn("zig")
                //last name
        .thenReturn("zag")
                //email
        .thenReturn("zig@zag1.com")
                //password
        .thenReturn("zigzag")
                //contact
        .thenReturn("123456")
                //securitycode
                .thenReturn("123")
                .thenReturn("exit");
        Assertions.assertTrue(registrationScreen.render(screenFactory));

    }
    @Test
    public void testMerchantRegistration(){
        Mockito.when(scanner.next())
                //invalid input
                .thenReturn("m")
                //correct input
                .thenReturn("merchant")
                // first name
                .thenReturn("zig")
                //email
                .thenReturn("zig@zag.com")
                //password
                .thenReturn("zigzag")
        //securitycode
                .thenReturn("123")
                .thenReturn("exit");
        Assertions.assertTrue(registrationScreen.render(screenFactory));

    }


    @Test
    public void testNavigations(){
        Mockito.when(scanner.next()).thenReturn(":")
                .thenReturn("login")
                .thenReturn("exit");
        registrationScreen.render(screenFactory);
    }
}
