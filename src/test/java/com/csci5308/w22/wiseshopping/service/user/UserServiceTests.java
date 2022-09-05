package com.csci5308.w22.wiseshopping.service.user;
import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
/**
 * @author Harsh Hariramani
 * @contributor Nilesh
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository mockedUserRepository;
    @InjectMocks
    private UserService userService;
    private User user;
    @BeforeEach
    public void setUpUser(){
        user = new User("John","Doe", "johndoe@xyz.com", "password123","123467890","123");
    }
    @Test
    public void testRegisterUser() {
        when(mockedUserRepository.save(any(User.class))).thenReturn(user);
        User actualUser = userService.registerUser("John","Doe", "johndoe@xyz.com", "password123","123467890","123");
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    public void testInputParametersForRegisterUser() {
        IllegalArgumentException exceptionForName1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(null, "dummy", "dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("firstName cannot be null or empty or blank", exceptionForName1.getMessage());
        IllegalArgumentException exceptionForName2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("", "dummy", "dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("firstName cannot be null or empty or blank", exceptionForName2.getMessage());
        IllegalArgumentException exceptionForName3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(" ", "dummy", "dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("firstName cannot be null or empty or blank", exceptionForName3.getMessage());
        IllegalArgumentException exceptionForName4 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser( "dummy", null,"dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("lastName cannot be null or empty or blank", exceptionForName4.getMessage());
        IllegalArgumentException exceptionForName5 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "", "dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("lastName cannot be null or empty or blank", exceptionForName5.getMessage());
        IllegalArgumentException exceptionForName6 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser( "dummy", " ","dummy","dummy","dummy","dummy");
        });
        Assertions.assertEquals("lastName cannot be null or empty or blank", exceptionForName6.getMessage());
        IllegalArgumentException exceptionForEmail1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy",null, "dummy","dummy","dummy");
        });
        Assertions.assertEquals("email cannot be null or empty or blank", exceptionForEmail1.getMessage());
        IllegalArgumentException exceptionForEmail2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy","", "dummy","dummy","dummy");
        });
        Assertions.assertEquals("email cannot be null or empty or blank", exceptionForEmail2.getMessage());
        IllegalArgumentException exceptionForEmail3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy"," ", "dummy","dummy","dummy");
        });
        Assertions.assertEquals("email cannot be null or empty or blank", exceptionForEmail3.getMessage());
        IllegalArgumentException exceptionForPassword1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy", "dummy", null,"dummy","dummy");
        });
        Assertions.assertEquals("password cannot be null or empty or blank", exceptionForPassword1.getMessage());
        IllegalArgumentException exceptionForPassword2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy", "dummy", "", "dummy","dummy");
        });
        Assertions.assertEquals("password cannot be null or empty or blank", exceptionForPassword2.getMessage());
        IllegalArgumentException exceptionForPassword3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy", "dummy", " ","dummy","dummy");
        });
        Assertions.assertEquals("password cannot be null or empty or blank", exceptionForPassword3.getMessage());
        IllegalArgumentException exceptionForEmail4 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("dummy", "dummy", "dummy","dummy", "dummy","dummy");});
        Assertions.assertEquals("given email is not valid", exceptionForEmail4.getMessage());
    }

    /**
     * @author Nilesh
     * Unit test to check user updation
     */
    @Test
    public void testUpdateUserDetails() {
        when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(user);

        //Getting the updated user
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.FIRST_NAME, "John");
        userDetails.put(Constants.LAST_NAME, "Doe");
        userDetails.put(Constants.CONTACT, "9096754432");
        User updatedUser = userService.updateUserDetails("johndoe@xyz.com", userDetails);

        //check if response is not null
        Assertions.assertNotNull(updatedUser);

        //Check if firstname,lastname and contact are updated
        Assertions.assertEquals("John", updatedUser.getFirstName());
        Assertions.assertEquals("Doe", updatedUser.getLastName());
        Assertions.assertEquals("johndoe@xyz.com", updatedUser.getEmail());
        Assertions.assertEquals("9096754432", updatedUser.getContact());

    }

    /**
     * @author Nilesh
     * Unit test to check if updated first name is invalid
     */
    @Test
    public void testUpdateUserDetailsInvalidFirstName() {
        when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(user);
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.FIRST_NAME, "");
        IllegalArgumentException ex = Assertions.assertThrows( IllegalArgumentException.class,
                () -> {
            userService.updateUserDetails("johndoe@xyz.com", userDetails);
                }, "Exception not thrown");
        Assertions.assertTrue(ex.getMessage().contains(Constants.FIRST_NAME + " cannot be null or empty or blank"));
    }

    /**
     * @author Nilesh
     * Unit test to check if updated last name is invalid
     */
    @Test
    public void testUpdateUserDetailsInvalidLastName() {
        when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(user);
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.LAST_NAME, "");
        IllegalArgumentException ex = Assertions.assertThrows( IllegalArgumentException.class,
                () -> {
            userService.updateUserDetails("johndoe@xyz.com", userDetails);
                }, "Exception not thrown");
        Assertions.assertTrue(ex.getMessage().contains(Constants.LAST_NAME + " cannot be null or empty or blank"));
    }
    /**
     * @author Nilesh
     * Unit test to check if updated contact is invalid
     */
    @Test
    public void testUpdateUserDetailsInvalidContact() {
        when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(user);
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put(Constants.CONTACT, "");
        IllegalArgumentException ex = Assertions.assertThrows( IllegalArgumentException.class,
                () -> {
            userService.updateUserDetails("johndoe@xyz.com", userDetails);
                }, "Exception not thrown");
        Assertions.assertTrue(ex.getMessage().contains(Constants.CONTACT + " cannot be null or empty or blank"));
    }

    @Test
    public void testLoginUser()  {
        when(mockedUserRepository.findByEmailAndPassword(any(String.class),any(String.class))).thenReturn(user);

        User actualUser = userService.loginUser("johndoe@xyz.com", "password123");
        Assertions.assertEquals(user, actualUser);
    }

    @Test
    public void testInputParamLogin(){

        IllegalArgumentException emailNullException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser(null,"test_password");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailNullException.getMessage());
        IllegalArgumentException emailEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser("","test_password");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emailEmptyException.getMessage());
        IllegalArgumentException emailMissingDomainEx =Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser("test_email","test_password");
        });
        Assertions.assertEquals("Given email is not valid", emailMissingDomainEx.getMessage());

        IllegalArgumentException passwordEmptyException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser("test_email@xyz.com","");
        });
        Assertions.assertEquals("password cannot be null or empty or blank",passwordEmptyException.getMessage());
        IllegalArgumentException passwordNullException=Assertions.assertThrows(IllegalArgumentException.class, () ->{
        userService.loginUser("test_email@xyz.com",null);});
        Assertions.assertEquals("password cannot be null or empty or blank",passwordNullException.getMessage());

    }

    @Test
    public void testGetUserByEmail(){
        when(mockedUserRepository.findByEmail(any(String.class))).thenReturn(user);
        User user = userService.getUserByEmail("johndoe@xyz.com");
        Assertions.assertNotNull(user);
    }

    @Test
    public void testResetPasswordInputValidation(){
        IllegalArgumentException emptyEmailException=Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.resetPassword("","adarsh","adarsh2");
        });
        Assertions.assertEquals("email cannot be null or empty or blank",emptyEmailException.getMessage());

    }


    @Test
    public void testGetUserByEmailInputParameter(){
        IllegalArgumentException emptyEmail =Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail("");
        });
        IllegalArgumentException invalidEmail =Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserByEmail("dummmy");
        });
        NoSuchUserException nonexistingEmail =Assertions.assertThrows(NoSuchUserException.class, () -> {
            userService.getUserByEmail("d@d.com");
        });
    }

}

