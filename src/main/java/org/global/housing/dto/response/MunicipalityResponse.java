package org.global.housing.dto.response;

import java.util.List;

public class MunicipalityResponse {
    String municipalityId;
    List<String> municipalitiesInRadio;

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public List<String> getMunicipalitiesInRadio() {
        return municipalitiesInRadio;
    }

    public void setMunicipalitiesInRadio(List<String> municipalitiesInRadio) {
        this.municipalitiesInRadio = municipalitiesInRadio;
    }
}
