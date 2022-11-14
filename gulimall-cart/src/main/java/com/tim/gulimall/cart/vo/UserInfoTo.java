package com.tim.gulimall.cart.vo;

import lombok.Data;

/**
 * @author tim
 * @date 2022/11/13 03:43
 **/
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey; //一定封装
    private boolean tempUser = false;
}
