package org.global.housing.dto.request;

public class FiltersRequest {

    private PopulationFilterRequest population;
    private RadiusPopulationFilterRequest radiusPopulation;
    private GrowthFilterRequest growth;
    private AgeGrowthFilterRequest ageGrowth; // ...existing code... add new DTO field

    public PopulationFilterRequest getPopulation() {
        return population;
    }

    public void setPopulation(PopulationFilterRequest population) {
        this.population = population;
    }

    public RadiusPopulationFilterRequest getRadiusPopulation() {
        return radiusPopulation;
    }

    public void setRadiusPopulation(RadiusPopulationFilterRequest radiusPopulation) {
        this.radiusPopulation = radiusPopulation;
    }

    public GrowthFilterRequest getGrowth() {
        return growth;
    }

    public void setGrowth(GrowthFilterRequest growth) {
        this.growth = growth;
    }

    public AgeGrowthFilterRequest getAgeGrowth() {
        return ageGrowth;
    }

    public void setAgeGrowth(AgeGrowthFilterRequest ageGrowth) {
        this.ageGrowth = ageGrowth;
    }
}
