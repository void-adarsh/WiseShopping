package com.csci5308.w22.wiseshopping.repository.vendor;


import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

/**
 * @author Elizabeth James
 */
@Repository
public interface StoreRepository extends CrudRepository<Store, Integer> {
    /**
     * get all the stores belonging to a merchant id
     * @param merchantID
     * @return list of stores
     * @author: Elizabeth James
     */
    @Query(value = "SELECT * FROM store WHERE merchant_id = ?1", nativeQuery = true)
    List<Store> findByMerchantID(int merchantID);

    /**
     * get the store by its ID
     * @param storeId
     * @return store
     * @author: Elizabeth James
     */
    Store findById(int storeId);


    /**
     * get the store by name, starting time, ending time, business type, contact, location and merchant.
     * @param name
     * @param startingTime
     * @param endingTime
     * @param businessType
     * @param contact
     * @param location
     * @param merchant
     * @return store
     * @author: Elizabeth James
     */
    Store findByNameAndStartTimeAndEndTimeAndTypeAndContactAndLocationAndMerchant(String name, Time startingTime, Time endingTime, String businessType, String contact, Location location, Merchant merchant);

    /**
     * find store for the given merchant
     * @param merchant
     * @return list of stores
     * @author: Nilesh Gupta
     */
    List<Store> findByMerchant(Merchant merchant);

    /**
     * find store for a given location
     * @param location
     * @return list of stores
     * @author: Nilesh Gupta
     */
    List<Store> findByLocation(Location location);

    /**
     * find list of stores by location and merchant
     * @param location
     * @param merchant
     * @return list of stores
     * @author: Nilesh Gupta
     */
    List<Store> findByLocationAndMerchant(Location location, Merchant merchant);

    /**
     * find list of stores by store name
     * @param storeName
     * @return store
     * @author: Pavithra Gunasekaran
     */
    Store findByName(String storeName);
}
