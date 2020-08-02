package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.mzx.gulimall.product.dao.AttrDao;
import com.mzx.gulimall.product.dao.AttrGroupDao;
import com.mzx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.mzx.gulimall.product.entity.AttrEntity;
import com.mzx.gulimall.product.entity.AttrGroupEntity;
import com.mzx.gulimall.product.exception.AttrErrorCode;
import com.mzx.gulimall.product.exception.CustomException;
import com.mzx.gulimall.product.service.AttrAttrgroupRelationService;
import com.mzx.gulimall.product.service.AttrGroupService;
import com.mzx.gulimall.product.service.AttrService;
import com.mzx.gulimall.product.vo.AttrGroupWithAttrVo;
import com.mzx.gulimall.product.vo.AttrRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private AttrService attrService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {

        // 查询的时候带上catelog=catId 这个选项.
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {

            wrapper.and(obj -> {

                obj.eq("attr_group_id", key).or().eq("attr_group_name", key);
            });

        }


        if (0 == catId) {

            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            Integer count = baseMapper.selectCount(new QueryWrapper<>());
            // 如果查询全部，则可以用这个count.
            PageUtils pageUtils = new PageUtils(page);
            pageUtils.setTotalCount(count);
            return pageUtils;
        } else {

            wrapper.eq("catelog_id", catId);
            // 查询
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            PageUtils pageUtils = new PageUtils(page);
            Integer count = baseMapper.selectCount(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
            System.out.println("查询同一个分类下分组的总数: " + count);
            pageUtils.setTotalCount(count);
            return pageUtils;
        }

    }


    @Override
    public R queryAttrDetail(Map<String, Object> params, Long attrgroupId) {

        if (attrgroupId <= 0) {

            // 表示参数错误抛出异常即可.
            throw new CustomException(AttrErrorCode.ATTR_GROUP_ID_IS_NULL);
        }
        /*
         * 1. 查询的属性只能是该分类下的属性
         * 2. 可选关联的属性只能是别的属性组里main没有关联的值.
         * */

        // 精确查出当前分组属于那个分类.
        // 然后对该分类进行查询.
        // 查出 手机分组.
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);
        // 通过分组实体类获取到该分组属于那个分类下.
        // 手机分类255
        Long catelogId = attrGroupEntity.getCatelogId();
        // 当前分组只能关联到别的分组中没有引用到的属性.
        // 1. 当前分类下的其他分组. 这里应该从attr属性表中查询 不是attrGroup属性表中查询.
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.and(w -> {

            w.eq("catelog_id", catelogId);
        });
        wrapper.and(w -> {

            w.ne("attr_group_id", attrgroupId);
        });

        List<AttrGroupEntity> groupEntities = baseMapper.selectList(wrapper);
        // 2. 这些分组关联的属性. 增加和删除 这里用LinkedList和ArrayList都差不多吧.
        // 返回的属性就是该分类下所有分组所关联的属性.
        List<List<AttrEntity>> attrEntities = groupEntities.stream().map(item -> {

            // 每一个item表示的是一个分组，而该分组是与当前分类有关系的.
            // 现在我要将该分组下的所有属性查询出来.
            // 通过分组ID查询出该分组关联的所有属性.
            // 一个分组ID对应着多个属性.
            List<AttrAttrgroupRelationEntity> attrgroupRelationEntities = attrAttrgroupRelationDao.selectList(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",
                            item.getAttrGroupId()));
            // attrgroupRelationEntities 集合里面包含的就是item分组所关联的属性ID.
            // 返回的一个entities表示的是一个分组下的相关属性.
            List<AttrEntity> entities = attrgroupRelationEntities.stream().map(obj -> {

                // 从中间表中获取到属性ID.
                // 这里的属性表示的是中间表的数据.
                Long attrId = obj.getAttrId();
                // 这里需要对查询条件进行增加： 就是这里查询到的都是规格参数属性.
                // 0是销售属性.
//                return attrDao.selectOne(
//                        new QueryWrapper<AttrEntity>().eq("attr_id", attrId).eq("attr_type", 1));
                AttrEntity entity = attrDao.selectById(attrId);
                return entity;
            }).collect(Collectors.toList());

            return entities;
        }).collect(Collectors.toList());

        // 3. 从当前分类属性中移除这些属性.
        // 通过分类ID查出该分类下的所有属性，然后将上面查出的属性进行过滤.
        List<AttrEntity> attrs = attrDao.selectList(new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId));
        List<AttrEntity> attrEntityList = attrs.stream().filter(item -> {

            // 如果返回false则将其过滤掉.
            AtomicBoolean flag = new AtomicBoolean(true);
            // 进行过滤
            attrEntities.forEach(obj -> {

                // 进行遍历 每一个obj都是一个List.
                if (obj.contains(item)) {

                    flag.set(false);
                }
            });

            return flag.get();
        }).collect(Collectors.toList());

        // attrEntities 就是要返回的数据.
        return R.ok().put("page", attrEntities);
    }

    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {

        // TODO 目前查出来的是当前分类下所有的属性.

        // 先查出该分组的基本信息
        AttrGroupEntity groupEntity = baseMapper.selectById(attrGroupId);
        // 获取该分组在那个分类下.
        Long catelogId = groupEntity.getCatelogId();
        // TODO 查询当前分类下的其他属性组.  这里是有问题.
        List<AttrGroupEntity> group = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",
                catelogId).ne("catelog_id", catelogId));
        // 先获取该分类其他分组ID.
        List<Long> collect = group.stream().map(item -> {

            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        // collect 可能为空.
        if (collect != null && collect.size() > 0) {

            // 该分类下还有其他分组.
            List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
            // 其他分组所有关联的属性.
            List<Long> attrIds = relationEntities.stream().map(item -> {

                return item.getAttrId();
            }).collect(Collectors.toList());

            QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).
                    eq("attr_type", 1);
            if (attrIds != null && attrIds.size() > 0) {

                queryWrapper.notIn("attr_id", attrIds);
            }
            // 查询这些关联的属性.
            IPage<AttrEntity> page = attrService.page(
                    new Query<AttrEntity>().getPage(params),
                    queryWrapper
            );
            PageUtils pageUtils = new PageUtils(page);
            return pageUtils;
        } else {

            // 现在问题是 销售属性也关联上来了  这里只关联参数属性.
            // 先看看groupID关联了那些属性
            List<AttrAttrgroupRelationEntity> attrRelations = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_group_id", attrGroupId));
            // 该分类下已经关联的属性.
            List<Long> attrIds = attrRelations.stream().map(item -> {

                return item.getAttrId();
            }).collect(Collectors.toList());

            QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).
                    eq("attr_type", 1);
            if (attrIds != null && attrIds.size() > 0) {

                wrapper.notIn("attr_id", attrIds);
            }
            IPage<AttrEntity> page = attrService.page(
                    new Query<AttrEntity>().getPage(params),
                    wrapper
            );


            // 所有属性都能关联.
            // 查询出当前分类下的所有属性

            //
            return new PageUtils(page);
        }

    }

    @Override
    public PageUtils getRelationAttr(Map<String, Object> params, Long attrgroupId) {

        if (attrgroupId == null || attrgroupId < 0) {

            throw new CustomException(AttrErrorCode.ATTR_GROUP_ID_IS_NULL);
        }

        // 业务的正常执行.
        // 查询该属性组下所关联的所有属性进行展示.

        // 查询该分类下所有属性的ID.
        // 这里查询的是规格参数 销售属性是0
        List<AttrAttrgroupRelationEntity> attrId = attrAttrgroupRelationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().
                        eq("attr_group_id", attrgroupId));
        System.out.println("该分组下的属性集合1: " + attrId.toString());
        List<Long> attrIds = attrId.stream().map(item -> {

            return item.getAttrId();
        }).collect(Collectors.toList());
        System.out.println("该分组下属性ID的集合: " + attrIds.toString());
        // 前提是attrIds 不为空
        QueryWrapper<AttrEntity> queryWrapper1 = new QueryWrapper<AttrEntity>();
        queryWrapper1.and(w -> {

            w.eq("attr_type", 1);
        });

        if (attrIds != null && attrIds.size() > 0) {

            queryWrapper1.and(obj -> {

                obj.in("attr_id", attrIds);
            });
        } else {

            return null;
        }

        // 空的情况下是查所有 应该是直接返回空
        IPage<AttrEntity> pageAttr = attrService.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper1
        );

        System.out.println(pageAttr.getRecords().toString());

        return new PageUtils(pageAttr);
    }

    @Override
    public R saveAttrGroupRelation(List<AttrRelationVo> vos) {

        /*实现将属性和属性分组进行中间表的关联.*/
        List<AttrAttrgroupRelationEntity> entities = vos.stream().map(item -> {

            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            entity.setAttrGroupId(item.getAttrGroupId());
            entity.setAttrId(item.getAttrId());
            return entity;
        }).collect(Collectors.toList());

        entities.forEach(obj -> {

            // 进行批量插入.
            attrAttrgroupRelationDao.insert(obj);
        });


        return R.ok();
    }

    @Override
    public List<AttrGroupWithAttrVo> getGroupAndAttr(Long catelogId) {

        if (catelogId == null || catelogId <= 0) {

            // 抛出自定义异常.
            throw new CustomException(AttrErrorCode.ATTR_GROUP_ID_IS_NULL);
        }

        /*根据分类ID查询出该分类下的所有分组以及所有属性.*/
        // 先查询出该分类下的所有分组.
        List<AttrGroupEntity> groupEntities = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        List<AttrGroupWithAttrVo> response = groupEntities.stream().map(item -> {

            AttrGroupWithAttrVo vo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(item, vo);
            // 单独设置attrs.
            // 通过分组ID查询该分组下所有关联的属性ID.
            List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", item.getAttrGroupId()));
            // 通过该中间表查询出该分组所关联的所有属性
            List<AttrEntity> attrEntities = relationEntities.stream().map(obj -> {

                Long attrId = obj.getAttrId();
                AttrEntity attrEntity = attrDao.selectById(attrId);
                if (attrEntity != null) {
                    return attrEntity;
                }
                return null;
            }).collect(Collectors.toList());

            ArrayList<AttrEntity> arrayList = new ArrayList<>();
            relationEntities.forEach(obj -> {

                if (obj.getAttrId() != null) {

                    AttrEntity attrEntity = attrDao.selectById(obj.getAttrId());
                    if (attrEntity != null) {

                        arrayList.add(attrEntity);
                    }
                }

            });


            vo.setAttrs(arrayList);
            return vo;

        }).collect(Collectors.toList());

        System.out.println("1");

        return response;
    }

}