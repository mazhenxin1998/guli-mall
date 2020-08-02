package com.mzx.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhenXinMa
 * @date 2020/7/26 20:29
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        /*
         * --------------------------------------------------------
         * 配置RestHighLevelClient.
         * --------------------------------------------------------
         * */
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200)));
    }


}
