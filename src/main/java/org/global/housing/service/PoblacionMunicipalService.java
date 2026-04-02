package org.global.housing.service;

import org.global.housing.dto.response.AgeItem;
import org.global.housing.dto.response.MunicipalityAgeResponse;
import org.global.housing.dto.response.YearAges;
import org.global.housing.entity.municipality.MunicipalityEntity;
import org.global.housing.entity.municipality.PoblacionMunicipalEntity;
import org.global.housing.model.MunicipalityDto;
import org.global.housing.repository.MunicipalityRepository;
import org.global.housing.repository.PoblacionMunicipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PoblacionMunicipalService {

    @Autowired
    PoblacionMunicipalRepository poblacionRepo;

    @Autowired
    MunicipalityRepository municipalityRepository;

    public MunicipalityAgeResponse getAgesByMunicipioId(String municipioId) {
        List<PoblacionMunicipalEntity> rows = poblacionRepo.findByMunicipioIdOrdered(municipioId);

        // Agrupar por año conservando orden
        Map<Integer, List<PoblacionMunicipalEntity>> byYear = rows.stream()
                .collect(Collectors.groupingBy(PoblacionMunicipalEntity::getAnio, LinkedHashMap::new, Collectors.toList()));

        List<YearAges> ageData = new ArrayList<>();

        for (Map.Entry<Integer, List<PoblacionMunicipalEntity>> e : byYear.entrySet()) {
            Integer year = e.getKey();

            // Agrupar por edad y sumar la población (unificar SEXO_ID)
            Map<Integer, Double> ageSum = e.getValue().stream()
                    .collect(Collectors.groupingBy(
                            PoblacionMunicipalEntity::getEdadNum,
                            TreeMap::new, // ordenar por edad
                            Collectors.summingDouble(r -> r.getPoblacion() == null ? 0.0 : r.getPoblacion())
                    ));

            List<AgeItem> ages = ageSum.entrySet().stream()
                    .map(entry -> new AgeItem(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            ageData.add(new YearAges(year, ages));
        }

        String name = null;
        Optional<MunicipalityEntity> me = municipalityRepository.findById(municipioId);
        if (me.isPresent()) {
            name = me.get().getName();
        }

        MunicipalityAgeResponse resp = new MunicipalityAgeResponse();
        resp.setMunicipalityId(municipioId);
        resp.setName(name);
        resp.setAgeData(ageData);
        return resp;
    }
}
