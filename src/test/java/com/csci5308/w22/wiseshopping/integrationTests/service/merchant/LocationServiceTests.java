package com.csci5308.w22.wiseshopping.integrationTests.service.merchant;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Elizabeth James
 */

//TODO: change to test
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class LocationServiceTests {
    @Autowired
    private LocationService locationService;

    private Location location;

    @BeforeEach
    public void setUp(){
        location = new Location("Timbuktu","000000","Timbuktu","TomorrowLand");
    }


    @Test
    public void testAddLocation(){
        Location actualLocation = locationService.addLocation("Timbuktu","000000","Timbuktu","TomorrowLand");
        location.setId(actualLocation.getId());
        Assertions.assertEquals(location,actualLocation);
        locationService.remove(location);

    }
}
