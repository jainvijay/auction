package com.jain.auction.controller;


import com.jain.auction.model.entity.Bid;
import com.jain.auction.model.entity.BidResponse;
import com.jain.auction.model.enums.BidResponseStatus;
import com.jain.auction.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bid")
public class BidController {
    /**
     * Service to handle bid related operations
     */
    @Autowired
    private BidService bidService;

    /**
     * @param auctionId
     * @return list of bids for the auction id
     */
    @GetMapping("/{auction_id}")
    @RouterOperation(operation = @Operation(description = "Retrieve all bids by Auction Id", tags = "bids",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Bid.class)))))
    public ResponseEntity<List<Bid>> getAllBidsByAuction(@PathVariable("auction_id") Long auctionId) {
        List<Bid> bids = bidService.getBidsByAuction(auctionId);
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    @PostMapping()
    @RouterOperation(operation = @Operation(description = "Create a bid", tags = "bid",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BidResponse.class)))))
    public ResponseEntity<BidResponse> createBid(@RequestBody Bid bid) {
        BidResponse createdBid = bidService.createBid(bid);
        if (createdBid.getBidResponseStatus().equals(BidResponseStatus.ACCEPTED)) {
            return new ResponseEntity<>(createdBid, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(createdBid, HttpStatus.BAD_REQUEST);
        }
    }

}
