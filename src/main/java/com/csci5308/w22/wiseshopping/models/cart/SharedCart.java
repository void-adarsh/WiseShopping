package com.csci5308.w22.wiseshopping.models.cart;


import com.csci5308.w22.wiseshopping.models.User;
import lombok.*;

import javax.persistence.*;

/**
 * @author Adarsh Kannan
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "cart_shared")
public class SharedCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_shared_id")
    private int sharedCartId;

    @ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;

    public SharedCart(User user, Cart cart) {
        this.cart = cart;
        this.user = user;
    }
}