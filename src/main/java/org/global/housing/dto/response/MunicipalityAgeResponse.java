package org.global.housing.dto.response;

import java.util.List;

public class MunicipalityAgeResponse {
    private String municipalityId;
    private String name;
    private List<YearAges> ageData;

    public MunicipalityAgeResponse() {}

    public MunicipalityAgeResponse(String municipalityId, String name, List<YearAges> ageData) {
        this.municipalityId = municipalityId;
        this.name = name;
        this.ageData = ageData;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YearAges> getAgeData() {
        return ageData;
    }

    public void setAgeData(List<YearAges> ageData) {
        this.ageData = ageData;
    }
}

