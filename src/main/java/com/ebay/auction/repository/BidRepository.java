package com.ebay.auction.repository;

import com.ebay.auction.model.entity.Bid;
import com.ebay.auction.model.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionIdOrderByBidPriceAsc(Long auctionId);
    Bid findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(Long auctionId, BidStatus bidStatus);

}
