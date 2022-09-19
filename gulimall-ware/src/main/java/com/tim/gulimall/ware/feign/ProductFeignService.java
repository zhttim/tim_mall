package com.tim.gulimall.ware.feign;

import com.tim.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tim
 * @date 2022/8/12 23:43
 **/
@FeignClient("gulimall-product")
public interface ProductFeignService {
    @RequestMapping("/product/skuinfo/info/{id}")
    public R info(@PathVariable("id") Long id);
}
