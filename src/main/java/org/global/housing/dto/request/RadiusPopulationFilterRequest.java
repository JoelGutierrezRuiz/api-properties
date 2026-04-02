package org.global.housing.dto.request;

public class RadiusPopulationFilterRequest {

    private Boolean active;
    private Integer totalMin;
    private Integer totalMax;
    private Integer nearbyMin;
    private Integer nearbyMax;
    private Integer km;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getTotalMin() {
        return totalMin;
    }

    public void setTotalMin(Integer totalMin) {
        this.totalMin = totalMin;
    }

    public Integer getTotalMax() {
        return totalMax;
    }

    public void setTotalMax(Integer totalMax) {
        this.totalMax = totalMax;
    }

    public Integer getNearbyMin() {
        return nearbyMin;
    }

    public void setNearbyMin(Integer nearbyMin) {
        this.nearbyMin = nearbyMin;
    }

    public Integer getNearbyMax() {
        return nearbyMax;
    }

    public void setNearbyMax(Integer nearbyMax) {
        this.nearbyMax = nearbyMax;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }
}
