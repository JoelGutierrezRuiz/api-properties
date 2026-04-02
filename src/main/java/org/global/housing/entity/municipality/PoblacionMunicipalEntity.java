package org.global.housing.entity.municipality;

import jakarta.persistence.*;

@Entity
@Table(name = "POBLACION_MUNICIPAL")
@IdClass(PoblacionMunicipalId.class)
public class PoblacionMunicipalEntity {

    @Id
    @Column(name = "MUNICIPIO_ID")
    private String municipioId;

    @Id
    @Column(name = "SEXO_ID")
    private Integer sexoId;

    @Id
    @Column(name = "EDAD_NUM")
    private Integer edadNum;

    @Id
    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "POBLACION")
    private Double poblacion;

    public String getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(String municipioId) {
        this.municipioId = municipioId;
    }

    public Integer getSexoId() {
        return sexoId;
    }

    public void setSexoId(Integer sexoId) {
        this.sexoId = sexoId;
    }

    public Integer getEdadNum() {
        return edadNum;
    }

    public void setEdadNum(Integer edadNum) {
        this.edadNum = edadNum;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Double getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Double poblacion) {
        this.poblacion = poblacion;
    }
}

