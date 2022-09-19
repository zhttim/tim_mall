package com.tim.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.tim.common.to.es.SkuEsModel;
import com.tim.gulimall.search.config.ElasticSearchConfig;
import com.tim.gulimall.search.constant.EsConstant;
import com.tim.gulimall.search.service.MallSearchService;
import com.tim.gulimall.search.vo.SearchParamVo;
import com.tim.gulimall.search.vo.SearchResultVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tim
 * @date 2022/9/10 18:01
 **/
@Service("MallSearchService")
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResultVo search(SearchParamVo searchParamVo) {
        // 1、动态构建出查询需要的DSL语句
        SearchResultVo searchResultVo = null;
        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParamVo);

        try {
            // 2、执行检索请求
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // 3、分析响应数据封装成需要的格式
            searchResultVo = buildSearchResultVo(response, searchParamVo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchResultVo;
    }

    private SearchRequest buildSearchRequest(SearchParamVo param) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*
          模糊匹配 过滤（按照属性、分类、品牌、价格区间、库存）
         */
        // 1、构建bool - query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 1.1 must - 模糊匹配
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // 1.2 bool - filter 按照三级分类id来查询
        if (param.getCatalog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // 1.2 bool - filter 按照品牌id来查询
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }
        // 1.2 bool - filter 按照库存是否存在
        boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        // 1.2 bool - filter 按照价格区间
        /*
         * 1_500/_500/500_
         */
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                rangeQueryBuilder.gte(s[0]).lte(s[1]);
            } else {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQueryBuilder.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQueryBuilder.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        // 1.2 bool - filter 按照所有指定的属性来进行查询
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            for (String attr : param.getAttrs()) {
                // attr=1_5寸:8寸&attrs=2_16G:8G
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                // 检索的属性id
                String attrId = s[0];
                // 检索的属性值
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                // 每一个必须都生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQuery);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        /*
         * 排序、分页、高亮
         */
        //2.1、排序
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            //sort=hotScore_asc/desc
            String[] s = sort.split("_");
            SortOrder order = "asc".equalsIgnoreCase(s[1]) ? SortOrder.ASC : SortOrder.DESC;
            searchSourceBuilder.sort(s[0], order);
        }
        //2.2 分页 pageSize:5
        // pageNum:1 from 0 size:5 [0,1,2,3,4]
        // pageNum:2 from 5 size:5
        // from (pageNum - 1)*size
        searchSourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        //2.3、高亮
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            searchSourceBuilder.highlighter(builder);
        }
        /*
         * 聚合分析
         */
        //1、品牌聚合
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        //品牌聚合的子聚合
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        // 聚合brand
        searchSourceBuilder.aggregation(brandAgg);
        //2、分类聚合
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        // 聚合catalog
        searchSourceBuilder.aggregation(catalogAgg);
        //3、属性聚合 attr_agg
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        // 聚合出当前所有的attrId
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析出当前attr_id对应的名字
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        // 聚合分析出当前attr_id对应的可能的属性值attractValue
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attrAgg.subAggregation(attrIdAgg);
        // 聚合attr
        searchSourceBuilder.aggregation(attrAgg);
//        System.out.println(searchSourceBuilder.toString());

        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
    }

    private SearchResultVo buildSearchResultVo(SearchResponse response, SearchParamVo param) {
        SearchResultVo result = new SearchResultVo();
        SearchHits hits = response.getHits();
        //1、返回所有查询到的商品
        List<SkuEsModel> esModels = new ArrayList<>();

        if (hits.getHits() != null && hits.getHits().length > 0) {
            //遍历命中商品记录
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                //转为JSON字符串
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(string);
                }
                esModels.add(skuEsModel);
            }
        }
        result.setProducts(esModels);

        //2、当前所有商品设计到的所有属性信息
        List<SearchResultVo.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResultVo.AttrVo attrVo = new SearchResultVo.AttrVo();
            // 1、得到属性的id
            Long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);
            // 2、得到属性的名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg"))
                    .getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);
            // 3、得到属性的所有值
            List<String> attrValue = ((ParsedStringTerms) bucket
                    .getAggregations().get("attr_value_agg")).getBuckets().stream()
                    .map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
            attrVo.setAttrValue(attrValue);
            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);

        //3、当前所有商品的分类信息
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        List<SearchResultVo.CatalogVo> catalogVos = new ArrayList<>();
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            SearchResultVo.CatalogVo catalogVo = new SearchResultVo.CatalogVo();
            // 得到分类id
            Long catalogId = bucket.getKeyAsNumber().longValue();
            catalogVo.setCatalogId(catalogId);
            // 得到分类名
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalogName);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);

        //4、当前所有商品的品牌信息
        List<SearchResultVo.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResultVo.BrandVo brandVo = new SearchResultVo.BrandVo();
            // 1、得到品牌的id
            long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);
            // 2、得到品牌的图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);
            // 3、得到品牌的姓名
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);

            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        //5、分页信息 - 总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //6、分页信息 - 页码
        result.setPageNum(param.getPageNum());
        //7、分页信息 - 总页码
        int totalPages = (int) total % EsConstant.PRODUCT_PAGESIZE == 0 ? (int) total / EsConstant.PRODUCT_PAGESIZE : ((int) total / EsConstant.PRODUCT_PAGESIZE + 1);
        result.setTotalPages(totalPages);

        return result;
    }

}
