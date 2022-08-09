package com.tim.gulimall.product.feign;

import com.tim.common.to.SkuReductionTo;
import com.tim.common.to.SpuBoundTo;
import com.tim.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author tim
 * @date 2022/8/9 18:08
 **/
@FeignClient("gulimall-coupon ")
public interface CouponFeignService {
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
