package com.tim.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.tim.gulimall.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author tim
 * @email zhttim1805@gmail.com
 * @date 2022-05-08 16:45:07
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}
