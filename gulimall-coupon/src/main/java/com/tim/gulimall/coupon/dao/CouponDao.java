package com.tim.gulimall.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.coupon.entity.CouponEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author tim
 * @email 
 * @date 2022-05-12 19:25:04
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
