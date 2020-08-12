package com.mzx.gulimall.search.service.impl;

import com.mzx.gulimall.search.entity.SkuEsModel;
import com.mzx.gulimall.search.service.IGuliMallSearchService;
import com.mzx.gulimall.search.vo.SearchParam;
import com.mzx.gulimall.search.vo.SearchResult;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 17:01
 */
@Service
public class GuliMallSearchServiceImpl implements IGuliMallSearchService {

    @Autowired
    @Qualifier(value = "restHighLevelClient")
    private RestHighLevelClient client;

    public SearchResult search(SearchParam param) {

        SearchRequest request = new SearchRequest("gulimall-product");


        SearchSourceBuilder builder = new SearchSourceBuilder();

        request.source(builder);

        try {

            // 使用ES中的客户端进行查询.
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

//    private SearchResult transformResult(SearchResponse response) {
//
//        SearchResult result = new SearchResult();
//
//        SearchHits hits = response.getHits();
//        List<SkuEsModel> productList = new ArrayList<SkuEsModel>();
//        for (SearchHit hit : hits.getHits()) {
//
//            // 这里是查询命中的每一个商品的信息.
//            SkuEsModel model = new SkuEsModel();
//            Map<String, Object> map = hit.getSourceAsMap();
//            // Attrs属性获取不到.
//
//            productList.add(model);
//        }
//
//        // 设置本次查询出的所有产品.
//        result.setProducts(productList);
//
//        // 设置本次查询所关联的所有分类信息
//        result.setCatalogs();
//
//        // 设置本次查询所涉及的所有规格参数属性.
//        result.setAttrs();
//
//        // 设置本次查询所涉及的所有品牌信息
//        result.setBrands();
//
//        // 设置传进来的页码数.
//        result.setPageNum();
//
//        // 设置本次查询的总记录数
//        result.setTotal();
//
//        // 设置本次查询的总页码数.
//        result.setTotalPages();
//
//        return result;
//    }
}
