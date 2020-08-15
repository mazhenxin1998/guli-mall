package com.mzx.gulimall.search.es;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.search.constant.EsConstant;
import com.mzx.gulimall.search.entity.SkuEsModel;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author ZhenXinMa
 * @date 2020/7/26 20:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    @Autowired
    @Qualifier(value = "restHighLevelClient")
    private RestHighLevelClient client;

    /**
     * 创建索引库.
     */
    @Test
    public void update() {

        CreateIndexRequest indexRequest = new CreateIndexRequest("gulimall-product");
        try {

            client.indices().create(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println("增加成功了!");
    }

    @Test
    public void add() {


        SkuEsModel model = new SkuEsModel();
        IndexRequest request = new IndexRequest("gulimall");
        request.id("1");
        // 设置超时时间
        request.timeout(TimeValue.timeValueSeconds(1));
        String mapJson = JSON.toJSONString(model);
        request.source(mapJson, XContentType.JSON);
        try {

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            System.out.println(response.toString());

        } catch (IOException e) {

            e.printStackTrace();
        }


    }


    @Test
    public void query() {

        SearchRequest request = new SearchRequest("gulimall-product");


    }


    @Test
    public void get() {

        GetRequest request = new GetRequest("gulimall-product", "TiLO4nMB90vt6RSEweoG");
        try {

            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            Map<String, Object> map = response.getSourceAsMap();
            System.out.println(map.toString());
            System.out.println(1);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Test
    public void t2() throws IOException {

        SearchRequest request = new SearchRequest("gulimall-product");

        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("skuTitle", "华为"));

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("skuTitle");
        highlightBuilder.preTags("<b style='color:red'>");
        highlightBuilder.postTags("</b>");
        highlightBuilder.requireFieldMatch(false);


        builder.highlighter(highlightBuilder);
        builder.highlighter(highlightBuilder);
        builder.query(boolQuery);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        System.out.println("----");

        for (SearchHit hit : response.getHits()) {

            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap.toString());
        }

        System.out.println("----");

//        for (SearchHit hit : response.getHits()) {
//
//            // 解析高亮.
//            Map<String, HighlightField> fields = hit.getHighlightFields();
//            HighlightField skuTitle = fields.get("skuTitle");
//            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//            if (!StringUtils.isEmpty(skuTitle)) {
//
//                Text[] texts = skuTitle.fragments();
//                String result = "";
//                for (Text text : texts) {
//
//                    result += text;
//                }
//                System.out.println("解析之后的高亮结果为: "+result);
//            }
//
//        }

        System.out.println(1);
    }

    @Test
    public void delete() throws IOException {

        DeleteRequest deleteRequest = new DeleteRequest(EsConstant.ES_PRODUCT_INDEX,"SiLO4nMB90vt6RSEnuqj");
        client.delete(deleteRequest,RequestOptions.DEFAULT);

    }


}
