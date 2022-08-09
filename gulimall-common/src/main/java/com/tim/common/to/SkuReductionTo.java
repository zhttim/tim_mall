package com.tim.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tim
 * @date 2022/8/9 18:27
 **/
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
