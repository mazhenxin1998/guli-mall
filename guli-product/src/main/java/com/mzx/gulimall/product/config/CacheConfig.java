package com.mzx.gulimall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author ZhenXinMa
 * @date 2020/8/12 17:34
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    /**
     * 如果使用了当前配置项的配置,那么在application.properties配置文件中配置的配置内容将是无效的.
     *
     * @return
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        // 获取默认配置.
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 分别设置Key和Value的序列化器.
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()));
        // 设置value的序列化器, 默认使用的JDK序列化器. 显示的时候是16进制.
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericFastJsonRedisSerializer()
        ));
        // 重要: 既自定义了自己的配置,还使用了配置文件中的配置.
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {

            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {

            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {

            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {

            config = config.disableKeyPrefix();
        }

        return config;
    }


}
