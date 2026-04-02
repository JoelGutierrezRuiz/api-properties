package org.global.housing.entity.municipality;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "MUNICIPALITIES")
public class MunicipalityEntity {
    @Id
    @Column(name = "MUNICIPALITY_ID")
    private String municipalityId;
    @Column(name = "CMUN")
    private String municipalityCode;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PROVINCE_ID")
    private String provinceId;
    @Column(name = "LATITUDE")
    private Float latitude;
    @Column(name = "LONGITUDE")
    private Float longitude;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUNICIPALITY_ID", referencedColumnName = "MUNICIPALITY_ID", insertable = false, updatable = false)
    private List<MunicipalityPopulationEntity> populations;



    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public String getName() {
        return name;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }
}
