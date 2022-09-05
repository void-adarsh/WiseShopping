package com.csci5308.w22.wiseshopping.repository.vendor;

import com.csci5308.w22.wiseshopping.models.vendor.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Elizabeth James
 */
@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    /**
     * find the location given the name, zipcode, province, countru
     * @param name
     * @param zipcode
     * @param province
     * @param country
     * @return location
     * @author: Elizabeth James
     */
    Location findByNameAndZipcodeAndProvinceAndCountry(String name, String zipcode, String province, String country);

    /**
     * find list of locations based on zipcode
     * @param zipcode
     * @return list of locations
     * @author: Elizabeth James
     */
    List<Location> findByZipcode(String zipcode);
}
