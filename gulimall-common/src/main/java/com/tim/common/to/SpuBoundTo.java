package com.tim.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tim
 * @date 2022/8/9 18:15
 **/
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
