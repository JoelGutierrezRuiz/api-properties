package org.global.housing.entity.municipality;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserFavoriteMunicipalityId implements Serializable {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "MUNICIPALITY_ID", length = 5)
    private String municipalityId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }
}
