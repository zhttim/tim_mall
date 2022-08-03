package com.tim.gulimall.product.vo;

import lombok.Data;

/**
 * @author tim
 * @date 2022/8/2 18:13
 **/
@Data
public class AttrRespVo extends AttrVo {

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
