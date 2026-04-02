package org.global.housing.dto.response;

import java.util.List;

public class YearAges {
    private Integer year;
    private List<AgeItem> ages;

    public YearAges() {}

    public YearAges(Integer year, List<AgeItem> ages) {
        this.year = year;
        this.ages = ages;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<AgeItem> getAges() {
        return ages;
    }

    public void setAges(List<AgeItem> ages) {
        this.ages = ages;
    }
}

