package com.jain.auction.model.entity;

import com.jain.auction.model.enums.BidStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represent a request to create a bid for an auction.
 * Contains information such as id, auction_id, bid_price, user_name
 */
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    /**
     * The id of the auction.
     */
    @NonNull
    @Column(name = "auction_id")
    private Long auctionId;

    /**
     * Bid Price offered for the auction.
     */
    @NonNull
    @Column(name = "bid_price")
    private BigDecimal bidPrice;

    /**
     * User who bid for the auction
     */
    @NonNull
    @Column(name = "user_name")
    private String userName;

    /**
     * Status of the bid.
     */
    @NonNull
    @Column(name = "bid_status")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private BidStatus bidStatus = BidStatus.ACCEPTED;

}
