package com.csci5308.w22.wiseshopping.models.products;

import com.csci5308.w22.wiseshopping.models.vendor.Store;
import lombok.*;

import javax.persistence.*;

/**
 * @author Nilesh
*/
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private int inventoryId;

    @ManyToOne
    @JoinColumn(name="store_id", referencedColumnName = "store_id")
    private Store store;



    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private Product product;


    @Column(name = "price")
    private float price;

    @Column(name = "stock")
    private int stock;


    public ProductInventory(Store store, Product product, float price, int stock) {
        this.store = store;
        this.product = product;
        this.price = price;
        this.stock = stock;
    }

}
