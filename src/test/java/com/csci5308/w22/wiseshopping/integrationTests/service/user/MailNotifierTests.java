package com.csci5308.w22.wiseshopping.integrationTests.service.user;

import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.utils.MailNotifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Elizabeth James
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class MailNotifierTests {

    @Autowired
    MailNotifier mailNotifier;

    //This test is commented out as there is no smtp server on git ci cd runner
   // @Test
    public void testMailNotification(){
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setEmail("james.liz581996@gmail.com");
        userList.add(user);
        Assertions.assertTrue(mailNotifier.sendPriceAlerts(userList,"Dummy Product", "dummy store", (float) 1.99));
    }
}
