package com.ebay.auction.model.entity;

import com.ebay.auction.model.enums.AuctionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a request to create an auction item.
 * Contains information such as the item name, start time, end time, and minimum price.
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Auction {
    /**
     * The id of the item to be auctioned.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    /**
     * The name of the item to be auctioned.
     */
    @NonNull
    @Column(name = "name")
    private String name;
    /**
     * The owner of the item to be auctioned.
     */
    @NonNull
    @Column(name = "user_name")
    private String userName;
    /**
     * The name of the item to be auctioned.
     */
    @NonNull
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * is auction active?
     */
    @Column(name = "is_active")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private AuctionStatus auctionStatus = AuctionStatus.NEW;

    /**
     * The minimum price for the item.
     * Defaults to 0 if not provided.
     */
    @Column(name = "start_price")
    private double startPrice = 0;

    /**
     * The start time of the auction.
     */
    @NonNull
    @Column(name = "start_time")
    private LocalDateTime startTime;
    /**
     * The end time of the auction.
     */
    @NonNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

}
