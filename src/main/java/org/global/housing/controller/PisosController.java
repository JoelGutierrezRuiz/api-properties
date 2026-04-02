package org.global.housing.controller;

import org.global.housing.dto.PisosListing;
import org.global.housing.service.PisosSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pisos")
public class PisosController {

    private static final Logger logger = LoggerFactory.getLogger(PisosController.class);

    @Autowired
    private PisosSearchService service;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        logger.info("/api/pisos/ping recibido");
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String municipio,
            @RequestParam(required = false) String selector,
            @RequestParam(required = false) Integer priceFrom,
            @RequestParam(required = false) Integer priceTo
    ) {
        try {
            logger.info("/api/pisos/search llamada municipio={} selector={} priceFrom={} priceTo={}", municipio, selector, priceFrom, priceTo);
            List<PisosListing> items = service.search(municipio, selector, priceFrom, priceTo);
            logger.info("/api/pisos/search resultado: {} items", items.size());
            // log minimal: cantidad y filtros activos
            logger.info("Filtro usado selector={} priceFrom={} priceTo={}", selector, priceFrom, priceTo);
            return ResponseEntity.ok(items);
        } catch (IllegalArgumentException e) {
            logger.warn("Bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error en búsqueda: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
