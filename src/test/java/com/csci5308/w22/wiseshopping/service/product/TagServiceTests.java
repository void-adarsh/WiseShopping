package com.csci5308.w22.wiseshopping.service.product;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import com.csci5308.w22.wiseshopping.service.products.TagsService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author Pavithra Gunasekaran
 * contributor Nilesh
 */
public class TagServiceTests {

    @Mock
    TagsRepository mockedTagsRepository;


    TagsService tagsService;

    //private Tags tags;

    /**
     * TODO fix this test
     */
   public void testGetProducts(){
        List<Tags> productsByTags = tagsService.getProducts("milk");
        Assertions.assertTrue(tagsService.getProducts("milk").size()>0);
    }
}
