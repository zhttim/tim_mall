package com.tim.gulimall.search.controller;

import com.tim.gulimall.search.service.MallSearchService;
import com.tim.gulimall.search.vo.SearchParamVo;
import com.tim.gulimall.search.vo.SearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tim
 * @date 2022/9/9 14:33
 **/
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @RequestMapping("/list.html")
    public String listPage(SearchParamVo searchParamVo, Model model) {
        SearchResultVo searchResultVo = mallSearchService.search(searchParamVo);
        model.addAttribute("result", searchResultVo);
        return "list";
    }
}