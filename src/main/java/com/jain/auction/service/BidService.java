package com.jain.auction.service;

import com.jain.auction.model.entity.Auction;
import com.jain.auction.model.entity.Bid;
import com.jain.auction.model.entity.BidResponse;
import com.jain.auction.model.enums.AuctionStatus;
import com.jain.auction.model.enums.BidResponseStatus;
import com.jain.auction.model.enums.BidStatus;
import com.jain.auction.repository.AuctionRepository;
import com.jain.auction.repository.BidRepository;
import com.jain.auction.utils.BidStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing bid resources
 */
@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    public List<Bid> getBidsByAuction(Long auctionId) {
        return bidRepository.findByAuctionIdOrderByBidPriceAsc(auctionId);
    }

    public Bid getHighestBid(Long auctionId) {
        return bidRepository.findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(auctionId, BidStatus.ACCEPTED);
    }

    public BidResponse createBid(Bid bid) {
        Auction auction = auctionRepository.findById(bid.getAuctionId()).orElse(null);
        if (auction == null) {
            return new BidResponse(BidResponseStatus.AUCTION_NOT_FOUND);
        }

        if (!auction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)) {
            return new BidResponse(BidStatusMapper.getBidStatus(auction.getAuctionStatus()));
        }

        Bid maxBidSoFar = bidRepository.
                findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(bid.getAuctionId(), BidStatus.ACCEPTED);

        BidResponse bidResponse = checkMinBidPrice(bid, maxBidSoFar, auction);
        if (bidResponse != null) return bidResponse;

        Bid savedBid = bidRepository.save(bid);
        return new BidResponse(savedBid.getId(), BidResponseStatus.ACCEPTED, bid.getBidPrice());

    }

    private BidResponse checkMinBidPrice(Bid bid, Bid maxBidSoFar, Auction auction) {
        BigDecimal minPrice = maxBidSoFar == null ? auction.getStartPrice() : maxBidSoFar.getBidPrice();

        if (bid.getBidPrice().compareTo(minPrice) < 0) {
            BidResponse bidResponse = new BidResponse(BidResponseStatus.LOWER_BID_PRICE);
            bidResponse.setCurrentMaxPrice(minPrice);
            return bidResponse;
        }
        return null;
    }


}
