package org.global.housing.service;

import org.global.housing.converter.MunicipalityConverter;
import org.global.housing.dto.request.FiltersRequest;
import org.global.housing.dto.request.AgeGrowthFilterRequest;
import org.global.housing.dto.response.FilterResponse;
import org.global.housing.dto.response.MunicipalityResponse;
import org.global.housing.entity.municipality.MunicipalityEntity;
import org.global.housing.model.MunicipalityDto;
import org.global.housing.model.MunicipalitySpecs;
import org.global.housing.repository.PoblacionMunicipalRepository;
import org.global.housing.repository.MunicipalityPopulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.global.housing.repository.MunicipalityRepository;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MunicipalityService {

    private static final Logger logger = LoggerFactory.getLogger(MunicipalityService.class);

    @Autowired
    MunicipalityRepository municipalityRepository;

    @Autowired
    MunicipalityPopulationRepository municipalityPopulationRepository;

    @Autowired
    PoblacionMunicipalRepository poblacionMunicipalRepository; // repo for POBLACION_MUNICIPAL

    @Autowired
    MunicipalityConverter municipalityConverter;

    public List<MunicipalityDto> getAllMunicipalities() {
        return municipalityRepository.findAll().stream()
                .map(municipalityConverter::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public Map<Integer,Integer> getAllYearPopulationById(String id) {
        List<Object[]> rows = municipalityPopulationRepository.findPopulationByMunicipalityId(id);

        return rows.stream()
                .collect(Collectors.toMap(
                        r -> (Integer) r[0], // year
                        r -> (Integer) r[1]  // population
                ));
    }

    public List<MunicipalityDto> getAllMunicipalitiesWithPopulationByYear(int year) {

        List<MunicipalityEntity> municipalities = municipalityRepository.findAll();

        List<Object[]> populations = municipalityPopulationRepository.findPopulationByYear(year);

        Map<String, Integer> populationMap = populations.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],   // municipalityId
                        row -> (Integer) row[1]   // population
                ));

        return municipalities.stream()
                .map(municipality -> {
                    Integer population = populationMap.get(municipality.getMunicipalityId());
                    if (Objects.nonNull(population)) {
                        return municipalityConverter.convertPopulationToDto(municipality, population);
                    } else {
                        return municipalityConverter.convertEntityToDto(municipality);
                    }
                })
                .collect(Collectors.toList());
    }

    public FilterResponse filter(FiltersRequest filtersRequest) {

        Specification<MunicipalityEntity> spec = (root, query, cb) -> null;

        // Año anterior al actual
        int previousYear = Year.now().getValue() - 1;

        // Filtro de población
        if (filtersRequest.getPopulation() != null &&
                Boolean.TRUE.equals(filtersRequest.getPopulation().getActive())) {

            spec = spec.and(
                    MunicipalitySpecs.population(filtersRequest.getPopulation(), previousYear)
            );
        }

        if (filtersRequest.getGrowth() != null &&
                Boolean.TRUE.equals(filtersRequest.getGrowth().getActive())) {

            spec = spec.and(
                    MunicipalitySpecs.growth(filtersRequest.getGrowth())
            );
        }

        // NOTE: We no longer add MunicipalitySpecs.ageGrowth here because that uses total population,
        // and ageGrowth must be calculated from POBLACION_MUNICIPAL (by ages). We'll apply ageGrowth
        // as a post-filter after we have the initial list of candidates.

        // radiusPopulation (si está activo)
        if (filtersRequest.getRadiusPopulation() != null &&
                Boolean.TRUE.equals(filtersRequest.getRadiusPopulation().getActive())) {
            // actualmente no implementada la lógica de radio; se mantiene placeholder
            // podríamos añadir spec = spec.and(MunicipalitySpecs.radiusPopulation(...)) cuando exista
        }

        List<MunicipalityEntity> filtered = municipalityRepository.findAll(spec);

        // Si ageGrowth está activo, aplicar post-filtrado usando la tabla POBLACION_MUNICIPAL
        if (filtersRequest.getAgeGrowth() != null && Boolean.TRUE.equals(filtersRequest.getAgeGrowth().getActive())) {
            AgeGrowthFilterRequest ag = filtersRequest.getAgeGrowth();

            // Normalizar años a 2021..2025 (misma lógica que en MunicipalitySpecs)
            final int AGE_YEAR_MIN = 2021;
            final int AGE_YEAR_MAX = 2025;
            int ys = Objects.nonNull(ag.getYearStart()) ? ag.getYearStart() : AGE_YEAR_MIN;
            if (ys < AGE_YEAR_MIN) ys = AGE_YEAR_MIN;
            if (ys > AGE_YEAR_MAX) ys = AGE_YEAR_MAX;
            int ye = Objects.nonNull(ag.getYearEnd()) ? ag.getYearEnd() : AGE_YEAR_MAX;
            if (ye < AGE_YEAR_MIN) ye = AGE_YEAR_MIN;
            if (ye > AGE_YEAR_MAX) ye = AGE_YEAR_MAX;
            final int ysFinal = Math.min(ys, ye);
            final int yeFinal = Math.max(ys, ye);

            // Normalizar edades 0..100
            int minAgeLocal = Objects.nonNull(ag.getMinAge()) ? ag.getMinAge() : 0;
            if (minAgeLocal < 0) minAgeLocal = 0;
            if (minAgeLocal > 100) minAgeLocal = 100;
            int maxAgeLocal = Objects.nonNull(ag.getMaxAge()) ? ag.getMaxAge() : 100;
            if (maxAgeLocal < 0) maxAgeLocal = 0;
            if (maxAgeLocal > 100) maxAgeLocal = 100;
            final int minAgeFinal = Math.min(minAgeLocal, maxAgeLocal);
            final int maxAgeFinal = Math.max(minAgeLocal, maxAgeLocal);

            // Preparar ids y llamar a repo para sumar en batch
            List<String> candidateIds = filtered.stream().map(MunicipalityEntity::getMunicipalityId).collect(Collectors.toList());
            if (!candidateIds.isEmpty()) {
                List<Integer> years = Arrays.asList(ysFinal, yeFinal);
                List<Object[]> sums = poblacionMunicipalRepository.sumByMunicipioAndYearsAndAgeRange(candidateIds, years, minAgeFinal, maxAgeFinal);

                // Construir mapa: municipioId -> (year -> sum)
                Map<String, Map<Integer, Double>> sumsMap = new HashMap<>();
                for (Object[] row : sums) {
                    String mid = (String) row[0];
                    Integer y = (Integer) row[1];
                    Double sum = row[2] == null ? 0.0 : ((Number) row[2]).doubleValue();
                    sumsMap.computeIfAbsent(mid, k -> new HashMap<>()).put(y, sum);
                }

                final Double percentMin = ag.getPercentMin() == null ? null : ag.getPercentMin().doubleValue();
                final Double percentMax = ag.getPercentMax() == null ? null : ag.getPercentMax().doubleValue();

                // Filtrar la lista `filtered` conservando sólo los que cumplan el growth entre los años sobre el rango de edades
                filtered = filtered.stream().filter(entity -> {
                    String id = entity.getMunicipalityId();
                    double start = sumsMap.getOrDefault(id, Collections.emptyMap()).getOrDefault(ysFinal, 0.0);
                    double end = sumsMap.getOrDefault(id, Collections.emptyMap()).getOrDefault(yeFinal, 0.0);
                    if (start <= 0d) return false; // política: no podemos calcular crecimiento desde 0 → excluir
                    double growthPct = ((end - start) * 100.0D) / start;
                    if (percentMin != null && growthPct < percentMin) return false;
                    if (percentMax != null && growthPct > percentMax) return false;
                    return true;
                }).collect(Collectors.toList());
            } else {
                // No candidates -> nothing to do
            }
        }

        List<String> ids = filtered.stream()
                .map(MunicipalityEntity::getMunicipalityId)
                .collect(Collectors.toList());

        // Construimos también la lista antigua de MunicipalityResponse para compatibilidad con el front
        List<MunicipalityResponse> responseList = filtered.stream()
                .map(entity -> {
                    MunicipalityResponse r = new MunicipalityResponse();
                    r.setMunicipalityId(entity.getMunicipalityId());
                    r.setMunicipalitiesInRadio(List.of()); // por ahora vacío
                    return r;
                })
                .collect(Collectors.toList());

        // Log: número de municipios encontrados y qué filtros estaban activos
        boolean popActive = filtersRequest.getPopulation() != null && Boolean.TRUE.equals(filtersRequest.getPopulation().getActive());
        boolean growthActive = filtersRequest.getGrowth() != null && Boolean.TRUE.equals(filtersRequest.getGrowth().getActive());
        boolean ageGrowthActive = filtersRequest.getAgeGrowth() != null && Boolean.TRUE.equals(filtersRequest.getAgeGrowth().getActive());
        boolean radiusActive = filtersRequest.getRadiusPopulation() != null && Boolean.TRUE.equals(filtersRequest.getRadiusPopulation().getActive());

        logger.info("Filter result: found {} municipalities; activeFilters={{population:{}, radiusPopulation:{}, growth:{}, ageGrowth:{}}}", ids.size(), popActive, radiusActive, growthActive, ageGrowthActive);

        FilterResponse response = new FilterResponse();
        response.setMunicipalityIds(ids);
        response.setMunicipalities(responseList);
        return response;
    }

}
