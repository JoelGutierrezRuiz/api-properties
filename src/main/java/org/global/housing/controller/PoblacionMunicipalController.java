package org.global.housing.controller;

import org.global.housing.dto.response.MunicipalityAgeResponse;
import org.global.housing.service.PoblacionMunicipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/municipality")
public class PoblacionMunicipalController {

    private static final Logger logger = LoggerFactory.getLogger(PoblacionMunicipalController.class);

    @Autowired
    PoblacionMunicipalService service;

    @GetMapping("/{id}/ages")
    public ResponseEntity<MunicipalityAgeResponse> getAges(@PathVariable("id") String id) {
        logger.info("Incoming request GET /municipality/{}/ages", id);
        MunicipalityAgeResponse resp = service.getAgesByMunicipioId(id);
        if (resp.getAgeData() == null || resp.getAgeData().isEmpty()) {
            logger.info("No age data found for municipalityId={}", id);
            return ResponseEntity.notFound().build();
        }
        // calcular métricas sencillas para el log
        int years = resp.getAgeData().size();
        int totalAges = resp.getAgeData().stream().mapToInt(y -> y.getAges() == null ? 0 : y.getAges().size()).sum();
        logger.info("Response OK for municipalityId={} name={} -> years={} totalAges={}", id, resp.getName(), years, totalAges);
        return ResponseEntity.ok(resp);
    }
}
