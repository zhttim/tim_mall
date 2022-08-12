package com.tim.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author tim
 * @date 2022/8/12 14:06
 **/
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
