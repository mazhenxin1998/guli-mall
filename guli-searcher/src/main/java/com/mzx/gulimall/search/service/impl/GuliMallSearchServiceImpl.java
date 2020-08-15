package com.mzx.gulimall.search.service.impl;

import com.mzx.gulimall.search.constant.EsConstant;
import com.mzx.gulimall.search.entity.SkuEsModel;
import com.mzx.gulimall.search.service.IGuliMallSearchService;
import com.mzx.gulimall.search.vo.SearchParam;
import com.mzx.gulimall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 17:01
 */
@Slf4j
@Service
public class GuliMallSearchServiceImpl implements IGuliMallSearchService {

    @Autowired
    @Qualifier(value = "restHighLevelClient")
    private RestHighLevelClient client;


    @Override
    public SearchResult search(SearchParam param) {

        // 构建检测请求.
        SearchRequest request = this.buildSearchRequest(param);
        try {

            // 使用ES中的客户端进行查询.
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            // 封装返回结果.
            SearchResult searchResult = this.transformResult(searchResponse);
            // 这个result也有可能为空.
            return searchResult;
        } catch (IOException e) {

            e.printStackTrace();
            // 返回一个空的对象.
            return new SearchResult();
        }

    }

    private SearchRequest buildSearchRequest(SearchParam param) {

        SearchRequest request = new SearchRequest(EsConstant.ES_PRODUCT_INDEX);
        // 构建查询语句.
        SearchSourceBuilder builder = new SearchSourceBuilder();
        // 构建bool查询 建多个条件合并查询.
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(param.getKeyword())) {

            // 如果关键字不为空 那么就使用模糊匹配进行全文匹.
            // bool下的must是必须查询的. 只有符合了该条件下的索引才会返回.
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }

        // 按照三级分类进行查询.
        if (!StringUtils.isEmpty(param.getCatalog3Id())) {

            // 该项是过滤项.
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }

        // 多个品牌.
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {

            // TODO: 构建该查询不知道会不会出现错误.
            boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        // 根据价格区间进行过滤.
        if (!StringUtils.isEmpty(param.getSkuPrice())) {

            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            // 传过来的价格为String类型并且格式为: 1_500/_500/500_ 表示1-500之间。。。
            String price = param.getSkuPrice();
            String[] strings = price.split("_");
            if (strings.length == 2) {

                // 表示有大有小
                rangeQuery.gte(strings[0]).lte(strings[1]);
            } else if (price.startsWith("_")) {

                // 表示从0到该价格.
                rangeQuery.lte(strings[0]);
            } else {

                rangeQuery.lte(strings[0]);
            }

            boolQuery.filter(rangeQuery);
        }

        // 排序字段.  sort=saleCount_asc/desc
        if (!StringUtils.isEmpty(param.getSort())) {

            // TODO : 未完成.
            String sort = param.getSort();
            String[] s = sort.split("_");
            if ("desc".equals(s[1])) {

                builder.sort(s[0], SortOrder.DESC);
            }
            if ("asc".equals(s[1])) {

                builder.sort(s[0], SortOrder.ASC);
            }

        }

        // 分页
        if (!StringUtils.isEmpty(param.getPageNum())) {

            builder.from((param.getPageNum() - 1) * EsConstant.PAGE_SIZE);
            builder.size(EsConstant.PAGE_SIZE);
        } else {

            builder.from(0);
            builder.size(EsConstant.PAGE_SIZE);
        }

        // 聚合不做 返回结果分封装的时候 自行封装.
        // 高亮显示.
        if (!StringUtils.isEmpty(param.getKeyword())) {

            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            highlightBuilder.requireFieldMatch(false);
            builder.highlighter(highlightBuilder);
        }

        builder.query(boolQuery);
        request.source(builder);
        return request;

    }

