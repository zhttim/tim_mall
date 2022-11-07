package com.tim.gulimall.product.vo;

import com.tim.gulimall.product.entity.SkuImagesEntity;
import com.tim.gulimall.product.entity.SkuInfoEntity;
import com.tim.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author tim
 * @date 2022/10/18 10:53
 **/
@Data
@ToString
public class SkuItemVo {
    // 1、sku基本获取 pms_sku_info
    SkuInfoEntity info;
    // 是否有库存
    boolean hasStock = true;
    // 2、sku的图片信息 pms_sku_images
    List<SkuImagesEntity> images;
    // 3、获取spu的销售属性组
    List<SkuItemSaleAttrVo> saleAttr;
    // 4、获取spu的介绍
    SpuInfoDescEntity desc;
    // 5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;
}
