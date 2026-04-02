package org.global.housing.controller.municipality;
import org.global.housing.dto.request.FiltersRequest;
import org.global.housing.dto.response.FilterResponse;
import org.global.housing.model.MunicipalityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.global.housing.service.MunicipalityService;

import java.util.List;
import java.util.Map;

@RestController
public class MunicipalityController {

    @Autowired
    private MunicipalityService municipalityService;

    @GetMapping("/allMunicipalities")
    public List<MunicipalityDto> getAllMunicipalities() {
        return this.municipalityService.getAllMunicipalities();
    }

    @GetMapping("allMunicipalitiesWithPopulationByYear/{year}")
    public List<MunicipalityDto> getAllMunicipalitiesWithPopulationByYear(@PathVariable int year) {
        return municipalityService.getAllMunicipalitiesWithPopulationByYear(year);
    }

    @GetMapping("municipality/allYearPopulation/{id}")
    public Map<Integer,Integer> getAllYearPopulation(@PathVariable String id) {
        return municipalityService.getAllYearPopulationById(id);
    }

    @PostMapping("/filter")
    public FilterResponse filter(@RequestBody FiltersRequest request) {
        return municipalityService.filter(request);
    }
}
