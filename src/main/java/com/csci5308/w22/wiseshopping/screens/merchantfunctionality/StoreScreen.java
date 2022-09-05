package com.csci5308.w22.wiseshopping.screens.merchantfunctionality;

import com.csci5308.w22.wiseshopping.exceptions.screen.MenuInterruptedException;
import com.csci5308.w22.wiseshopping.factory.ScreenFactory;
import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.screens.signin.RegistrationScreen;
import com.csci5308.w22.wiseshopping.screens.Screen;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Elizabeth James
 */
@Component
public class StoreScreen implements Screen {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreScreen.class);
    @Autowired
    private Scanner scanner;
    @Autowired
    private StoreService storeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MerchantService merchantService;

    private ArrayList<String> validScreens;


//    private List<String> validScreens;

    private Merchant merchant;
    private User user;


    public StoreScreen() {
        // these denote the list of screens the user can navigate to from the current scree
        validScreens= new ArrayList<>(Arrays.asList(Constants.LOGOUT, Constants.MERCHANT_MENU,Constants.PRODUCTS,Constants.STORE_MENU,Constants.MERCHANT_ANALYTICS));
    }

    @Override
    public boolean render(ScreenFactory screenFactory) {
        LOGGER.info("****STORE MENU****");
        LOGGER.info("");
        boolean success = false;
        try {
            String input = "";
            LOGGER.info("enter one of the following - add or delete or update");
            LOGGER.info("enter a_store to add store");
            LOGGER.info ("enter d_store to delete store");
            LOGGER.info("enter u_store to update stores");
            LOGGER.info("enter v_store to view stores");
            LOGGER.info("enter exit to exit store menu");
            input = scan(scanner);
            //add store
            if (input.equalsIgnoreCase("a_store")) {
                LOGGER.info("Enter <store_name> <business_type> <start_time> <end_time> <contact> ");
                String storeName = scan(scanner);
                String businessType = scan(scanner);
                String startTime = scan(scanner);
                String endTime = scan(scanner);
                String contact = scan(scanner);
                LOGGER.info("Enter <locationName> <zipcode> <province> <country>");
                String locationName = scan(scanner);
                String zipcode = scan(scanner);
                String province = scan(scanner);
                String country = scan(scanner);
                Location location = locationService.addLocation(locationName, zipcode, province, country);
                setMerchant(merchant);
                Store store = storeService.addStore(storeName, businessType, startTime, endTime, contact, merchant, location);
                if (store != null) {
                    success = true;
                }

            }
            //delete store
            else if (input.equalsIgnoreCase("d_store")) {
                List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant);
                storeList.stream().forEach(s -> LOGGER.info("Store id:  {}, name: {}", s.getId(), s.getName()));
                LOGGER.info("Enter the id to be deleted");
                String idToBeDeleted = scan(scanner);
                success = storeService.remove(Integer.parseInt(idToBeDeleted));
                LOGGER.info("Store ID : {} deleted",idToBeDeleted);
            }

            //view store
            else if(input.equalsIgnoreCase("v_store")){
                List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant);
                storeList.stream().forEach(s -> LOGGER.info("Store id:  {}, name: {} startTime: {}, endTime: {}, contact: {}", s.getId(), s.getName(), s.getStartTime(),s.getEndTime(),s.getContact()));
            }

            // update store
            else if (input.equalsIgnoreCase("u_store")){
                List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant);
                storeList.stream().forEach(s -> LOGGER.info("Store id:  {}, name: {}", s.getId(), s.getName()));
                LOGGER.info("Enter the id to be updated");
                int idToBeUpdated = Integer.parseInt(scan(scanner));
                Store storeToBeUpdated = storeService.getStoreById(idToBeUpdated);
                LOGGER.info("Pass the attributes to be updated. Enter in <key>:<value> format");
                LOGGER.info("Acceptable keys are : name, type, startTime, endTime, contact");
                LOGGER.info("Enter done after updating");
                Map<String,String> mapAttributes = new HashMap<>();

                String keyValuePair = scan(scanner);
                do {
                    try {
                        String[] pair = keyValuePair.split(":");
                        mapAttributes.put(pair[0], pair[1]);

                    }
                    catch (Exception e){
                        LOGGER.error("Invalid key value pair: {}", keyValuePair);
                    }
                    finally {
                        keyValuePair = scan(scanner);
                    }
                }while (!keyValuePair.equalsIgnoreCase("done"));
                Store updatedStore = storeService.updateStore(storeToBeUpdated,mapAttributes);
                success = updatedStore!=null;
            }
        } catch (MenuInterruptedException e) {
            getNavigations(screenFactory, validScreens, LOGGER, scanner);
        }
        catch (DataAccessException | NumberFormatException e){
            LOGGER.warn("Invalid input");
            Screen screen = screenFactory.getScreen(Constants.STORE_MENU);
            screen.setUser(user);
            success = screen.render(screenFactory);
        }
        return success;
    }

    @Override
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    @Override
    public void setUser(User user) {
        this.user = user;

    }
}
