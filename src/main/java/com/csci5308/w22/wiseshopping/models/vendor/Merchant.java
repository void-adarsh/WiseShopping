package com.csci5308.w22.wiseshopping.models.vendor;

import com.csci5308.w22.wiseshopping.utils.Util;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.security.Timestamp;

/**
 * @author Elizabeth James
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "merchant_details")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private int id;

    @Column(name =  "merchant_name")
    private String name;

    @Column(name =   "password")
    private String password;

    @Column(name =  "email")
    private String email;

    @Column(name = "security_code")
    private String security_code;


    public Merchant(String merchantName, String email , String password) {
        this.name = merchantName;
        this.password = Util.encode(password);
        this.email = email;
    }
    public Merchant(String merchantName, String email , String password, String securityCode) {
        this.name = merchantName;
        this.password = Util.encode(password);
        this.email = email;
        this.security_code = Util.encode(securityCode);
    }

    public void setPassword(String password) {
        this.password = Util.encode(password);
    }

    public void setSecurity_code(String security_code) {
        this.security_code = Util.encode(security_code);
    }

}
