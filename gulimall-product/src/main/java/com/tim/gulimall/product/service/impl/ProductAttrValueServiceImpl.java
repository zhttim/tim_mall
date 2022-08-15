package com.tim.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tim.common.utils.PageUtils;
import com.tim.common.utils.Query;
import com.tim.gulimall.product.dao.ProductAttrValueDao;
import com.tim.gulimall.product.entity.ProductAttrValueEntity;
import com.tim.gulimall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities) {
        this.saveBatch(productAttrValueEntities);
    }

    @Override
    public List<ProductAttrValueEntity> getBaseAttrListForSpu(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = this.baseMapper
                .selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return productAttrValueEntities;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities) {
        // 1、根据spuid删除记录
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        // 2、遍历传递过来的记录 设置 spuId
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueEntities.stream().map(productAttrValueEntity -> {
            productAttrValueEntity.setSpuId(spuId);
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        // 3、批量保存
        this.saveBatch(productAttrValueEntityList);
    }

}