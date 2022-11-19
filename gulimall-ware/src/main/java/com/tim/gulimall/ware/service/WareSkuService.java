package com.tim.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.to.mq.OrderTo;
import com.tim.common.to.mq.StockLockedTo;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.ware.entity.WareSkuEntity;
import com.tim.gulimall.ware.vo.SkuHasStockVo;
import com.tim.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author tim
 * @email
 * @date 2022-05-12 19:59:36
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);


    void unlockStock(OrderTo orderTo);
}

