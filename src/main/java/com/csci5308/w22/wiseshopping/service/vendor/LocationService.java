package com.csci5308.w22.wiseshopping.service.vendor;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import com.csci5308.w22.wiseshopping.repository.vendor.LocationRepository;
import com.csci5308.w22.wiseshopping.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * this class acts like a service for location
 * @author Elizabeth James
 */
@Service
public class LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationRepository locationRepository;

    /**
     * inserts location to table
     * @param name name of location
     * @param zipcode zip code of location
     * @param province province
     * @param country country
     * @return true, if successful; else false
     * @author: Elizabeth James
     */
    @Transactional
    public Location addLocation(String name, String zipcode, String province, String country){

        if (!Util.isValidString(name)){
            throw new IllegalArgumentException("locationName cannot be null or empty or blank");
        }

        if (!Util.isValidString(zipcode)){
            throw new IllegalArgumentException("zipcode cannot be null or empty or blank");
        }
        if (!Util.isValidString(province)){
            throw new IllegalArgumentException("province cannot be null or empty or blank");
        }
        if (!Util.isValidString(country)){
            throw new IllegalArgumentException("country cannot be null or empty or blank");
        }


        Location location = locationRepository.findByNameAndZipcodeAndProvinceAndCountry(name,zipcode, province, country);
        if (location == null) {
            location = new Location(name, zipcode, province, country);
            locationRepository.save(location);
            LOGGER.info("Location {} is added",location.getName());
        }
        else {
            LOGGER.warn("Location with the same values exist in DB already...");
        }

        return location;
    }



    /**
     * deletes location from table
     * @param location location
     * @return true, if deleted; else false
     *  @author: Elizabeth James
     */
    @Transactional
    public boolean remove(Location location) {
        if (location == null){
            throw new IllegalArgumentException("location cannot be null");
        }
        locationRepository.delete(location);
        return true;
    }

    /**
     * gets the lists of locations having the given zipcode
     * @param zipcode
     * @return list of locations
     *  @author: Elizabeth James
     */
    public List<Location> getLocationByZipCode(String zipcode) {
        return locationRepository.findByZipcode(zipcode);
    }


}
