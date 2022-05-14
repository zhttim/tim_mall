package com.tim.gulimall.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.order.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author tim
 * @email 
 * @date 2022-05-12 19:53:31
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
