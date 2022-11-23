package com.tim.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.to.mq.SeckillOrderTo;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.order.entity.OrderEntity;
import com.tim.gulimall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author tim
 * @email
 * @date 2022-05-12 19:53:31
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    OrderEntity getOrderByOrderSn(String orderSn);

    void closeOrder(OrderEntity entity);

    PayVo getOrderPay(String orderSn);

    PageUtils queryPageWithItem(Map<String, Object> params);

    String handlePayResult(PayAsyncVo vo);

    void createSeckillOrder(SeckillOrderTo seckillOrder);
}

