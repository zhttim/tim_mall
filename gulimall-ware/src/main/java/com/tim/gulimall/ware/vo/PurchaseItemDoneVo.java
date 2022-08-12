package com.tim.gulimall.ware.vo;

import lombok.Data;

/**
 * @author tim
 * @date 2022/8/12 17:38
 **/
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
