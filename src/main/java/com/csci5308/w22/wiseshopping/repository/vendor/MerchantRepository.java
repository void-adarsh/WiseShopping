package com.csci5308.w22.wiseshopping.repository.vendor;

import com.csci5308.w22.wiseshopping.models.vendor.Merchant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Elizabeth James
 */
@Repository
public interface MerchantRepository extends CrudRepository<Merchant,Integer> {
    /**
     * delete the merchant for the given email
     * @param email
     * @return id of deleted merchant
     * @author: Elizabeth James
     */
     Integer deleteByEmail(String email);

    /**
     * find merchant by email and password
     * @param email
     * @param password
     * @return merchant
     * @author: Pavithra Gunasekran
     */
     Merchant findMerchantByEmailAndPassword(String email, String password);

    /**
     * find merchant by email
     * @param email
     * @return
     * @author: Elizabeth James
     */
     Merchant findByEmail(String email);

    /**
     * find merchants by their name
     * @param seller
     * @return list of merchants
     * @author: Elizabeth James
     */
    List<Merchant> findMerchantByName(String seller);
}
