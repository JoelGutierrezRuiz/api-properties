package org.global.housing.dto.response;

import java.util.List;

public class FilterResponse {
    private List<String> municipalityIds;
    private List<MunicipalityResponse> municipalities; // compatibilidad

    public List<String> getMunicipalityIds() {
        return municipalityIds;
    }

    public void setMunicipalityIds(List<String> municipalityIds) {
        this.municipalityIds = municipalityIds;
    }

    public List<MunicipalityResponse> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<MunicipalityResponse> municipalities) {
        this.municipalities = municipalities;
    }
}
