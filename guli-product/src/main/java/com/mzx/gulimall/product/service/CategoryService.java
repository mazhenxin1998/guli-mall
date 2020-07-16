package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过JDK8的流来对集合进行操作并获取到整个三级目录.
     * <p>
     * SQL获取的目录也已经写好.
     *
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 根据SQL查询出三级分类,但是这里使用了,join关键字进行查询》
     * @return
     */
    List<CategoryEntity> listBySQL();

    /**
     * 自定义的批量删除.
     * @param catIds
     */
    void removeMenus(List<Long> catIds);
}

