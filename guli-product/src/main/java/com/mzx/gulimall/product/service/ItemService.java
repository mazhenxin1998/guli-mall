package com.mzx.gulimall.product.service;

import com.mzx.gulimall.product.vo.web.SkuItemVo;

/**
 * @author ZhenXinMa
 * @date 2020/8/19 16:52
 */
public interface ItemService {

    /**
     * 根据SKU的ID查询出该SKU页面所需要的所有详细信息.
     * <p>
     * 需要考虑下这里是否需要添加缓存.
     * 1、页面上的属性不经常修改所以说可以将SKU的信息存入缓存中,
     * 2、对于需要不是经常变动的数据来说,我们采用删除模式. 在修改的时候将缓存中的数据进行删除.
     * 3、我们假设以后该项目的并发量很大,那么我们对缓存进行读取和更新的时候需要添加读写锁. 只要修改数据的线程在对数据进行修改那么添加写锁.
     * 4、即使缓存里面没有我们本次读取的数据, 从数据库中查询的时候我们采用异步方法去查询来提高效率.
     *
     * @param skuId
     * @return
     */
    SkuItemVo item(Long skuId);
}
