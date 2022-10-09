package com.tim.gulimall.search.service;

import com.tim.gulimall.search.vo.SearchParam;
import com.tim.gulimall.search.vo.SearchResult;

/**
 * @author tim
 * @date 2022/9/10 18:01
 **/
public interface MallSearchService {
    /**
     * @param param 检索的所有参数
     * @return 返回检索的结果, 里面包含页面需要的所有信息
     */
    SearchResult search(SearchParam param);
}
