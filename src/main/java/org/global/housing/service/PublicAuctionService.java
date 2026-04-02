package org.global.housing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.global.housing.converter.PublicAuctionDtoPublicAuctionConverter;
import org.global.housing.dto.response.PublicAuctionDto;
import org.global.housing.model.PublicAuction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicAuctionService {

    @Autowired
    private PublicAuctionDtoPublicAuctionConverter converter;

    public List<PublicAuction> getPublicAuctions() {
        try {
            String url = "https://www2.agenciatributaria.gob.es/static_files/common/internet/dep/taiif/subastaInmuebles/data2/bienes.js";

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String raw = response.body();

            raw = raw.replaceAll("(?s)/\\*.*?\\*/", "").trim();

            raw = raw.replace("const inmueblesSubasta =", "").trim();

            if (raw.endsWith(";")) {
                raw = raw.substring(0, raw.length() - 1);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<PublicAuctionDto> auctions =
                    mapper.readValue(raw, new TypeReference<List<PublicAuctionDto>>() {
                    });

            return auctions.stream().map(auction -> converter.convert(auction)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
