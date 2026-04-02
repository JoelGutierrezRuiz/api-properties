package org.global.housing.entity.municipality;

import jakarta.persistence.*;
import org.global.housing.entity.login.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_FAVORITE_MUNICIPALITIES")
public class UserFavoriteMunicipalityEntity {

    @EmbeddedId
    private UserFavoriteMunicipalityId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @MapsId("municipalityId")
    @JoinColumn(name = "MUNICIPALITY_ID")
    private MunicipalityEntity municipality;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    public UserFavoriteMunicipalityId getId() {
        return id;
    }

    public void setId(UserFavoriteMunicipalityId id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MunicipalityEntity getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityEntity municipality) {
        this.municipality = municipality;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
