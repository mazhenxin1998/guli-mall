package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.entity.AttrGroupEntity;

import java.util.Map;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询的时候带上分类ID.
     * <p>
     * 不管catId是不是为0 都需要带上模糊查询.
     *
     * @param params
     * @param catId
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Long catId);

    /**
     * 从中间表中查询该分组ID下的所有属性. 自己实现的. 功能不可用》。。
     *
     * @param attrgroupId
     * @param params
     * @return
     */
    R queryAttrDetail(Map<String, Object> params, Long attrgroupId);

    /**
     * 获取该分类能关联的属性.
     *
     * @param params
     * @param attrGroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);

    /**
     * 查询出该分组下所有关联的属性.
     *
     * @param params
     * @param attrgroupId
     * @return
     */
    PageUtils getRelationAttr(Map<String, Object> params, Long attrgroupId);
}

