package org.global.housing.dto.response;

public class AgeItem {
    private Integer age;
    private Double population;

    public AgeItem() {}

    public AgeItem(Integer age, Double population) {
        this.age = age;
        this.population = population;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getPopulation() {
        return population;
    }

    public void setPopulation(Double population) {
        this.population = population;
    }
}

