package org.global.housing.dto.request;

public class GrowthFilterRequest {

    private Boolean active;
    private Integer yearStart;
    private Integer yearEnd;
    private Double percentMin;
    private Double percentMax;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getYearStart() {
        return yearStart;
    }

    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }

    public Integer getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
    }

    public Double getPercentMin() {
        return percentMin;
    }

    public void setPercentMin(Double percentMin) {
        this.percentMin = percentMin;
    }

    public Double getPercentMax() {
        return percentMax;
    }

    public void setPercentMax(Double percentMax) {
        this.percentMax = percentMax;
    }
}
