package com.mzx.gulimall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        // 先获取到所有List 参数不填就说明是查询所有.
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 使用流来进行对所以分类来进行整理.
        /*
         * 1. 通过集合获取流.
         * 2. 使用filter进行过滤,过滤之后所得到的是一个新的流,而不会去改变源数据.
         * 3. 可以通过流的map来操作流中的每一个元素.
         * 4. 可以通过调用一个流的函数来终止一个流，通常又count、collect
         * */
        List<CategoryEntity> list = baseMapper.get().stream().filter(entity -> {

            // 先获取以及目录.
            return entity.getParentCid() == 0;
        }).map(item -> {

            // 这里是针对所以一级目录进行子节点的设置.
            item.setChildren(getChildren(item, entities));
            // return之后返回的仍然是一个流.
            return item;
        }).sorted((item1, item2) -> {

            return (item1.getSort() == null ? 0 : item1.getSort()) - (item2.getSort() == null ? 0 : item2.getSort());
        }).collect(Collectors.toList());

        return list;
    }


    @Override
    public List<CategoryEntity> listBySQL() {

        // 测试.
        return baseMapper.get();
    }

    @Override
    public void removeMenus(List<Long> catIds) {

        // TODO 删除之前应该校验表单是否被其他表单引用.
        baseMapper.deleteBatchIds(catIds);

    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        // 通过递归调用来获取到所有的目录的子目录.
        List<CategoryEntity> list = all.stream().filter(entity -> {

            // 获取到root的所有子目录
            return entity.getParentCid().equals(root.getCatId());
        }).map(item -> {

            // 这里为第二级目录设置子菜单.
            item.setChildren(this.getChildren(item, all));
            return item;
        }).sorted((item1, item2) -> {

            return (item1.getSort() == null ? 0 : item1.getSort()) - (item2.getSort() == null ? 0 : item2.getSort());
        }).collect(Collectors.toList());

        return list;
    }

}