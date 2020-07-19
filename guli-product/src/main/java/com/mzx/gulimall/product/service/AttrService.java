package com.mzx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.product.entity.AttrEntity;
import com.mzx.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 12:26:27
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 增加Attr的同时也增加对分组的关联, 可能分组为空，但是也分组也可能不为空.
     *
     * @param attr
     */
    void saveDetail(AttrVo attr);

    /**
     * 平台属性，规格参数查询.
     * <p>
     * 如果带有key关键字查询，那么就进行关键字查询. 而关键字查询的是 属性的id或者name.
     *
     * @param params
     * @param catelogId
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    /**
     * 删除attr属性表中的同时，也删除中间表的关系，是否启用逻辑删除.
     *
     * @param attrIds
     * @return
     */
    PageUtils deleteBatchByIds(Long[] attrIds);

    /**
     * 查询销售属性列表.
     *
     * @param params
     * @param catId
     * @return
     */
    PageUtils queryPageDetail(Map<String, Object> params, Long catId);
}

