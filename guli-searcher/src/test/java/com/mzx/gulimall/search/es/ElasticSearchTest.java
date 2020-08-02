package com.mzx.gulimall.search.es;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.search.entity.SkuEsModel;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
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
    public void query(){

        SearchRequest request = new SearchRequest("gulimall-product");




    }


    @Test
    public void get(){

        GetRequest request = new GetRequest("gulimall", "1");
        try {

            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            Map<String, Object> map = response.getSourceAsMap();
            // 输出结果: {0=0, 1=1, 2=2}

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    

}
