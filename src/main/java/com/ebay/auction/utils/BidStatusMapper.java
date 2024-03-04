package com.ebay.auction.utils;

import com.ebay.auction.model.enums.AuctionStatus;
import com.ebay.auction.model.enums.BidResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class BidStatusMapper {
    private static final Map<AuctionStatus, BidResponseStatus> bidStatusMapper;

    static {
        bidStatusMapper = new HashMap<>();
        bidStatusMapper.put(AuctionStatus.NEW, BidResponseStatus.AUCTION_NOT_STARTED);
        bidStatusMapper.put(AuctionStatus.COMPLETED, BidResponseStatus.AUCTION_COMPLETED);
        bidStatusMapper.put(AuctionStatus.CANCELLED, BidResponseStatus.AUCTION_CANCELLED);
    }

    public static BidResponseStatus getBidStatus(AuctionStatus auctionStatus) {
        return bidStatusMapper.get(auctionStatus);
    }
}
