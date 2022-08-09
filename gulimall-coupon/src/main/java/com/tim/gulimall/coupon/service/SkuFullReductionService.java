package com.tim.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.to.SkuReductionTo;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author tim
 * @email
 * @date 2022-05-12 19:25:04
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

