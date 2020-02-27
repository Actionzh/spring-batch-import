package com.actionzh.batch.dto;

import com.actionzh.utils.JodaTimeConverter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by songfan on 17/4/23.
 */
@Table(name = "contact")
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
public class Contact implements Serializable {
    private static final long serialVersionUID = 2388135703541029496L;

    public static final String GENDER_TYPE_UNKNOWN = "unknown";
    public static final String GENDER_TYPE_MALE = "male";
    public static final String GENDER_TYPE_FEMALE = "female";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anonymous_id")
    private String anonymousId;
    @Column(name = "name")
    private String name;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "gender")
    private String gender = Contact.GENDER_TYPE_UNKNOWN;
    @Column(name = "date_of_birthday")
    private DateTime dateOfBirthday;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "home_phone")
    private String homePhone;
    @Column(name = "email")
    private String email;
    @Column(name = "country")
    private String country;
    @Column(name = "state")
    private String state;
    @Column(name = "street")
    private String street;
    @Column(name = "city")
    private String city;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "comments")
    private String comments;
    //User’s username. This should be unique to each user, like the usernames of Twitter or GitHub.
    @Column(name = "user_name")
    private String userName;
    @Column(name = "title")
    private String title;
    @Column(name = "website")
    private String website;
    @Column(name = "company")
    private String company;
    @Column(name = "industry")
    private String industry;
    @Column(name = "department")
    private String department;
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "merged_id")
    private Long mergedId;
    @Column(name = "version")
    @Version
    private Long version;
    @CreatedDate
    @Convert(converter = JodaTimeConverter.class)
    @Column(name = "date_created", updatable = false)
    private DateTime dateCreated;

    @LastModifiedDate
    @Convert(converter = JodaTimeConverter.class)
    @Column(name = "last_updated", updatable = false)
    private DateTime lastUpdated;
}
