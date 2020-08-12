package com.mzx.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.product.dao.CategoryBrandRelationDao;
import com.mzx.gulimall.product.dao.CategoryDao;
import com.mzx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.mzx.gulimall.product.entity.CategoryEntity;
import com.mzx.gulimall.product.service.CategoryService;
import com.mzx.gulimall.product.vo.web.Catalog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    /**
     * 模拟本地缓存.
     */
    private Map<String, Object> caches = new ConcurrentHashMap<>();

    @Autowired
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
            // 返回false 则将其过滤掉.
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateDetail(CategoryEntity category) {

        String categoryName = category.getName();
        if (!StringUtils.isEmpty(categoryName)) {

            // 如果修改的是categoryName则会中间表进行修改.
            baseMapper.updateById(category);
            // 额外的增加对中间表的修改操作，但是此操作会设计的事务的处理.
            CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
            relationEntity.setCatelogId(category.getCatId());
            relationEntity.setCatelogName(categoryName);
            categoryBrandRelationDao.updateByCategoryId(relationEntity);
        } else {

            // 如果修改的不包含三级分类的名字,那么正常修改即可.
            baseMapper.updateById(category);
        }

    }

    @Override
    public Map<String, List<Catalog2Vo>> findAllCatagory() {

        /*
         * --------------------------------------------------------
         * 先从缓存中取出数据，如果缓存中没有数据,再从数据库中查询.
         * --------------------------------------------------------
         * */

        // TODO 可能会出现堆外溢出现象.
        // 解决办法： Redis目前还没有解决对外内存溢出,而我们可以升级客户端来进行防范.
        // 将lettuce转换为jedis ----> 修改pom依赖.
        // 不用修改具体的配置，只需要修改Redis底层的客户端即可,SpringBoot自动配置.
        // TODO 缓存穿透: 某一时刻，100w用户进行访问，但是访问的key在redis中没有，从数据库中查询的也没有.(非法访问)
        /*
         * --------------------------------------------------------
         * 就是说某一时刻大量用户进来访问，但是访问的数据在缓存中没有，在DB中也
         * 没有，这就造成了所有的用户都回去访问DB，造成DB的压力瞬时间就增大几倍
         * 有可能造成DB服务器崩溃.
         *      但是，不存在的用户，我们没有必要一直去DB中查询，我们可以将在数
         * 据库中不存在的数据进行null存在缓存中，就算再来100w个非法访问的请求
         * 也没有任何关系. 因为只有一个用户回去访问DB，将空值存入缓存中的时候,
         * 一定要加上过期时间.
         *      由于这里业务的特殊性质,所以应该不会出现缓存穿透的现象.如果出现
         * 了，则按照刚才测试的方法进行保护.
         * --------------------------------------------------------
         * */

        // TODO: 解决了缓存穿透,现在又面临着缓存雪崩问题。在某一时刻,大面积的Key集体失效，而之后的某个时间点有100w+的用户进行并发访问,
        //  而我们的设计思路是先从缓存中取出数据,如果取不到再从DB中查询,但是100w+用户访问过期的数据,会导致DB的压力骤增至宕机,同时Redis
        //  的大量请求会积压,开始出现超时现象导致Redis服务宕机.
        // TODO: 解决办法是为每一个缓存加一个随机数的缓存时间.

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // JSON类型
        String catagoryJson = ops.get("catagoryJson");
        if (!StringUtils.isEmpty(catagoryJson)) {

            System.out.println("从缓存中读取数据.");
            // 表示当前有缓存.
            // 将缓存转换为Map<String,List<Catalog2Vo>>类型
            Map<String, List<Catalog2Vo>> listMap = JSON.parseObject(catagoryJson,
                    new TypeReference<Map<String, List<Catalog2Vo>>>() {});
            return listMap;
        } else {

            System.out.println("从DB中读取数据");
            // 我每次查询一次DB,都要将其结果放入缓存,即使该结果为空.
            Map<String, List<Catalog2Vo>> listMap = this.findAllCatagoryDB();
            // 获取之后再从Redis缓存中获取.
            ops.set("catagoryJson", JSON.toJSONString(listMap));
            return listMap;

        }


    }

    public Map<String, List<Catalog2Vo>> findAllCatagoryDB() {

        /*
         * --------------------------------------------------------
         * 为了节省查询分类的时间,那么一次性从数据库查出所有的数据，通过JAVA，
         * 来组装要返回的Map.
         * --------------------------------------------------------
         * */

        // 如果没有缓存就从数据库里面进行查询.
        //查询出的所有分类.
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        Map<String, List<Catalog2Vo>> models = new ConcurrentHashMap<>();
        List<Catalog2Vo> list = new ArrayList<>();
        categoryEntities.forEach(item -> {

            // 遍历每一个分类,如果该分类是一级分类则
            if (item.getParentCid() == 0) {

                // 如果父分类ID是0 则表示该分类是一级分类.
                // 再次进行遍历吗？
                List<Catalog2Vo> catalog2Vos = new ArrayList<>();
                categoryEntities.forEach(obj -> {

                    if (obj.getParentCid().equals(item.getCatId())) {

                        Catalog2Vo catalog2Vo = new Catalog2Vo(
                                item.getCatId().toString(), null, obj.getCatId().toString(), obj.getName());
                        List<Catalog2Vo.Catalog3Vo> catalog3Vos = new ArrayList<>();
                        categoryEntities.forEach(o -> {

                            if (o.getParentCid().equals(obj.getCatId())) {

                                // 这里封装每一个二级分类的所有子分类.
                                Catalog2Vo.Catalog3Vo catalog3Vo = new Catalog2Vo.Catalog3Vo(
                                        obj.getCatId().toString(), o.getCatId().toString(), o.getName());
                                catalog3Vos.add(catalog3Vo);

                            }

                        });

                        // 这里为catalog2Vo设置List的值.
                        catalog2Vo.setCatalog3List(catalog3Vos);
                        catalog2Vos.add(catalog2Vo);
                    }

                });

                models.put(item.getCatId().toString(), catalog2Vos);
            }

        });

        // 存的时候存的是String类型的字符串.
        return models;
    }

    @Override
    public List<CategoryEntity> findOneCategoryList() {

        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", "0"));
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