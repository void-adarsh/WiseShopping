package com.csci5308.w22.wiseshopping.integrationTests.service.merchant;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.models.vendor.Merchant;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import com.csci5308.w22.wiseshopping.service.vendor.MerchantService;
import com.csci5308.w22.wiseshopping.service.vendor.StoreService;
import com.csci5308.w22.wiseshopping.utils.Constants;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Elizabeth James
 */
//TODO: change profile to test later
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class StoreServiceTests {
    @Autowired
    private StoreService storeService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private LocationService locationService;

    private Merchant merchant;
    private Location location;

    @BeforeEach
    public void setUp(){
        merchant = merchantService.registerMerchant("dummy", "dummy2@dummy.com", "dummy","dummy");
        location =  locationService.addLocation("dummy","dummy","dummy","dummy");

    }
    @Test
    public void testAddStore() {

        Store expectedStore  = new Store("Timbuktu", Util.parseTime("11"),  Util.parseTime("12"), "private", "John Doe", location,merchant);

        Store actualStore = storeService.addStore("Timbuktu", "private", "11","12", "John Doe", merchant,location);
        expectedStore.setId(actualStore.getId());
        Assertions.assertEquals(expectedStore, actualStore);
        storeService.remove(expectedStore);

    }

    @Test
    public void testUpdateStore() {

        Store expectedStore  = new Store("Timbuktu",  Util.parseTime("11"),  Util.parseTime("12"), "private", "John Doe", location,merchant);

        Store actualStore = storeService.addStore("Timbuktu", "private", "11","12", "John Doe", merchant,location);
        Map<String, String> map = new HashMap<>();
        map.put(Constants.KEY_TYPE_OF_BUSINESS, "none");
        map.put(Constants.KEY_CONTACT, "1");
        map.put(Constants.KEY_NAME, "walmart");
        map.put(Constants.KEY_START_TIME, "11");
        map.put(Constants.KEY_END_TIME,"12");
        // unknown key
        map.put("end time","12");
        Store updatedStore = storeService.updateStore(actualStore, map);
        Assertions.assertNotNull(updatedStore);
        storeService.remove(updatedStore);

    }


    @Test
    public void testRemoveStores(){
        storeService.addStore("Timbuktu", "private", "11","12", "John Doe", merchant,location);

        Merchant merchant2 = merchantService.getMerchantByEmail("dummy2@dummy.com");
        List<Store> storeList = storeService.getAllStoresBelongingToAMerchant(merchant2);
        Assertions.assertTrue(storeService.remove(storeList.stream().findFirst().get().getId()));
    }

    /**
     * @author Nilesh
     * Integration test to get stored by location and merchant
     */
    @Test
    public void testGetStoresByLocationAndMerchant(){
        Store newstore = storeService.addStore("Timbuktu", "private", "11", "12" ,"John Doe", merchant,location);
        List<Store> storeList = storeService.getStoresByLocationAndMerchant(location, merchant);
        Assertions.assertEquals(1, storeList.size());
        Store s = storeList.get(0);
        Assertions.assertEquals("Timbuktu", s.getName());
        Assertions.assertEquals("private", s.getType());
        Assertions.assertEquals("John Doe", s.getContact());
        Assertions.assertEquals(merchant, s.getMerchant());
        Assertions.assertEquals(location, s.getLocation());
        storeService.remove(newstore);

    }

    @AfterEach
    public void cleanUp(){
        locationService.remove(location);
        merchantService.removeMerchant("dummy2@dummy.com");
    }

}
