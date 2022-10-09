package com.tim.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装页面所有可能传递过来的查询条件
 *
 * @author tim
 * @date 2022/9/10 17:53
 **/
@Data
public class SearchParam {

    //页面传递过来的全文匹配关键字
    private String keyword;

    //三级分类id
    private Long catalog3Id;

    /*
     *   sort=saleCount_asc/desc
     *   sort=skuPrice_asc/desc
     *   sort=hotScore_asc/desc
     */
    //排序条件
    private String sort;

    /*
     *  过滤条件
     *  hasStock(是否有货)、skuPrice区间、brandId、catalog3Id、attrs
     *  hasStock=0/1
     *  skuPrice=1_500/_500/500_
     *  brandId=1
     *  attrs=2_5存:6寸
     *
     */
    //是否只显示有货  0（无库存）1（有库存）
    private Integer hasStock;
    //价格区间查询
    private String skuPrice;
    //按照品牌进行查询，可以多选
    private List<Long> brandId;
    //按照属性进行筛选
    private List<String> attrs;
    //页码
    private Integer pageNum = 1;
    //原生的所有查询条件
    private String queryString;
}
