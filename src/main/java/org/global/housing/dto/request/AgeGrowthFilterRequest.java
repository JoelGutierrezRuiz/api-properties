package org.global.housing.dto.request;

public class AgeGrowthFilterRequest {

    private Boolean active;
    private Integer minAge;
    private Integer maxAge;
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

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
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

