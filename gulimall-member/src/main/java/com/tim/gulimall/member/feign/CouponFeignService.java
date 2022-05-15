package com.tim.gulimall.member.feign;

import com.tim.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tim
 * @date 2022/5/15 15:46
 **/
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/member/coupons")
    public R memberCoupons();
}
