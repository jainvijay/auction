package com.ebay.auction.service;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.model.enums.AuctionStatus;
import com.ebay.auction.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* Service class for managing auction resources.
* */
@Service
public class AuctionService {

    /**
     * Repository to handle auction related operations
     */
    @Autowired
    private AuctionRepository auctionRepository;

    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    public Auction getAuctionById (Long id) {
        return auctionRepository.findById(id)
                .orElse(null);
    }

    public Auction updateAuction(Long id, Auction auction) {
        Optional<Auction> auctionFromRepository = auctionRepository.findById(id);
        if (auctionFromRepository.isEmpty()) {
            return null;
        }

        Auction existingAuction = auctionFromRepository.get();

        existingAuction.setName(auction.getName());
        existingAuction.setDescription(auction.getDescription());
        existingAuction.setStartPrice(auction.getStartPrice());
        existingAuction.setStartTime(auction.getStartTime());
        existingAuction.setEndTime(auction.getEndTime());
        existingAuction.setAuctionStatus(auction.getAuctionStatus());
        auctionRepository.save(existingAuction);

        return existingAuction;
    }

    public Auction updateAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public boolean deleteAuction(Long id) {
        if(auctionRepository.existsById(id)) {
            auctionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public List<Auction> getAuctionByStatus(AuctionStatus auctionStatus) {
        return auctionRepository.findByAuctionStatus(auctionStatus);
    }
}
