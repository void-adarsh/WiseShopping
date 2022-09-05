package com.csci5308.w22.wiseshopping.integrationTests.service.user;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.service.products.TagsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class TagsServiceTests {

    @Autowired
    TagsService tagsService;

    /**
     * Integration test for getting products based on the tags
     * @author Pavithra Gunasekaran
     */
    @Test
    public void testGetProducts(){
        List<Tags> expectedTags = tagsService.getProducts("milk");
        Assertions.assertTrue(expectedTags.size()>0);

    }
}
