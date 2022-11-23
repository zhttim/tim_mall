package com.tim.gulimall.seckill.service;

import com.tim.gulimall.seckill.to.SecKillSkuRedisTo;

import java.util.List;

/**
 * @author tim
 * @date 2022/11/20 03:04
 **/
public interface SeckillService {
    void uploadSeckillSkuLatest3Days();

    List<SecKillSkuRedisTo> getCurrentSeckillSkus();

    SecKillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
