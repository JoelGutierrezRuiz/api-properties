package org.global.housing.dto.response;
import java.util.List;

public class PublicAuctionDto {

    private String id;
    private int tipo;
    private String subasta;
    private int derecho;
    private double porcTitularidad;
    private int codProvincia;
    private String direccion;
    private Double valoracion;
    private Double cargas;
    private String finSubasta;
    private String descripcion;
    private String cru;
    private Boolean capital;
    private Integer interes;
    private Integer municipioCod;
    private Integer cp;
    private List<String> fotos;
    private String refCatastro;
    private Double gpsLat;
    private Double gpsLong;

    // Getters y setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getTipo() { return tipo; }
    public void setTipo(int tipo) { this.tipo = tipo; }

    public String getSubasta() { return subasta; }
    public void setSubasta(String subasta) { this.subasta = subasta; }

    public int getDerecho() { return derecho; }
    public void setDerecho(int derecho) { this.derecho = derecho; }

    public double getPorcTitularidad() { return porcTitularidad; }
    public void setPorcTitularidad(double porcTitularidad) { this.porcTitularidad = porcTitularidad; }

    public int getCodProvincia() { return codProvincia; }
    public void setCodProvincia(int codProvincia) { this.codProvincia = codProvincia; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getValoracion() { return valoracion; }
    public void setValoracion(Double valoracion) { this.valoracion = valoracion; }

    public Double getCargas() { return cargas; }
    public void setCargas(Double cargas) { this.cargas = cargas; }

    public String getFinSubasta() { return finSubasta; }
    public void setFinSubasta(String finSubasta) { this.finSubasta = finSubasta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCru() { return cru; }
    public void setCru(String cru) { this.cru = cru; }

    public Boolean getCapital() { return capital; }
    public void setCapital(Boolean capital) { this.capital = capital; }

    public Integer getInteres() { return interes; }
    public void setInteres(Integer interes) { this.interes = interes; }

    public Integer getMunicipioCod() { return municipioCod; }
    public void setMunicipioCod(Integer municipioCod) { this.municipioCod = municipioCod; }

    public Integer getCp() { return cp; }
    public void setCp(Integer cp) { this.cp = cp; }

    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }

    public String getRefCatastro() { return refCatastro; }
    public void setRefCatastro(String refCatastro) { this.refCatastro = refCatastro; }

    public Double getGpsLat() { return gpsLat; }
    public void setGpsLat(Double gpsLat) { this.gpsLat = gpsLat; }

    public Double getGpsLong() { return gpsLong; }
    public void setGpsLong(Double gpsLong) { this.gpsLong = gpsLong; }

    @Override
    public String toString() {
        return "AuctionDto{" +
                "id='" + id + '\'' +
                ", tipo=" + tipo +
                ", subasta='" + subasta + '\'' +
                ", derecho=" + derecho +
                ", porcTitularidad=" + porcTitularidad +
                ", codProvincia=" + codProvincia +
                ", direccion='" + direccion + '\'' +
                ", valoracion=" + valoracion +
                ", cargas=" + cargas +
                ", finSubasta='" + finSubasta + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cru='" + cru + '\'' +
                ", capital=" + capital +
                ", interes=" + interes +
                ", municipioCod=" + municipioCod +
                ", cp=" + cp +
                ", fotos=" + fotos +
                ", refCatastro='" + refCatastro + '\'' +
                ", gpsLat=" + gpsLat +
                ", gpsLong=" + gpsLong +
                '}';
    }
}
