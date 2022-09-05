package com.csci5308.w22.wiseshopping.models.products;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;

/**
 * @author Nilesh
*/
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;


    public Product(String productName, String productDescription) {
        this.productName = productName;
        this.productDescription = productDescription;
    }

    public Product(int id) {
        this.productId =  id;
    }

    public Product(String name) {
        this.productName = name;
    }
}
