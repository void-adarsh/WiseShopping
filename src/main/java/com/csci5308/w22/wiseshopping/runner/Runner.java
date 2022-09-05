package com.csci5308.w22.wiseshopping.runner;

import com.csci5308.w22.wiseshopping.factory.ScreenFactory;

import com.csci5308.w22.wiseshopping.screens.signin.LoginScreen;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author Elizabeth James
 */
@Slf4j
public class Runner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);


    @Autowired
    private LoginScreen loginScreen;
    @Autowired


    private ScreenFactory screenFactory;


    /**
     * login screen should be the first screen that comes up when the application is run
     * @param args
     * @author: Elizabeth James
     */
    @Override
    public void run(String... args) {

       loginScreen.render(screenFactory);

    }

}
