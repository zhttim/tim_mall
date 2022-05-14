package com.tim.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.member.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author tim
 * @email 
 * @date 2022-05-12 19:42:35
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

