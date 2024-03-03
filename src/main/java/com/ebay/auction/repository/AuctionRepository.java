package com.ebay.auction.repository;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.model.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByAuctionStatus(AuctionStatus auctionStatus);

}
