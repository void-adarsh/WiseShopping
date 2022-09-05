package com.csci5308.w22.wiseshopping.models.products;

import lombok.*;

import javax.persistence.*;

/**
 * @author Nilesh
*/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    private int productCategoryId;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "product_category_name")
    private String categoryName;

    @Column(name = "product_category_description")
    private String categoryDesc;

    public ProductCategory(Product product, String name, String desc) {
        this.product = product;
        this.categoryName = name;
        this.categoryDesc = desc;
    }

}
