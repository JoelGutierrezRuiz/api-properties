package org.global.housing.entity.municipality;

import java.io.Serializable;
import java.util.Objects;

public class PoblacionMunicipalId implements Serializable {
    private String municipioId;
    private Integer sexoId;
    private Integer edadNum;
    private Integer anio;

    public PoblacionMunicipalId() {}

    public PoblacionMunicipalId(String municipioId, Integer sexoId, Integer edadNum, Integer anio) {
        this.municipioId = municipioId;
        this.sexoId = sexoId;
        this.edadNum = edadNum;
        this.anio = anio;
    }

    // getters y setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoblacionMunicipalId that = (PoblacionMunicipalId) o;
        return Objects.equals(municipioId, that.municipioId) && Objects.equals(sexoId, that.sexoId) && Objects.equals(edadNum, that.edadNum) && Objects.equals(anio, that.anio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(municipioId, sexoId, edadNum, anio);
    }
}

