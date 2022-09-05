package com.csci5308.w22.wiseshopping.service.vendor;

import com.csci5308.w22.wiseshopping.exceptions.user.NoSuchUserException;
import com.csci5308.w22.wiseshopping.exceptions.user.UserAlreadyRegisteredException;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.repository.vendor.MerchantRepository;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.csci5308.w22.wiseshopping.utils.Util.isValidString;


/**
 * @author Elizabeth James
 */

@Service
public class MerchantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantService.class);

    @Autowired
    MerchantRepository merchantRepository;

    /**
     * inserts a merchant into table
     *
     * @param name     name of merchant
     * @param email    email of merchant
     * @param password password of merchant
     * @return true if success, else false
     *  @author: Elizabeth James
     *
     */
    @Transactional
    public Merchant registerMerchant(String name, String email, String password,String securityCode) {
        // validate inputs
        if (!isValidString(name)) {
            throw new IllegalArgumentException("name cannot be null or empty or blank");
        }
        if (!isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }

        if (!isValidString(password)) {
            throw new IllegalArgumentException("password cannot be null or empty or blank");
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("given email is not valid");
        }
        if (!isValidString(securityCode)) {
            throw new IllegalArgumentException("security code cannot be null or empty or blank");
        }

        Merchant merchant = merchantRepository.findByEmail(email);
        if(merchant==null) {
            LOGGER.info("merchant is not registered");
        }
        if (merchant != null) {
            throw new UserAlreadyRegisteredException(email + " is already registered");
        }
        merchant = new Merchant(name, email, password,securityCode);
        merchantRepository.save(merchant);
        LOGGER.info("You have been registered successfully");
        return merchant;
    }

    /**
     * deletes a store from table
     *
     * @param email email of the merchant
     * @return true, if success; else false
     *  @author: Elizabeth James
     */
    @Transactional
    public boolean removeMerchant(String email) {
        if (!isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        int id = merchantRepository.deleteByEmail(email);
        if (id > 0) {
            return true;
        }
        return false;
    }

    /**
     * this method verifies the login credentials for the merchant
     * @author Pavithra Gunasekaran
     * @param email
     * @param password
     * @return merchant
     */
    @Transactional
    public Merchant loginMerchant(String email, String password) {
        if (!isValidString(email)) {
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("given email id is not valid");
        }

        if (!isValidString(password)) {
            throw new IllegalArgumentException("password cannot be null or empty or blank");
        }

        String hashedPassword = Util.encode(password);
        Merchant merchant = merchantRepository.findMerchantByEmailAndPassword(email, hashedPassword);

        if (merchant != null) {
            LOGGER.info("You have logged in successfully");
            return merchant;
        } else {
            throw new NoSuchUserException("Invalid credentials. Merchant does not exist");
        }
    }

    /**
     * this method gets Merchant by email address
     * @param email
     * @return
     * @author: Elizabeth James
     */
    public Merchant getMerchantByEmail(String email) {
        return merchantRepository.findByEmail(email);
    }

    /**
     * @author Adarsh Kannan
     */
    @Transactional
    public Merchant resetMerchantPassword(String email, String securityCode,String newPassword){
        if(!isValidString(email)){
            throw new IllegalArgumentException("email cannot be null or empty or blank");
        }
        Merchant merchant = merchantRepository.findByEmail(email);
        if (merchant == null){
            throw new NoSuchUserException("Invalid email. Merchant does not exist");
        }
        if(securityCode.equals(merchant.getSecurity_code())){
            merchant.setPassword(newPassword);
            merchantRepository.save(merchant);
        }
        return merchant;
    }

    public List<Merchant> getMerchantByName(String seller) {
        return merchantRepository.findMerchantByName(seller);
    }

    /**
     * this method gets all the merchants signed up in the application
     * @return list of merchants
     * @author: Elizabeth James
     */
    public Iterable<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }
}
