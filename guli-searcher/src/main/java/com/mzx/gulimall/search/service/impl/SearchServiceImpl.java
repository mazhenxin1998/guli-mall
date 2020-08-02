package com.mzx.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.search.entity.SkuEsModel;
import com.mzx.gulimall.search.service.ISearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/8/1 16:21
 */
@Slf4j
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    @Qualifier(value = "restHighLevelClient")
    private RestHighLevelClient client;


    public boolean saveProduct(List<SkuEsModel> models) {

        // 批量保存每一个SKU.
        BulkRequest request = new BulkRequest();
        for (SkuEsModel model : models) {

            // 为每一个SkuEsModel都设置一个IndexRequest
            IndexRequest indexRequest = new IndexRequest("gulimall-product");
            // 不需要设置ID,ES只是用来模糊进行搜索的.
            indexRequest.source(JSON.toJSONString(model), XContentType.JSON);
            request.add(indexRequest);
        }

        try {

            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            // 表示是否有错误出现.
            boolean failures = bulkResponse.hasFailures();
            if (failures) {

                //发生了异常.
                log.error("批量操作的时候发生了错误.");

            } else {

                // 我认为这里没有必要进行这样判断，如果注入失败的时候，应该会给抛出异常的吧.
            }


            System.out.println("商品保存成功: " + models.size());
            return true;
        } catch (IOException e) {
            System.out.println("商品上架失败...");
            e.printStackTrace();
            return false;
        }

    }
}
