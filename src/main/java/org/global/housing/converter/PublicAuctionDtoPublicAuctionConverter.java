package org.global.housing.converter;

import org.global.housing.dto.response.PublicAuctionDto;
import org.global.housing.enums.PublicAuctionTypes;
import org.global.housing.model.PublicAuction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@Component
public class PublicAuctionDtoPublicAuctionConverter {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger log = LoggerFactory.getLogger(PublicAuctionDtoPublicAuctionConverter.class);

    public static PublicAuction convert(PublicAuctionDto dto) {

        if(Objects.isNull(dto) || Objects.isNull(dto.getId())){
            System.out.println(dto.toString());
            return null;
        }

        if(Objects.isNull(dto.getCru())){
            System.out.println(dto.toString());
            return null;
        }

        PublicAuction pa = new PublicAuction();
        pa.setId(dto.getId());
        pa.setOwnerPercentage(dto.getPorcTitularidad());
        pa.setProvinceId(dto.getCodProvincia());
        pa.setAddress(dto.getDireccion());
        pa.setPrice(dto.getValoracion());
        pa.setLoads(dto.getCargas());

        try {
            if (dto.getFinSubasta() != null) {
                pa.setEndingDate(DATE_FORMAT.parse(dto.getFinSubasta()));
            }
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date: " + dto.getFinSubasta(), e);
        }

        pa.setDescription(dto.getDescripcion());
        pa.setType(PublicAuctionTypes.fromCode(dto.getTipo()).getDisplayName());
        pa.setMunicipalityId(Objects.nonNull(dto.getCru())?dto.getCru().substring(0, 5):null);
        pa.setLatitude(dto.getGpsLat());
        pa.setLongitude(dto.getGpsLong());

        if (dto.getFotos() != null) {
            List<String> fullUrls = dto.getFotos().stream()
                    .map(image -> "https://www2.agenciatributaria.gob.es/static_files/common/internet/dep/taiif/subastaInmuebles/data2/"
                            + dto.getId() +"/"+ image)
                    .toList();

            pa.setPhotos(fullUrls);
        }

        // Generar URL de detalle
        pa.setUrl("https://www2.agenciatributaria.gob.es/static_files/common/internet/dep/taiif/subastaInmuebles/index.html#/inmueble/" + dto.getId());

        return pa;
    }
}
