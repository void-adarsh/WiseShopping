package com.csci5308.w22.wiseshopping.models;
import com.csci5308.w22.wiseshopping.utils.Util;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import javax.persistence.*;
import java.sql.Timestamp;
/**
 * @author Pavithra Gunasekaran
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Getter
@Setter
@Table(name = "user_details")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "contact")
    private String contact;
    @Column(name = "register_at" ,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp register_at;
    @Column(name = "security_code")
    private String security_code;

    public User(int userId) {
        this.id = userId;
    }


    public User(String firstName, String lastName, String email, String password, String contact, String security_code) {
        this.email = email;
        this.password = Util.encode(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.security_code=Util.encode(security_code);
    }

    public void setPassword(String password) {
        this.password = Util.encode(password);
    }

    public void setSecurity_code(String security_code) {
        this.security_code = Util.encode(security_code);
    }
}