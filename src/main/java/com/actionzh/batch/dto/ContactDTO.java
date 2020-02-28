package com.actionzh.batch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode()
public class ContactDTO implements Serializable {

    private static final long serialVersionUID = 376003851514387310L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime lastUpdated;

    private Long ver;
    private Long id;
    private String anonymousId;
    private String name;
    private String nickname;

    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime dateOfBirthday;
    private String avatar;

    private String mobilePhone;
    private String homePhone;
    private String email;
    private String country;
    private String state;
    private String street;
    private String city;
    private String postalCode;
    private String comments;

    private String userName;
    private String title;
    private String website;
    private String company;
    private String industry;
    private String department;
    private String idCard;
    private Map<String, Object> props;

    private Long mergedId;
    private Boolean isAnonymous = false;

    private DateTime firstSeen;
    private DateTime lastSeen;
    private DateTime lastTagged;
    private DateTime identified;

    /**
     * This property is used for openApi
     */
    private List<ContactOpenId> openIds;

    @Data
    public static class ContactOpenId {
        private String channelId;
        private String channelName;
        private String openId;
    }

    public boolean setTimingIfValid(DateTime lastSeen, DateTime identified) {
        boolean dirty = false;
        if (lastSeen != null) {
            if (this.lastSeen == null || lastSeen.isAfter(this.lastSeen)) {
                this.lastSeen = lastSeen;
                dirty = true;
            }
        }
        if (identified != null) {
            if (this.identified == null || identified.isBefore(this.identified)) {
                this.identified = identified;
                dirty = true;
            }
        }
        return dirty;
    }


}


