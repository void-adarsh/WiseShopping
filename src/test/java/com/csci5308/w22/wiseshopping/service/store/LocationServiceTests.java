package com.csci5308.w22.wiseshopping.service.store;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.repository.vendor.LocationRepository;
import com.csci5308.w22.wiseshopping.service.vendor.LocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Elizabeth James
 * @contributor nilesh
 */
@ExtendWith(MockitoExtension.class)
public class LocationServiceTests {
    @Mock
    private LocationRepository mockedLocationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    public void setUp(){
        location = new Location("Timbuktu","000000","Timbuktu","TomorrowLand");
    }

    @Test
    public void testAddLocation(){
        when(mockedLocationRepository.save(any(Location.class))).thenReturn(location);
        Location actualLocation = locationService.addLocation("Timbuktu","000000","Timbuktu","TomorrowLand");
        Assertions.assertEquals(location, actualLocation );
    }

    @Test
    public void testInputParametersForAddLocation(){
        IllegalArgumentException exceptionForLocationName1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation(null,"000000","Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("locationName cannot be null or empty or blank",exceptionForLocationName1.getMessage());
        IllegalArgumentException exceptionForLocationName2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("","000000","Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("locationName cannot be null or empty or blank",exceptionForLocationName2.getMessage());
        IllegalArgumentException exceptionForLocationName3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation(" ","000000","Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("locationName cannot be null or empty or blank",exceptionForLocationName3.getMessage());

        IllegalArgumentException exceptionForZipcode1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu",null,"Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("zipcode cannot be null or empty or blank",exceptionForZipcode1.getMessage());
        IllegalArgumentException exceptionForZipcode2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","","Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("zipcode cannot be null or empty or blank",exceptionForZipcode2.getMessage());
        IllegalArgumentException exceptionForZipcode3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu"," ","Timbuktu","TomorrowLand");
        });
        Assertions.assertEquals("zipcode cannot be null or empty or blank",exceptionForZipcode3.getMessage());

        IllegalArgumentException exceptionForProvince1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000",null,"TomorrowLand");
        });
        Assertions.assertEquals("province cannot be null or empty or blank",exceptionForProvince1.getMessage());
        IllegalArgumentException exceptionForProvince2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000","","TomorrowLand");
        });
        Assertions.assertEquals("province cannot be null or empty or blank",exceptionForProvince2.getMessage());
        IllegalArgumentException exceptionForProvince3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000"," ","TomorrowLand");
        });
        Assertions.assertEquals("province cannot be null or empty or blank",exceptionForProvince3.getMessage());

        IllegalArgumentException exceptionForCountry1 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000","Timbuktu",null);
        });
        Assertions.assertEquals("country cannot be null or empty or blank",exceptionForCountry1.getMessage());
        IllegalArgumentException exceptionForCountry2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000","Timbuktu","");
        });
        Assertions.assertEquals("country cannot be null or empty or blank",exceptionForCountry2.getMessage());
        IllegalArgumentException exceptionForCountry3 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.addLocation("Timbuktu","000000","Timbuktu"," ");
        });
        Assertions.assertEquals("country cannot be null or empty or blank",exceptionForCountry3.getMessage());

    }
    @Test
    public void testRemoveLocation(){
        Assertions.assertTrue(locationService.remove(location));
    }

    @Test
    public void testInvalidArgumentsRemoveLocation(){
        IllegalArgumentException exceptionForMerchant = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            locationService.remove(null);
        });
        Assertions.assertEquals("location cannot be null",exceptionForMerchant.getMessage());
    }

    /**
     * Unit test to fetch location bases on zipcode
     * @author Nilesh
     */
    @Test
    public void testGetLocationByZipCode(){
        when(mockedLocationRepository.findByZipcode("123ASD")).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(locationService.getLocationByZipCode("123ASD"));
    }

}
