package com.csci5308.w22.wiseshopping.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author Pavithra Gunasekaran
 */
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "product")
    private String product;

    @Column(name = "quantity_ordered")
    private int quantityOrdered;

    @Column(name = "price_each")
    private double priceEach;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "purchase_address")
    private String purchaseAddress;



    public Sales(String product) {
        this.product = product;
    }


}
