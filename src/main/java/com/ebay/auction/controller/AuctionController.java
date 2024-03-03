package com.ebay.auction.controller;

import com.ebay.auction.model.entity.Auction;
import com.ebay.auction.service.AuctionService;
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
/**
 * Controller class for managing auction resources.
 */
@RestController
@RequestMapping("/auctions")
public class AuctionController {

    /**
     * Service to handle auction-related operations.
     */
    @Autowired
    private AuctionService auctionService;

    /**
     * @return ResponseEntity with list of auctions and HTTP status OK.
     */
    @GetMapping
    @RouterOperation(operation = @Operation(description = "Retrieve all auctions", tags = "auctions",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Auction.class)))))
    public ResponseEntity<List<Auction>> getAllAuctions() {
        List<Auction> auctions = auctionService.getAllAuctions();
        return new ResponseEntity<>(auctions, HttpStatus.OK);
    }

    /**
     * @param auction Auction object containing updated information.
     * @return ResponseEntity with created auction and HTTP status OK if successful, or HTTP status NOT_FOUND if auction not found.
     */
    @PostMapping()
    @RouterOperation(operation = @Operation(description = "create an auction", tags = "auction",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Auction.class)))))
    public ResponseEntity<Auction> updateAuction(@RequestBody Auction auction) {
        Auction updatedAuction = auctionService.createAuction(auction);
        if (updatedAuction != null) {
            return new ResponseEntity<>(updatedAuction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param id ID of the auction to retrieve.
     * @return ResponseEntity with auction and HTTP status OK if found, or HTTP status NOT_FOUND if not found.
     */
    @GetMapping("/{id}")
    @RouterOperation(operation = @Operation(description = "Retrieve an auction by ID.", tags = "auction",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Auction.class)))))
    public ResponseEntity<Auction> getAuctionById(@PathVariable("id") Long id) {
        Auction auction = auctionService.getAuctionById(id);
        if (auction != null) {
            return new ResponseEntity<>(auction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param id      ID of the auction to update.
     * @param auction Auction object containing updated information.
     * @return ResponseEntity with updated auction and HTTP status OK if successful, or HTTP status NOT_FOUND if auction not found.
     */
    @PutMapping("/{id}")
    @RouterOperation(operation = @Operation(description = "update an auction by ID with content", tags = "auction",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Auction.class)))))
    public ResponseEntity<Auction> updateAuction(@PathVariable("id") Long id, @RequestBody Auction auction) {
        Auction updatedAuction = auctionService.updateAuction(id, auction);
        if (updatedAuction != null) {
            return new ResponseEntity<>(updatedAuction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param id ID of the auction to delete.
     * @return ResponseEntity with HTTP status NO_CONTENT if successful, or HTTP status NOT_FOUND if auction not found.
     */
    @DeleteMapping("/{id}")
    @RouterOperation(operation = @Operation(description = "delete an auction", tags = "auction",
            responses = @ApiResponse(responseCode = "200", content = @Content(schema = @Schema()))))
    public ResponseEntity<Void> deleteAuction(@PathVariable("id") Long id) {
        boolean deleted = auctionService.deleteAuction(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
