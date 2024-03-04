package com.jain.auction.repository;

import com.jain.auction.model.entity.Auction;
import com.jain.auction.model.enums.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByAuctionStatus(AuctionStatus auctionStatus);

}
