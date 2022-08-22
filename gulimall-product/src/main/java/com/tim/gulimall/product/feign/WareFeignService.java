package com.tim.gulimall.product.feign;

import com.tim.common.utils.R;
import com.tim.gulimall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author tim
 * @date 2022/8/22 23:38
 **/
@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("ware/waresku/hasstock")
    R<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds);
}
