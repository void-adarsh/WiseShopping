package com.csci5308.w22.wiseshopping.models.cart;

import com.csci5308.w22.wiseshopping.models.products.Product;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

import javax.persistence.*;

//Tags

/**
 * @author Nilesh
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
@NoArgsConstructor
public class Tags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int tagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName = "product_id")

    private Product product;

    @Column(name = "tag_name")
    private String tagName;


    public Tags(Product product, String tagName) {
        this.product= product;
        this.tagName = tagName;
    }

}
