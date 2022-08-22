package com.tim.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.product.entity.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author tim
 * @email zhttim1805@gmail.com
 * @date 2022-05-08 16:45:07
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrsIds(@Param("attrIds") List<Long> attrIds);
}
