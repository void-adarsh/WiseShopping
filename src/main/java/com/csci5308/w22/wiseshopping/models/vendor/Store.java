package com.csci5308.w22.wiseshopping.models.vendor;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;

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
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private int id;

    @Column(name = "store_name")
    private String name;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    @Column(name = "type_of_business")
    private String type;

    private String contact;

    @ManyToOne
    @JoinColumn(name="location_id", referencedColumnName = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name="merchant_id", referencedColumnName = "merchant_id")
    private Merchant merchant;


    public Store(String storeName, Time startTime, Time endTime, String type, String contact, Location location, Merchant merchant) {
        this.name = storeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.contact = contact;
        this.location = location;
        this.merchant = merchant;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
