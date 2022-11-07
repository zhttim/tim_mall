package com.tim.gulimall.product.web;

import com.tim.gulimall.product.service.SkuInfoService;
import com.tim.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author tim
 * @date 2022/10/18 10:27
 **/
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = skuInfoService.item(skuId);

        model.addAttribute("item", skuItemVo);

        return "item";
    }

}
