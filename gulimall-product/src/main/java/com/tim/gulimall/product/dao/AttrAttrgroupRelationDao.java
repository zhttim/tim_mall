package com.tim.gulimall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author tim
 * @email zhttim1805@gmail.com
 * @date 2022-05-08 16:45:06
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatchRelation(@Param("attrAttrgroupRelationEntities") List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities);
}