    private SearchResult transformResult(SearchResponse response) {

        if (response == null) {

            // 如果查询出来的response为空那么就直接返回空的结果.
            return new SearchResult();
        } else {

            // 每一个商品.
            List<SkuEsModel> productList = new ArrayList<SkuEsModel>();
            List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
            List<SearchResult.BrandVo> brandVos = new ArrayList<>();

            SearchResult result = new SearchResult();
            SearchHits hits = response.getHits();
            if (hits != null) {


                for (SearchHit hit : hits) {

                    // 表示命中的每一个记录》
                    Map<String, Object> map = hit.getSourceAsMap();
                    // 为了防止转换的时候出现错误, 用try将整个代码块括起来
                    try {


                        // 这里是查询命中的每一个商品的信息.
                        SkuEsModel model = new SkuEsModel();
                        // Attrs属性获取不到.
                        // 将String类型转换为Long类型
                        model.setSkuId(Long.parseLong(map.get("skuId").toString()));
                        model.setSkuImg(map.get("skuImg").toString());

                        // TODO: 外层有try内层可以不需要该异常控制.
                        BigDecimal price = new BigDecimal(map.get("skuPrice").toString());
                        model.setSkuPrice(price);
                        // 这个Title应该设置成谁的Title啊
                        // 使用高亮的Title.
                        Map<String, HighlightField> fields = hit.getHighlightFields();
                        HighlightField skuTitle = fields.get("skuTitle");
                        if (skuTitle != null) {

                            Text[] texts = skuTitle.fragments();
                            String s = "";
                            for (Text text : texts) {

                                s += text;
                            }

                            model.setSkuTitle(s);
                        }else{

                            model.setSkuTitle(map.get("skuTitle").toString());
                        }
                        model.setSpuId(Long.parseLong(map.get("spuId").toString()));
                        model.setBrandId(Long.parseLong(map.get("brandId").toString()));
                        model.setBrandImg(map.get("brandImg").toString());
                        model.setBrandName(map.get("brandName").toString());
                        model.setCatalogId(Long.parseLong(map.get("catalogId").toString()));
                        model.setCatalogName(map.get("catalogName").toString());

                        // 将所有的分类ID和名字收集起来.
                        catalogVos.add(new SearchResult.CatalogVo(Long.parseLong(map.get("catalogId").toString()),
                                map.get("catalogName").toString()));
                        // 将所有与品牌相关的东西封装起来.
                        brandVos.add(new SearchResult.BrandVo(Long.parseLong(map.get("brandId").toString()),
                                map.get("brandName").toString(), map.get("brandImg").toString()));
                        String stock = map.get("hasStock").toString();
                        if ("true".equals(stock)) {

                            // 只要是true则进来设置库存,否则一直是false.
                            model.setHasStock(true);
                        } else {

                            model.setHasStock(false);
                        }

                        model.setHotScore(Long.parseLong(map.get("hotScore").toString()));
                        model.setSaleCount(Long.parseLong(map.get("saleCount").toString()));
                        productList.add(model);

                    } catch (Exception e) {

                        // 一旦转换出现错误就返回控制,并且将日志打印出来.
                        log.error("从ES中查询Product信息进行转换出现错误: {} ", e.getMessage());
                    }
                }
            }


            // 设置本次查询出的所有产品.
            result.setProducts(productList);

            // 设置本次查询所关联的所有分类信息
            // TODO: 从上一步结果中获取catalogVos的含有重复数据.
            HashSet<SearchResult.CatalogVo> voHashSet = new HashSet<>(catalogVos);
            catalogVos.clear();
            catalogVos.addAll(voHashSet);
            result.setCatalogs(catalogVos);

            // 设置本次查询所涉及的所有规格参数属性.
            // 通过分类ID进行查询相关联的属性. . 这里暂时不设置根据参数属性进行查询.
            // result.setAttrs();

            // 去重.
            HashSet<SearchResult.BrandVo> brandVoHashSet = new HashSet<>(brandVos);
            brandVos.clear();
            brandVos.addAll(brandVoHashSet);
            result.setBrands(brandVos);
            // 设置传进来的页码数.
            if (response.getHits() != null) {

                // 设置本次查询的总记录数
                result.setTotal(response.getHits().getTotalHits().value);
            }
            result.setPageNum(EsConstant.PAGE_SIZE);
            // 设置本次查询的总页码数.
            // TODO: 这样写不知道有没有问题，出了问题在回头来看.
            result.setTotalPages(response.getHits().getTotalHits().value / EsConstant.PAGE_SIZE > 0 ?
                    (int) (response.getHits().getTotalHits().value / EsConstant.PAGE_SIZE) : 1);
            // 现在每一页有 EsConstant.PAGE_SIZE 一共有response.getHits().getTotalHits().value这么多个.

            return result;
        }
    }

    private List<SearchResult.AttrVo> getAttrs(List<SearchResult.CatalogVo> catas) {

        // 通过分类ID将所有关联的分类查出来.
//        List<SearchResult.AttrVo> result =
        return null;
    }


}
