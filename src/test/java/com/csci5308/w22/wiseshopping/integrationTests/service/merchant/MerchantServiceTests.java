package com.csci5308.w22.wiseshopping.integrationTests.service.merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Elizabeth James
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MerchantServiceTests {
    @Autowired
    private MerchantService merchantService;

    private Merchant merchant;

    @BeforeEach
    public void setUp(){
        merchant = new Merchant("John Doe", "johndoe@xyz.com", "password123","123");
    }

    @AfterAll()
    public void cleanUp(){
        merchantService.removeMerchant("johndoe@xyz.com");
    }
    @Test
    @Order(1)
    public void testRegisterMerchant(){
        Merchant actualMerchant = merchantService.registerMerchant("John Doe","johndoe@xyz.com","password123","123");
        merchant.setId(actualMerchant.getId());
        Assertions.assertEquals(merchant,actualMerchant);

    }

    @Test
    @Order(2)
    public void testLoginMerchant(){
        Merchant actualMerchant = merchantService.loginMerchant("johndoe@xyz.com","password123");
        Assertions.assertEquals(merchant.getPassword(),actualMerchant.getPassword());
    }


    @Test
    @Order(3)
    public void testRemoveExistingMerchant(){
        merchantService.registerMerchant("John Doe2","johndoe2@xyz.com","password123","123");
        Assertions.assertTrue(merchantService.removeMerchant("johndoe2@xyz.com"));
    }

    @Test
    @Order(4)
    public void testRemoveNonExistingMerchant(){
        Assertions.assertFalse(merchantService.removeMerchant("johndoe2@xyz.com"));
    }

//
//    @Test
//    @Order(5)
//    public void testResetPassword(){
//        Merchant actualMerchant = merchantService.resetMerchantPassword("zig@zag1.com","q1w2e3r4","zigzag");
//        Merchant merchant = merchantService.resetMerchantPassword(actualMerchant.getEmail(),"q1w2e3r4","abcd" );
//    }

}
