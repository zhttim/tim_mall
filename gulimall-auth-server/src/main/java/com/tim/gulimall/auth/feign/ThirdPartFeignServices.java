package com.tim.gulimall.auth.feign;

import com.tim.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tim
 * @date 2022/11/8 11:54
 **/
@FeignClient("gulimall-third-part")
public interface ThirdPartFeignServices {
    @GetMapping("/sms/sendcode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
