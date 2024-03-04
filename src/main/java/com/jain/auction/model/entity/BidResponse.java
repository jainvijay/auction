package com.jain.auction.model.entity;

import com.jain.auction.model.enums.BidResponseStatus;
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
