package com.mzx.gulimall.product.service.impl;

import com.mzx.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.mzx.gulimall.product.dao.AttrGroupDao;
import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.mzx.gulimall.product.entity.AttrGroupEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.vo.AttrResponseVo;
import com.mzx.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;

import com.mzx.gulimall.product.dao.AttrDao;
import com.mzx.gulimall.product.entity.AttrEntity;
import com.mzx.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * @author lenovo
 */
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveDetail(AttrVo attr) {

        System.out.println("向数据库中增加了信息");
        // 向数据库中增加属性
        AttrEntity attrEntity = new AttrEntity();
        // BeanUtils 会将源对象里面有的属性拷贝到目标对象阿ing里面
        // 前提是目标对象里面有的属性才会被拷贝.
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);
        // 向数据库中增加属性和分组的关联关系.
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        // 赋值
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        // 构建查询对象
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", 1);
        QueryWrapper<AttrEntity> catID_1 = new QueryWrapper<>();
        catID_1.eq("attr_type", 1);
        if (catelogId != 0) {

            // 不等于需要查出一个count.
            catID_1.and(wrapper -> {

                wrapper.eq("catelog_id", catelogId);
            });
            queryWrapper.eq("catelog_id", catelogId);
        }

        // 如果是查询所有，则使用默认的QueryWrapper.
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {

            // 如果key不为空，那么就对查询条件进行额外条件. 不用返回值.
            queryWrapper.and(wrapper -> {

                // 增加查询条件
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });

        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        // 返回AttrResponseVo 类型 其封装了下面的所有.
        PageUtils pageList = new PageUtils(page);
        List<AttrResponseVo> responseVoList = page.getRecords().stream().map(item -> {

            AttrResponseVo attrResponseVo = new AttrResponseVo();
            // 对其结果重新进行封装.
            // 先对基本属性进行重新拷贝.
            BeanUtils.copyProperties(item, attrResponseVo);
            // 在封装一个分类名字和属性组的名字.
            // 分类ID.
            if (item.getCatelogId() != null) {

                CategoryEntity categoryEntity = categoryDao.selectById(item.getCatelogId());
                attrResponseVo.setCatelogName(categoryEntity.getName());
            }

            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectById(item.getAttrId());
            if (relationEntity != null) {

                // 分组ID需要从属性和组中间表进行查询.
                if (relationEntity.getAttrGroupId() != null) {

                    Long attrGroupId = relationEntity.getAttrGroupId();
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                    attrResponseVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            // 通过组ID获取到其分组的名字.
            // 返回的是新集合.
            return attrResponseVo;
        }).collect(Collectors.toList());

        Integer count = baseMapper.selectCount(catID_1);
        pageList.setTotalCount(count);
        // 还需要为pageUtils 设置数据库表的总数.
        pageList.setList(responseVoList);
        // 控制台打印出返回的List。

        return pageList;
    }

    @Override
    public PageUtils deleteBatchByIds(Long[] attrIds) {

        // 应该进行批量删除.


        return null;
    }

    @Override
    public PageUtils queryPageDetail(Map<String, Object> params, Long catId) {

        // 查询销售属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type", 0);
        QueryWrapper<AttrEntity> countWrapper = new QueryWrapper<>();
        countWrapper.eq("attr_type", 0);
        if (catId != 0) {

            wrapper.and(w -> {

                w.eq("catelog_id", catId);
            });
            countWrapper.eq("catelog_id", catId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {

            // 如果查询关键字不为空，那么就匹配
            wrapper.and(object -> {

                // 对查询wrapper进行模糊查询.
                object.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        // 对List集合进行遍历.
        List<AttrResponseVo> attrResponseVos = page.getRecords().stream().map(item -> {

            AttrResponseVo attrResponseVo = new AttrResponseVo();
            BeanUtils.copyProperties(item, attrResponseVo);
            // 现在需要查询出其所属的分类是哪一个分类,要具体显示器名字.
            if (item.getCatelogId() != null) {

                CategoryEntity categoryEntity = categoryDao.selectById(item.getCatelogId());
                attrResponseVo.setCatelogName(categoryEntity.getName());
            }

            return attrResponseVo;
        }).collect(Collectors.toList());


        PageUtils pageUtils = new PageUtils(page);
        Integer count = baseMapper.selectCount(countWrapper);
        pageUtils.setTotalCount(count);
        pageUtils.setList(attrResponseVos);
        return pageUtils;
    }

}