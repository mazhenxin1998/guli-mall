package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.vo.web.Catalog2Vo;

import java.util.List;
import java.util.Map;

/**
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
     *
     * @return
     */
    List<CategoryEntity> listBySQL();

    /**
     * 自定义的批量删除.
     *
     * @param catIds
     */
    void removeMenus(List<Long> catIds);

    /**
     * 当在分类管理列表对分类进行修改的时候，如果修改的是名字选项，还应该对分类和品牌关联的表中的分类名字也进行修改.
     *
     * @param category
     */
    void updateDetail(CategoryEntity category);

    /**
     * 返回值以一级分类的ID作为Key，其子分类作为其Key的值.
     *
     * @return
     */
    Map<String, List<Catalog2Vo>> findAllCatagory();

    /**
     * 查询出所有的以及分类.
     *
     * @return
     */
    List<CategoryEntity> findOneCategoryList();
}

