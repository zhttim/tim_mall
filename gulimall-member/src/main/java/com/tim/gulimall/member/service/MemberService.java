package com.tim.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tim.common.utils.PageUtils;
import com.tim.gulimall.member.entity.MemberEntity;
import com.tim.gulimall.member.exception.PhoneExsitException;
import com.tim.gulimall.member.exception.UsernameExistException;
import com.tim.gulimall.member.vo.MemberLoginVo;
import com.tim.gulimall.member.vo.MemberRegistVo;

import java.util.Map;

/**
 * 会员
 *
 * @author tim
 * @email
 * @date 2022-05-12 19:42:36
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void regist(MemberRegistVo vo);

    void checkPhoneUnique(String phone) throws PhoneExsitException;

    void checkUsernameUnique(String userName) throws UsernameExistException;

    MemberEntity login(MemberLoginVo vo);
}

