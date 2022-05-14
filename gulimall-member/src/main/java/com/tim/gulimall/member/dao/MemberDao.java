package com.tim.gulimall.member.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tim.gulimall.member.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author tim
 * @email 
 * @date 2022-05-12 19:42:36
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
