package com.jain.auction.service;

import com.jain.auction.model.entity.Auction;
import com.jain.auction.model.entity.Bid;
import com.jain.auction.model.entity.BidResponse;
import com.jain.auction.model.enums.AuctionStatus;
import com.jain.auction.model.enums.BidResponseStatus;
import com.jain.auction.model.enums.BidStatus;
import com.jain.auction.repository.AuctionRepository;
import com.jain.auction.repository.BidRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @InjectMocks
    private BidService bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBidsByAuction() {
        // Test data
        long auctionId = 1L;
        when(bidRepository.findByAuctionIdOrderByBidPriceAsc(auctionId)).thenReturn(List.of());

        // Test method invocation
        List<Bid> bids = bidService.getBidsByAuction(auctionId);

        // Assertion
        assertEquals(0, bids.size());
    }

    @Test
    void testGetHighestBid() {
        // Test data
        long auctionId = 1L;
        Bid expectedBid = new Bid();
        when(bidRepository.findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(auctionId, BidStatus.ACCEPTED))
                .thenReturn(expectedBid);

        // Test method invocation
        Bid highestBid = bidService.getHighestBid(auctionId);

        // Assertion
        assertEquals(expectedBid, highestBid);
    }

    @Test
    void testCreateBid_AuctionNotFound() {
        // Test data
        Bid bid = new Bid();
        bid.setAuctionId(1L);
        when(auctionRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method invocation
        BidResponse response = bidService.createBid(bid);

        // Assertion
        Assertions.assertEquals(BidResponseStatus.AUCTION_NOT_FOUND, response.getBidResponseStatus());
    }

    @Test
    void testCreateBid_AuctionNotInProgress() {
        // Test data
        Bid bid = new Bid();
        bid.setAuctionId(1L);
        Auction auction = new Auction();
        auction.setAuctionStatus(AuctionStatus.YET_TO_START);
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        // Test method invocation
        BidResponse response = bidService.createBid(bid);

        // Assertion
        Assertions.assertEquals(BidResponseStatus.AUCTION_NOT_STARTED, response.getBidResponseStatus());
    }

    @Test
    void testCreateBid_LowerBidPrice() {
        // Test data
        Bid bid = new Bid();
        bid.setAuctionId(1L);
        bid.setBidPrice(BigDecimal.valueOf(50));
        Auction auction = new Auction();
        auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
        auction.setStartPrice(BigDecimal.valueOf(100));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(1L, BidStatus.ACCEPTED))
                .thenReturn(null);

        // Test method invocation
        BidResponse response = bidService.createBid(bid);

        // Assertion
        Assertions.assertEquals(BidResponseStatus.LOWER_BID_PRICE, response.getBidResponseStatus());
    }

    @Test
    void testCreateBid_Accepted() {
        // Test data
        Bid bid = new Bid();
        bid.setAuctionId(1L);
        bid.setBidPrice(BigDecimal.valueOf(150));
        Auction auction = new Auction();
        auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
        auction.setStartPrice(BigDecimal.valueOf(100));
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(1L, BidStatus.ACCEPTED))
                .thenReturn(null);
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);

        // Test method invocation
        BidResponse response = bidService.createBid(bid);

        // Assertion
        Assertions.assertEquals(BidResponseStatus.ACCEPTED, response.getBidResponseStatus());
    }
}
