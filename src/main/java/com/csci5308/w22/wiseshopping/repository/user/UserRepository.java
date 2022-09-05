package com.csci5308.w22.wiseshopping.repository.user;

import com.csci5308.w22.wiseshopping.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Pavithra Gunasekaran
 */
@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    /**
     * Get the user based on email
     * @param email
     * @return user
     * @author: Pavithra Gunasekaran
     */
    User findByEmail(String email);

    /**
     * Get the user based on email and password
     * @param email, password
     * @return user
     * @author: Pavithra Gunasekaran
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Delete the user based on the email id
     * @param email
     * @return user
     * @author: Pavithra Gunasekaran
     */

    Integer deleteByEmail(String email);


}
