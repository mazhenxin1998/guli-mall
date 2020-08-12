package com.mzx.gulimall.product.web;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author ZhenXinMa
 * @date 2020/8/7 20:06
 */
@Service
public class CachingTest {



    /**
     * @return
     * @Cacheable 中value应该是缓存分组的情况. 该注解的意思触发将数据保存到缓存的操作
     * 如果当前方法的返回结果存在缓存中，那么该方法就不会调用，直接从缓存中取，如果该方法的
     * 返回值不在缓存中，那就会先调用方法，最后将返回值存入缓存中.
     * @CacheEvict 触发将缓存删除的操作.
     * @CachePut 不影响方法执行更新缓存.
     * @Caching 组合以上多个操作.
     * @CacheConfig 作用在类上，共享缓存相同的配置.
     */
    @Cacheable(value = {"test"})
    public String t1() {

        // @Cacheable Spring在调用该方法会先检查该方法的返回值是否存在配置的SpringCacheType里面,如果存在那么就直接返回而不去调用方法，
        //            如果该方法的返回值不在缓存里面存在，那么就会先执行该方法，最后将该方法的返回值放入缓存中.
        String s = new String("我使用了SpringCache缓存组件");
        System.out.println("CachingTest 里面的方法t1执行了...缓存没有命中...");
        return s;
    }



}
