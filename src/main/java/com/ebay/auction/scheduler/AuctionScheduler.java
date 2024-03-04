package com.ebay.auction.scheduler;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.model.entity.Bid;
import com.ebay.auction.model.enums.AuctionStatus;
import com.ebay.auction.service.AuctionService;
import com.ebay.auction.service.BidService;
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

    @Autowired
    private BidService bidService;

    @Scheduled(fixedRate = 60000)
    public void processNewAuctions() {
        log.info("processNewAuctions Started");

        List<Auction> auctions = auctionService.getAuctionByStatus(AuctionStatus.NEW);

        for(Auction auction: auctions) {
            if (canStartAuction(auction)) {
                log.info("Starting Auction: " + auction.getName());
                auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
                auctionService.updateAuction(auction);
            }
        }
        log.info("processNewAuctions Finished");
    }

    @Scheduled(fixedRate = 60000)
    public void closeAuctions() {
        log.info("closeAuctions Started");

        List<Auction> auctions = auctionService.getAuctionByStatus(AuctionStatus.IN_PROGRESS);
        for (Auction auction: auctions) {
            if (canCloseAuction(auction)) {
                log.info("Close Auction: " + auction.getName());
                auctionService.closeAuction(auction);
            }
        }
        log.info("closeAuctions Finished");
    }


    private boolean canCloseAuction(Auction auction) {
        return auction.getEndTime().isBefore(LocalDateTime.now());
    }

    private boolean canStartAuction(Auction auction) {
        return auction.getStartTime().isBefore(LocalDateTime.now());
    }
}
