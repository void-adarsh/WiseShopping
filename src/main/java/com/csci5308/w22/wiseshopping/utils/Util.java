package com.csci5308.w22.wiseshopping.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Time;

/**
 * @author Elizabeth James
 */
public class Util {
    /**
     * this method parse the time passed as string
     * Accepts date in HH:MM:SS or HH:MM or HH format
     *
     * @param time time as string
     * @return Time object
     * @author: Elizabeth James
     */
    public static Time parseTime(String time) {

        if (time.split(":").length == 1) {
            time = time + ":00:00";
        } else if (time.split(":").length == 2) {
            time = time + ":00";
        } else if (time.split(":").length == 3) {
            // do nothing
        }
        // throw exception if any other format is passed
        else {
            throw new IllegalArgumentException("Invalid argument for time.\n Expected format : HH:MM");
        }
        try{
            return Time.valueOf(time);
        }
        catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid argument for time. No other characters expect string is allowed.\n Expected format : HH:MM");
        }
    }


    public static boolean isValidString(String name) {
        return name!=null && !name.isEmpty() && !name.isBlank();
    }

    /**
     * this encodes the password using sha 256 algorithm
     * @param password password
     * @return encoded password
     * @author: Elizabeth James
     */
    public static String encode(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
