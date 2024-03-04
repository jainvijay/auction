package com.ebay.auction.model.entity;

import com.ebay.auction.model.enums.BidResponseStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class BidResponse {
    Long bidId;
    @NonNull
    BidResponseStatus bidResponseStatus;
    BigDecimal currentMaxPrice;
}
