package com.ebay.auction.scheduler;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.model.enums.AuctionStatus;
import com.ebay.auction.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AuctionScheduler {

    @Autowired
    private AuctionService auctionService;

    @Scheduled(fixedRate = 60000)
    public void processNewAuctions() {
        List<Auction> auctions = auctionService.getAuctionByStatus(AuctionStatus.NEW);
        log.info("Starting Auction Processing");

        for(Auction auction: auctions) {
            if (canStartAuction(auction)) {
                log.info("Starting Auction: " + auction.getName());
                auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
                auctionService.updateAuction(auction);
            }
        }
        log.info("Finished Auction Processing");
    }

    private boolean canStartAuction(Auction auction) {
        return auction.getStartTime().isBefore(LocalDateTime.now());
    }
}
