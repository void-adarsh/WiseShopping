package com.csci5308.w22.wiseshopping.models.cart;

import com.csci5308.w22.wiseshopping.models.User;
import com.csci5308.w22.wiseshopping.utils.Constants;
import lombok.*;

import javax.persistence.*;

/**
 * @author Pavithra Gunasekaran
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;


    @Column(name = "shared_status")
    private String sharedStatus;

    @Column
    private String status;

    public Cart(User user,String status) {
        this.user = user;
        this.sharedStatus =status;
        this.status = Constants.INPROGRESS;
    }

}