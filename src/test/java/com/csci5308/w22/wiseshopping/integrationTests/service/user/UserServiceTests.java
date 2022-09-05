package com.csci5308.w22.wiseshopping.integrationTests.service.user;

import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.service.user.UserService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author Pavithra Gunasekaran
 * @contributor Nilesh
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class UserServiceTests {

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp(){
        user = new User("John","Doe", "zig@zag1.com", "zigzag","1234567890","123");
    }

    /**
     * Integration test for user login
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testLoginUser(){
        User actualUser = userService.loginUser("zig@zag1.com","zigzag");
        Assertions.assertEquals(user.getEmail(),actualUser.getEmail());

    }

    /**
     * Integration Test to test update user information
     * @aythor nilesh
     */
    @Test
    public void testUpdateUser(){
        User actualUser = userService.loginUser("zig@zag1.com","zigzag");

        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.FIRST_NAME, "John ABC");
        userDetails.put(Constants.LAST_NAME, "Doe1");
        userDetails.put(Constants.CONTACT, "9096754412");
        User updatedUser = userService.updateUserDetails(actualUser.getEmail(), userDetails);

        //check if response is not null
        Assertions.assertNotNull(updatedUser);

        //Check if firstname,lastname and contact are updated
        Assertions.assertEquals("John ABC", updatedUser.getFirstName());
        Assertions.assertEquals("Doe1", updatedUser.getLastName());
        Assertions.assertEquals("zig@zag1.com", updatedUser.getEmail());
        Assertions.assertEquals("9096754412", updatedUser.getContact());

    }

    @Test
    public void testResetPassword(){
        User user = userService.resetPassword("adarsh@test.com","adarsh","adarsh2");
        Assertions.assertEquals(user.getPassword(), Util.encode("adarsh2"));
    }

    @Test
    public void testGetUserByEmail(){
        User user = userService.getUserByEmail("johndoe@xyz.com");
        Assertions.assertNotNull(user);
    }

}
