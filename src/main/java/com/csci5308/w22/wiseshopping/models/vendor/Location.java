package com.csci5308.w22.wiseshopping.models.vendor;

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
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private int id;

    @Column(name = "location_name")
    private String name;

    private String zipcode;

    private String province;

    private String country;

    public Location(String name, String zipcode, String province, String country) {
        this.name = name;
        this.zipcode = zipcode;
        this.province = province;
        this.country = country;
    }

}
