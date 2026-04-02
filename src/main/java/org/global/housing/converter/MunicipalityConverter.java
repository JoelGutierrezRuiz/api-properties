package org.global.housing.converter;

import org.global.housing.entity.municipality.MunicipalityEntity;
import org.global.housing.model.MunicipalityDto;
import org.springframework.stereotype.Component;

@Component
public class MunicipalityConverter {

    public MunicipalityDto convertEntityToDto(MunicipalityEntity municipalityEntity) {
        MunicipalityDto municipalityDTO = new MunicipalityDto();
        municipalityDTO.setMunicipalityId(municipalityEntity.getMunicipalityId());
        municipalityDTO.setName(municipalityEntity.getName());
        municipalityDTO.setLatitude(municipalityEntity.getLatitude());
        municipalityDTO.setLongitude(municipalityEntity.getLongitude());
        municipalityDTO.setProvinceId(municipalityEntity.getProvinceId());
        return municipalityDTO;
    }

    public MunicipalityDto convertPopulationToDto(MunicipalityEntity municipalityEntity, int population) {
        MunicipalityDto municipalityDTO = this.convertEntityToDto(municipalityEntity);
        municipalityDTO.setPopulation(population);
        return municipalityDTO;
    }

}
