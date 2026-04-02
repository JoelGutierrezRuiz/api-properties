package org.global.housing.entity.municipality;

import jakarta.persistence.*;

@Entity
@Table(name = "MUNICIPALITY_POPULATION")
public class MunicipalityPopulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private  Integer id;

    @Column(name = "MUNICIPALITY_ID")
    private  String municipalityId;

    @Column(name = "POPULATION")
    private Integer population;

    @Column(name = "YEAR")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MUNICIPALITY_ID", insertable = false, updatable = false)
    private MunicipalityEntity municipality;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(String municipalityId) {
        this.municipalityId = municipalityId;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
