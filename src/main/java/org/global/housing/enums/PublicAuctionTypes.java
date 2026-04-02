package org.global.housing.enums;

public enum PublicAuctionTypes {
    VIVIENDA(1),
    Local(2),
    Garaje(3),
    Trastero(4),
    NaveIndustrial(5),
    Solar(6),
    FincaRustica(7),
    Otros(99);

    private final int code;

    PublicAuctionTypes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PublicAuctionTypes fromCode(int code) {
        for (PublicAuctionTypes t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de subasta desconocido: " + code);
    }

    public String getDisplayName() {
        return switch (this) {
            case VIVIENDA -> "Vivienda";
            case Local -> "Local";
            case Garaje -> "Garaje";
            case Trastero -> "Trastero";
            case NaveIndustrial -> "Nave industrial";
            case Solar -> "Solar";
            case FincaRustica -> "Finca rústica";
            case Otros -> "Otros";
        };
    }


}
