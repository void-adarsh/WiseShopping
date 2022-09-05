package com.csci5308.w22.wiseshopping.utils;

import com.csci5308.w22.wiseshopping.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Elizabeth James
 */
@Component
public class MailNotifier {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * this method sends mails notifying about the price alerts to the users
     * @param userList
     * @param productName
     * @param storeName
     * @param price
     * @return true on sucess
     * @author: Elizabeth James
     */
    public boolean sendPriceAlerts(List<User> userList, String productName, String storeName, float price) {
        boolean success = false;
        SimpleMailMessage msg = new SimpleMailMessage();
        for (User user : userList) {
            msg.setTo(user.getEmail());
            msg.setSubject("Price drop in " + productName + "!");
            msg.setText("Prices for " + productName + " has dropped to " + price + ". Check your nearest store - " + storeName);

            javaMailSender.send(msg);
        }
        success = true;
        return success;

    }

}