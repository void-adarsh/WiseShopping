package com.csci5308.w22.wiseshopping.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Time;

/**
 * @author Elizabeth James
 */
public class UtilTest {
    @Test
    public void testParseTime(){
        Time expectedTime = new Time(11);
        Assertions.assertEquals(expectedTime.toString(),Util.parseTime("20").toString());
        Assertions.assertEquals(expectedTime.toString(),Util.parseTime("20:00").toString());
        Assertions.assertEquals(expectedTime.toString(),Util.parseTime("20:00:00").toString());
    }

    @Test
    public void testInvalidInputParameterParseTime(){
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Util.parseTime("11:00:00:00");
        });
        Assertions.assertEquals("Invalid argument for time.\n Expected format : HH:MM",exception.getMessage());

        IllegalArgumentException exception2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Util.parseTime("11d");
        });
        Assertions.assertEquals("Invalid argument for time. No other characters expect string is allowed.\n Expected format : HH:MM",exception2.getMessage());
    }
}
