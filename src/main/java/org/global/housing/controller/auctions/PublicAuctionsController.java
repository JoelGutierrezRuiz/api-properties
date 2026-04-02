package org.global.housing.controller.auctions;

import org.global.housing.model.PublicAuction;
import org.global.housing.service.PublicAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublicAuctionsController {

    @Autowired
    private PublicAuctionService publicAuctionService;

    @GetMapping("/allPublicAuctions")
    public List<PublicAuction> getAllPublicAuctions() {
        return publicAuctionService.getPublicAuctions();
    }


}
