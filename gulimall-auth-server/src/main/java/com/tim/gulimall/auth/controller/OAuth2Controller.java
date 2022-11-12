package com.tim.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tim.common.constant.AuthServerConstant;
import com.tim.common.utils.HttpUtils;
import com.tim.common.utils.R;
import com.tim.common.vo.MemberRespVo;
import com.tim.gulimall.auth.config.Oauth2ConfigProperties;
import com.tim.gulimall.auth.feign.MemberFeignService;
import com.tim.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tim
 * @date 2022/11/9 18:21
 **/
@Slf4j
@Controller
public class OAuth2Controller {
    @Autowired
    MemberFeignService memberFeignService;
    @Autowired
    Oauth2ConfigProperties oauth2ConfigProperties;


    /**
     * 社交登录成功回调
     *
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/oauth2/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> header = new HashMap<>();

        Map<String, String> query = new HashMap<>();

        Map<String, String> map = new HashMap<>();
        map.put("client_id", oauth2ConfigProperties.getClientId());
        map.put("client_secret", oauth2ConfigProperties.getClientSecret());
        map.put("grant_type", oauth2ConfigProperties.getGrantType());
        map.put("redirect_uri", "http://auth.gulimall.com/oauth2/weibo/success");
        map.put("code", code);
        //1、根据code换取accessToken；
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", header, query, map);

//        2、处理
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取到了 accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道当前是哪个社交用户
            //1）、当前用户如果是第一次进网站，自动注册进来(为当前社交用户生成一个会员信息账号，以后这个社交账号就对应指定的会员)
            //登录或者注册这个社交用户
            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if (oauthlogin.getCode() == 0) {
                MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登录成功：用户：{}", data.toString());

                session.setAttribute(AuthServerConstant.LOGIN_USER, data);
//
                //2、登录成功就跳回首页
                return "redirect:http://gulimall.com";

            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }

        } else {
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}
