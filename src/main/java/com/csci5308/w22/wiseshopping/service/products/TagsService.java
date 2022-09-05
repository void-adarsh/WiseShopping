package com.csci5308.w22.wiseshopping.service.products;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import com.csci5308.w22.wiseshopping.repository.product.TagsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Pavithra Gunasekaran
 */
@Service
public class TagsService {

    @Autowired
    private TagsRepository tagsRepository;

    public List<Tags> getProducts(String tagName){
        return  tagsRepository.findByTagName(tagName);
    }

}
