package com.csci5308.w22.wiseshopping.integrationTests.screens.userfunctionality;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserMenuScreen;
import com.csci5308.w22.wiseshopping.screens.userfunctionality.UserProfileScreen;
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
public class UserProfileScreenTest {
    @Mock
    private Scanner scanner;

    @Autowired
    private ScreenFactory screenFactory;

    @Autowired
    @InjectMocks
    private UserProfileScreen userProfileScreen;

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
        userProfileScreen.setMerchant(merchant);
    }


    @Test
    public void testUpdateUserProfile(){

        Mockito.when(scanner.next())
                // go to profile screen
                .thenReturn("profile")
                // update profile
                .thenReturn("u_profile")
                // update contact
                .thenReturn("contact:123")
                // send invalid key
                .thenReturn("c : 124")
                .thenReturn("c")
                // done inputting keys
                .thenReturn("done")
                //invalid input
                .thenReturn("asdf")
                //exit
                .thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));


    }

    @Test
    public void testViewUserProfile(){

        Mockito.when(scanner.next())
                .thenReturn("profile").thenReturn("v_profile").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));


    }

    @Test
    public void testInvalidInput(){
        Mockito.when(scanner.next())
                .thenReturn("profile").thenReturn("profile").thenReturn("exit");
        userMenuScreen.setUser(user);
        Assertions.assertTrue(userMenuScreen.render(screenFactory));
    }
}
