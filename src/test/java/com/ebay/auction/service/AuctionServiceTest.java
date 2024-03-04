package com.ebay.auction.service;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.model.entity.Bid;
import com.ebay.auction.model.enums.AuctionStatus;
import com.ebay.auction.model.enums.BidStatus;
import com.ebay.auction.repository.AuctionRepository;
import com.ebay.auction.repository.BidRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

class AuctionServiceTest {

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    BidRepository bidRepository;

    @InjectMocks
    AuctionService auctionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAuctions() {
        List<Auction> expected = new ArrayList<>();
        Mockito.when(auctionRepository.findAll()).thenReturn(expected);
        List<Auction> actual = auctionService.getAllAuctions();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAuctionById() {
        Optional<Auction> expected = Optional.of(new Auction());
        Mockito.when(auctionRepository.findById(1l)).thenReturn(expected);
        Auction actual = auctionService.getAuctionById(1l);
        Assertions.assertEquals(expected.get(), actual);
    }

    @Test
    void getEmptyAuctionById() {
        Mockito.when(auctionRepository.findById(1l)).thenReturn(Optional.empty());
        Auction actual = auctionService.getAuctionById(1l);
        Assertions.assertNull(actual);
    }

    @Test
    void updateAuction() {
        Auction existingAuction = createSampleAuction();
        Auction updatedAuction = createSampleAuction();
        updatedAuction.setName("updated_name");
        Mockito.when(auctionRepository.findById(1l)).thenReturn(Optional.of(existingAuction));
        Auction actual = auctionService.updateAuction(1l, updatedAuction);
        Mockito.verify(auctionRepository, times(1)).save(any());
        assertEquals(existingAuction.getName(), "updated_name");
    }

    @Test
    void updateNonExistingAuction() {
        Auction existingAuction = createSampleAuction();
        Auction updatedAuction = createSampleAuction();
        updatedAuction.setName("updated_name");
        Mockito.when(auctionRepository.findById(1l)).thenReturn(Optional.empty());
        Mockito.verify(auctionRepository, times(0)).save(any());
    }

    @Test
    void deleteAuction() {
        Auction auction = createSampleAuction();
        Mockito.when(auctionRepository.existsById(1L)).thenReturn(true);
        assertTrue(auctionService.deleteAuction(1L));
        Mockito.verify(auctionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNonExistingAuction() {
        Mockito.when(auctionRepository.existsById(1L)).thenReturn(false);
        assertFalse(auctionService.deleteAuction(1L));
    }

    @Test
    void createAuction() {
        Auction auction = createSampleAuction();
        Mockito.when(auctionRepository.save(any())).thenReturn(auction);
        Auction savedAuction = auctionService.createAuction(auction);
        assertEquals(auction, savedAuction);
    }

    @Test
    void getAuctionByStatus() {
        Auction auction = createSampleAuction();
        List<Auction> expected = List.of(auction);
        Mockito.when(auctionRepository.findByAuctionStatus(any())).thenReturn(expected);
        List<Auction> actual = auctionService.getAuctionByStatus(AuctionStatus.NEW);
        assertEquals(expected, actual);
    }

    @Test
    void closeAuction() {
        Auction auction = createSampleAuction();
        auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        Mockito.when(auctionRepository.save(auction)).thenReturn(auction);
        Mockito.when(bidRepository.findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(1L, BidStatus.ACCEPTED)).thenReturn(null);
        auctionService.closeAuction(auction);
        assertEquals(AuctionStatus.CANCELLED, auction.getAuctionStatus());
        Mockito.verify(auctionRepository, times(2)).save(auction);
    }

    @Test
    void closeAuctionWithHighestBidder() {
        Auction auction = createSampleAuction();
        auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        Mockito.when(auctionRepository.save(auction)).thenReturn(auction);
        Bid bid = createSampleBid();
        Mockito.when(bidRepository.
                findFirstByAuctionIdAndBidStatusOrderByBidPriceDesc(1L, BidStatus.ACCEPTED))
                .thenReturn(bid);
        auctionService.closeAuction(auction);
        assertEquals(AuctionStatus.COMPLETED, auction.getAuctionStatus());
        Mockito.verify(auctionRepository, times(2)).save(auction);
    }

    private Bid createSampleBid() {
        Bid bid = new Bid();
        bid.setAuctionId(1l);
        bid.setBidStatus(BidStatus.ACCEPTED);
        bid.setBidPrice(BigDecimal.valueOf(100l));
        bid.setId(1l);
        return bid;
    }


    public Auction createSampleAuction() {
        Auction auction = new Auction();
        auction.setId(1l);
        auction.setName("name");
        auction.setUserName("username");
        auction.setAuctionStatus(AuctionStatus.NEW);
        auction.setDescription("description");
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusDays(1l));
        return auction;
    }
}