package com.csci5308.w22.wiseshopping.service.vendor;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.repository.vendor.StoreRepository;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.Map;

/**
 * this method acts like a service for store
 * @author Elizabeth James
 */
@Service
public class StoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private LocationService locationService;


    /**
     * add a store to the table
     * @param name name of store
     * @param businessType type of business of the store
     * @param startTime starting time of business
     * @param endTime ending time of business
     * @param contact contact info
     * @param merchant merchant that the store belongs to
     * @param location location of the store
     * @return true, if success; else false
     *  @author: Elizabeth James
     */
    @Transactional
    public Store addStore(String name, String businessType, String startTime, String endTime, String contact, Merchant merchant, Location location){

        //validate inputs
        if (!Util.isValidString(name)){
            throw new IllegalArgumentException("storeName cannot be null or empty or blank");
        }
        if (!Util.isValidString(businessType)){
            throw new IllegalArgumentException("businessType cannot be null or empty or blank");
        }

        if (!Util.isValidString(startTime)){
            throw new IllegalArgumentException("startTime cannot be null or empty or blank");
        }

        if (!Util.isValidString(endTime)){
            throw new IllegalArgumentException("endTime cannot be null or empty or blank");
        }

        if (!Util.isValidString(contact)){
            throw new IllegalArgumentException("contactNumber cannot be null or empty or blank");
        }

        if (merchant == null){
            throw new IllegalArgumentException("merchant cannot be null");
        }

        if (location == null){
            throw new IllegalArgumentException("location cannot be null");
        }

        //parse string to time
        Time startingTime = Util.parseTime(startTime);
        Time endingTime = Util.parseTime(endTime);
        // get store from DB, if existing
        // else, create a new store
        Store store = storeRepository.findByNameAndStartTimeAndEndTimeAndTypeAndContactAndLocationAndMerchant(name, startingTime, endingTime, businessType, contact, location, merchant);
        if (store == null) {
            store = new Store(name, startingTime, endingTime, businessType, contact, location, merchant);;
            storeRepository.save(store);
            LOGGER.info("Store {} is added",store.getName());
        }
        else {
            LOGGER.warn("Store with the same values exist in DB already...");
        }

        return store;
    }

    /**
     * this method updates the store
     * @param store
     * @param attributes
     * @return store
     *  @author: Elizabeth James
     */
    public Store updateStore(Store store, Map<String, String> attributes){
        // validate inputs
        if (store == null || attributes == null){
            throw new IllegalArgumentException("Store or attributes cannot be null");
        }
        boolean recorded = false;

        // update the fields as per the keys
        for (Map.Entry<String, String> entry : attributes.entrySet()) {

            switch (entry.getKey()) {
                    case Constants.KEY_NAME:
                        store.setName(attributes.get(Constants.KEY_NAME));
                        break;
                    case Constants.KEY_START_TIME:
                        Time startTime = Util.parseTime(attributes.get(Constants.KEY_START_TIME));
                        store.setStartTime(startTime);
                        break;
                    case Constants.KEY_END_TIME:
                        Time endTime = Util.parseTime(attributes.get(Constants.KEY_END_TIME));
                        store.setStartTime(endTime);
                        break;
                    case Constants.KEY_TYPE_OF_BUSINESS:
                        store.setType(attributes.get(Constants.KEY_TYPE_OF_BUSINESS));
                        break;
                    case Constants.KEY_CONTACT:
                        store.setContact(attributes.get(Constants.KEY_CONTACT));
                        break;
                    default:
                        LOGGER.warn("No such key as {}", entry.getKey());
                        break;
                }
        }
        Store updatedStore = storeRepository.save(store);
        LOGGER.info("Store {} is updated",store.getName());
        return updatedStore;
    }

    /**
     * deletes a store from table
     * @param store store
     * @return true, if success; else false
     *  @author: Elizabeth James
     */
    @Transactional
    public boolean remove(Store store) {
        if (store == null){
            throw new IllegalArgumentException("store cannot be null");
        }
        storeRepository.delete(store);
        return true;
    }

    /**
     * get the list of stores belonging to a merchant
     * @param merchant
     * @return list of store
     *  @author: Elizabeth James
     */
    @Transactional
    public List<Store> getAllStoresBelongingToAMerchant(Merchant merchant){
        return storeRepository.findByMerchantID(merchant.getId());
    }

    /**
     * remove a store from the DB based on its ID. If the id is not existing, the state of DB is same as the id being deleted. hence return true
     * @param id
     * @return true
     *  @author: Elizabeth James
     */
    @Transactional
    public boolean remove(int id){
        storeRepository.deleteById(id);
        return true;
    }


    /**
     * get store by its location and merchant
     * @param location
     * @param merchant
     * @return
     * @author: Nilesh
     */
    public List<Store> getStoresByLocationAndMerchant(Location location, Merchant merchant) {
        // validate inputs. one of the inputs is required
        if(location == null && merchant == null) {
            throw new IllegalArgumentException("Both location and merchant cannot be null");
        }
        // get stores by merchant,if location is null
        if (location == null) return storeRepository.findByMerchant(merchant);
        // get stores by location, if merchant is null
        if (merchant == null) return storeRepository.findByLocation(location);

        // else return stores belonging to merchant and location
        return storeRepository.findByLocationAndMerchant(location, merchant);
    }

    /**
     * this method returns a store by its id
     * @param id
     * @return
     */
    public Store getStoreById(int id) {
        return storeRepository.findById(id);
    }

    /**
     * this method gets the store details
     * @param storeName
     * @return store
     * @author: Pavithra Gunasekaran
     */
    public Store getStoreByName (String storeName){ return storeRepository.findByName(storeName);}

    /**
     * get the list of all serviceable stores
     * @return list of stores
     * @author: Elizabeth James
     */
    public List<Store> getAllStores() {
        return (List<Store>) storeRepository.findAll();
    }

}
