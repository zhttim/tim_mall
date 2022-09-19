package com.tim.gulimall.search.service;

import com.tim.gulimall.search.vo.SearchParamVo;
import com.tim.gulimall.search.vo.SearchResultVo;

/**
 * @author tim
 * @date 2022/9/10 18:01
 **/
public interface MallSearchService {
    SearchResultVo search(SearchParamVo searchParamVo);
}
