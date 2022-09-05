package com.csci5308.w22.wiseshopping.repository.product;

import com.csci5308.w22.wiseshopping.models.cart.Tags;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Nilesh
 */
@Repository
public interface TagsRepository extends CrudRepository<Tags,Integer> {

    //Tags
    @Query(value = "SELECT * FROM tags WHERE product_id = ?1", nativeQuery = true)
    List<Tags> findByProductId(int productId);

    /**
     * Get the tags based on the tag name
     * @param tagName
     * @return tags
     * @author: Pavithra Gunasekaran
     */
    @Query(value = "SELECT * FROM tags WHERE tag_name LIKE %?1%", nativeQuery = true)
    List<Tags> findByTagName(String tagName);

    /**
     * Get the tags based on the tag id
     * @param id
     * @return tags
     * @author: Pavithra Gunasekaran
     */
    @Query(value = "SELECT * from tags where tag_id = ?1",nativeQuery = true)
    Tags findById(int id);
}
