package com.csci5308.w22.wiseshopping.models;

import com.csci5308.w22.wiseshopping.models.products.Product;
import lombok.*;

import javax.persistence.*;

/**
 * @author Elizabeth James
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class Subscription {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "subscrptn_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private Product product;

    private float priceAlert;

    public Subscription(Product product, User user) {
        this.product = product;
        this.user = user;
    }
}
