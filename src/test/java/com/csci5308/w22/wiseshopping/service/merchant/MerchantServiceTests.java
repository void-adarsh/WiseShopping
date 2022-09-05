package com.csci5308.w22.wiseshopping.service.merchant;


import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.repository.vendor.MerchantRepository;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

/**
 * @author Elizabeth James
 */
@ExtendWith(MockitoExtension.class)
public class MerchantServiceTests {
    @Mock
    private MerchantRepository mockedMerchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    private Merchant merchant;

    @BeforeEach
    public void setUp(){
         merchant = new Merchant("John Doe", "johndoe@xyz.com", "password123","123");
    }

    @Test
    public void testRegisterMerchant() {
        when(mockedMerchantRepository.save(any(Merchant.class))).thenReturn(merchant);
        Merchant actualMerchant = merchantService.registerMerchant("John Doe", "johndoe@xyz.com", "password123","123");
        Assertions.assertEquals(merchant, actualMerchant);
    }


    @Test
    public void testInputParametersForRegisterMerchant(){
        IllegalArgumentException exceptionForName1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant(null,"dummy","dummy","123");
        });
        Assertions.assertEquals("name cannot be null or empty or blank",exceptionForName1.getMessage());
        IllegalArgumentException exceptionForName2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("","dummy","dummy","123");
        });
        Assertions.assertEquals("name cannot be null or empty or blank",exceptionForName2.getMessage());
        IllegalArgumentException exceptionForName3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant(" ","dummy","dummy","123");
        });
        Assertions.assertEquals("name cannot be null or empty or blank",exceptionForName3.getMessage());

        IllegalArgumentException exceptionForEmail1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy",null,"dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail1.getMessage());
        IllegalArgumentException exceptionForEmail2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy","","dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail2.getMessage());
        IllegalArgumentException exceptionForEmail3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy"," ","dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail3.getMessage());

        IllegalArgumentException exceptionForPassword1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy","dummy",null,"123");
        });
        Assertions.assertEquals("password cannot be null or empty or blank",exceptionForPassword1.getMessage());
        IllegalArgumentException exceptionForPassword2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy","dummy","","123");
        });
        Assertions.assertEquals("password cannot be null or empty or blank",exceptionForPassword2.getMessage());
        IllegalArgumentException exceptionForPassword3 = Assertions.assertThrows(IllegalArgumentException.class, () ->{
            merchantService.registerMerchant("dummy","dummy"," ","123");
        });
        Assertions.assertEquals("password cannot be null or empty or blank",exceptionForPassword3.getMessage());

        IllegalArgumentException exceptionForEmail4 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy","dummy","dummy","123");
        });
        Assertions.assertEquals("given email is not valid",exceptionForEmail4.getMessage());

        IllegalArgumentException exceptionForSecurityCode = Assertions.assertThrows(IllegalArgumentException.class, () ->{
            merchantService.registerMerchant("dummy","dummy@dummy.com","dummy","");
        });
        Assertions.assertEquals("security code cannot be null or empty or blank",exceptionForSecurityCode.getMessage());

    }

    @Test
    public void testRemoveMerchantForExistingRow(){
        when(mockedMerchantRepository.deleteByEmail(any(String.class))).thenReturn(1);
        Assertions.assertTrue(merchantService.removeMerchant("n@n.com"));
    }

    @Test
    public void testRemoveMerchantForNonExistingRow(){
        when(mockedMerchantRepository.deleteByEmail(any(String.class))).thenReturn(0);
        Assertions.assertFalse(merchantService.removeMerchant("n@n.com"));
    }

    @Test
    public void testInputParametersForRemoveMerchant(){
        IllegalArgumentException exceptionForEmail1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy",null,"dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail1.getMessage());
        IllegalArgumentException exceptionForEmail2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy","","dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail2.getMessage());
        IllegalArgumentException exceptionForEmail3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.registerMerchant("dummy"," ","dummy","123");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exceptionForEmail3.getMessage());

    }

    @Test
    public void testIllegalArgumentsRemoveNonExistingMerchant(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.removeMerchant(null);
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exception.getMessage());
        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.removeMerchant("");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exception.getMessage());
        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->{
            merchantService.removeMerchant(" ");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",exception.getMessage());
    }

    @Test
    public void testLoginMerchant()  {

        when(mockedMerchantRepository.findMerchantByEmailAndPassword(any(String.class),any(String.class))).thenReturn(merchant);
        Merchant actualMerchant = merchantService.loginMerchant("johndoe@xyz.com", "password123");
        Assertions.assertEquals(merchant, actualMerchant);
    }

    @Test
    public void testInputParamLogin(){



        IllegalArgumentException emailNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.loginMerchant(null,"test_password");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailNullException.getMessage());
        IllegalArgumentException emailEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.loginMerchant("","test_password");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailEmptyException.getMessage());
        IllegalArgumentException emailMissingDomainEx =Assertions.assertThrows(IllegalArgumentException.class, () ->{
            merchantService.loginMerchant("test_email","test_password");});
        Assertions.assertEquals("given email id is not valid", emailMissingDomainEx.getMessage());

        IllegalArgumentException passwordEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.loginMerchant("test_email@xyz.com","");
        });
        Assertions.assertEquals("password cannot be null or empty or blank",passwordEmptyException.getMessage());

        IllegalArgumentException passwordNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.loginMerchant("test_email@xyz.com",null);
        });
        Assertions.assertEquals("password cannot be null or empty or blank",passwordNullException.getMessage());
    }

    @Test
    public void testResetMerchantPassword()  {
        when(mockedMerchantRepository.findByEmail(any(String.class))).thenReturn(merchant);

        Merchant actualMerchant= merchantService.resetMerchantPassword("johndoe@xyz.com","q1w2e3r4","");
        Assertions.assertEquals(merchant, actualMerchant);
    }

    @Test
    public void testInputParametersForResetMerchantPassword(){

        IllegalArgumentException emailNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.resetMerchantPassword(null,null,null);
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailNullException.getMessage());
        IllegalArgumentException emailEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            merchantService.resetMerchantPassword("","","");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailEmptyException.getMessage());
//        IllegalArgumentException emailMissingDomainNameException=Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.resetMerchantPassword("test_email"));
//        Assertions.assertEquals("Given email or se is not valid",emailMissingDomainNameException.getMessage());



    }
    @Test
    public void testGetAllMerchants(){
        when(mockedMerchantRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(merchantService.getAllMerchants());
    }

    @Test
    public void testGetMerchantByName(){
        when(mockedMerchantRepository.findMerchantByName(any(String.class))).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(merchantService.getMerchantByName("dummy"));
    }


    @Test
    public void testGetMerchantByEmail(){
        when(mockedMerchantRepository.findByEmail(any(String.class))).thenReturn(new Merchant());
        Assertions.assertNotNull(merchantService.getMerchantByEmail("dummy"));
    }



}
