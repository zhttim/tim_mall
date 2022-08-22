package com.tim.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.product.entity.AttrEntity;
import com.tim.gulimall.product.vo.AttrGroupRelationVo;
import com.tim.gulimall.product.vo.AttrRespVo;
import com.tim.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author tim
 * @email zhttim1805@gmail.com
 * @date 2022-05-08 16:45:07
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrRespVo attrRespVo);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrsIds(List<Long> attrIds);
}

