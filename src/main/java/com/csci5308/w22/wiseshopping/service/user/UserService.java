package com.csci5308.w22.wiseshopping.service.user;
import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
import com.csci5308.w22.wiseshopping.exceptions.user.UserAlreadyRegisteredException;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.repository.user.UserRepository;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
/**
 * @author Pavithra Gunasekaran
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantService.class);
    @Autowired
    UserRepository userRepository;

    @Transactional
    public User registerUser(String firstName, String lastName, String email, String password, String contact, String securityCode) {
        if (!Util.isValidString(firstName)) {
            throw new IllegalArgumentException("firstName cannot be null or empty or blank");
        }
        if (!Util.isValidString(lastName)) {
            throw new IllegalArgumentException("lastName cannot be null or empty or blank");
        }
        if (!Util.isValidString(contact)) {
            throw new IllegalArgumentException("contact number cannot be null or empty or blank");
        }
        if (!Util.isValidString(password)) {
            throw new IllegalArgumentException("password cannot be null or empty or blank");
        }
        if (!Util.isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("given email is not valid");
        }
        if (!Util.isValidString(securityCode)) {
            throw new IllegalArgumentException("given security code is not valid");
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new UserAlreadyRegisteredException(email + " is already registered");
        }
        user = new User(firstName, lastName, email, password, contact,securityCode);
        userRepository.save(user);
        LOGGER.info("User has been successfully registered");
        return user;
    }


    /**
     * Update userdetails such as firstname,lastname,contact
     * @param email
     * @param userDetails
     * @return
     * @author :Nilesh
     */

    public User updateUserDetails(String email, Map<String, String> userDetails) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Could not find any user with email id:" + email);
        }
        for (Map.Entry<String, String> entry : userDetails.entrySet()) {
            if (!Util.isValidString(entry.getValue())) {
                throw new IllegalArgumentException(entry.getKey() + " cannot be null or empty or blank");
            }
            switch (entry.getKey()) {
                case Constants.FIRST_NAME:
                    user.setFirstName(entry.getValue());
                    break;
                case Constants.LAST_NAME:
                    user.setLastName(entry.getValue());
                    break;
                case Constants.CONTACT:
                    user.setContact(entry.getValue());
                    break;
                default:
                    LOGGER.warn("No such key as {}", entry.getKey());
            }
        }
        userRepository.save(user);
        return user;
    }

    /**
     * this method verifies the login credentials for the user
     * @author Pavithra Gunasekaran
     * @param email
     * @param password
     * @return user
     */
    @Transactional
    public User loginUser(String email, String password) {
        if (!Util.isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("Given email is not valid");
        }

        if (!Util.isValidString(password)) {
            throw new IllegalArgumentException("password cannot be null or empty or blank");
        }

        User user = userRepository.findByEmailAndPassword(email, Util.encode(password));
        if (user!=null) {
            return user;
        }
        else {
            throw new NoSuchUserException("Invalid credentials. User does not exist");
        }
    }


    /**
     * this method resets the password for the requested user
     * @author Pavithra Gunasekaran
     * @param email
     * @param securityCode
     * @param newPassword
     * @return user
     */

    @Transactional
    public User resetPassword(String email, String securityCode, String newPassword){
        if (!Util.isValidString(email) && !EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new NoSuchUserException("Invalid email. User does not exist");
        }
        if(securityCode.equals(user.getSecurity_code())){
            user.setPassword(newPassword);
            userRepository.save(user);
        }
        else{
            LOGGER.warn("Invalid security code. Enter the correct security code.");
        }

        return user;
    }

    /**
     * this method gets the user details for the email entered
     * @author Pavithra Gunasekaran
     * @param email
     * @return user
     */
    @Transactional
    public User getUserByEmail(String email) {
        if (!Util.isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(email + " is not a valid email");
        }
        User user =  userRepository.findByEmail(email);
        if (user == null){
            throw new NoSuchUserException("No user found for this email id");
        }
        return user;
    }

    /**
     * this method removes the user from the database
     * @author Pavithra Gunasekaran
     * @param email
     * @return true if the data is removed
     */
    @Transactional
    public boolean removeUser(String email) {
        userRepository.deleteByEmail(email);
        return true;
    }
}
