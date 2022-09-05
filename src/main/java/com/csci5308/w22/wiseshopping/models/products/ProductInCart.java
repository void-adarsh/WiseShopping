package com.csci5308.w22.wiseshopping.models.products;

import com.csci5308.w22.wiseshopping.models.cart.Cart;
import com.csci5308.w22.wiseshopping.models.vendor.Store;
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
public class ProductInCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_in_cart_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="cart_id", referencedColumnName = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="store_id", referencedColumnName = "store_id")
    private Store store;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private float price;

    public ProductInCart( Cart cart, Product product, Store store, int quantity, float price) {
        this.cart = cart;
        this.product = product;
        this.store = store;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductInCart(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }
}
