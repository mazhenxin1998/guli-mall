package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.dao.SpuInfoDao;
import com.mzx.gulimall.product.entity.*;
import com.mzx.gulimall.product.feign.ICouponServiceFeign;
import com.mzx.gulimall.product.feign.ISearchServiceFeign;
import com.mzx.gulimall.product.feign.IWareServiceFeign;
import com.mzx.gulimall.product.service.*;
import com.mzx.gulimall.product.vo.Attr;
import com.mzx.gulimall.product.vo.Bounds;
import com.mzx.gulimall.product.vo.SpuSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private ICouponServiceFeign ICouponServiceFeign;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private IWareServiceFeign iWareServiceFeign;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ISearchServiceFeign iSearchServiceFeign;

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public R saveSpuDetails(SpuSaveVo vo) {

        /*将每一步的结果进行并运算最后的结果为true才返回增加成功!
         * 先完成功能，在做细节处理.
         * */

        // 1. 保存SPU基本信息: pms_spu_info
        Long spuId = this.saveSpuBaseInfo(vo);
        // 2. 保存SPU的描述图片集  pms_spu_desc
        spuInfoDescService.saveSpuDesc(spuId, vo);
        // 3. 保存spu的图片集 pms_spu_images
        // 以SPUName作为Name保存图片.
        spuImagesService.saveSpuImages(spuId, vo);
        // 4. 保存spu的积分信息 : gulimall_sms 需要跨服务进行引用.
        // 就是第一步里面的金币和成长值.
        // 5. 跨服务保存SPU的积分信，就是Bounds的信息进行保存.
        this.saveSpuBounds(spuId, vo);
        // 6. 保存当前spu对应的所有SKU信息
        // 6.1 sku的基本信息  pms_sku_info
        skuInfoService.saveSkuBaseInfo(spuId, vo);
        // 6.2 sku的图片信息  pms_sku_images
        // 再上一步就完成了.
        // 6.3 sku的销售属性   pms_sku_salel_attr_value
        // 规格属性保存了吗？
        // 上上一步就完成了.
        // 6.4 sku的优惠券 满减等信息  跨服务进行保存.
        // 积分和优惠券需要跨服务进行保存.
        return R.ok();
    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {

        /*
         * --------------------------------------------------------
         * 从数据库查询的时候不用放在事务里面。.
         * --------------------------------------------------------
         * */

        // 构造查询条件
        QueryWrapper<SpuInfoEntity> wrapper = this.getWrapper(params);

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        //  还需要查出该总的数目.
        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void up(Long spuId) {

        /*SPU上架.*/
        // ES索引库中每一行就是一个SKU.
        // TODO 在写这个项目的时候查询不用根据规格参数进行查询,但是EsModel暂时没有删除List属性，先就暂时存着吧.
        SpuInfoEntity spuInfoEntity = baseMapper.selectById(spuId);
        List<SkuInfoEntity> skus = skuInfoService.findAllBySpuId(spuId);
        List<SkuEsModel> esModels = skus.stream().map(sku -> {

            SkuEsModel model = new SkuEsModel();
            model.setSpuId(spuId);
            model.setSpuName(spuInfoEntity.getSpuName());
            model.setSkuId(sku.getSkuId());
            model.setSkuTitle(sku.getSkuTitle());
            model.setSkuPrice(sku.getPrice());
            model.setSkuImg(sku.getSkuDefaultImg());
            // 刚上架默认为0.
            model.setSaleCount(0L);
            // 当前仓库是否还有货.
            model.setHasStock(iWareServiceFeign.getStock(sku.getSkuId()));
            model.setHotScore(99L);

            // 查询品牌ID,品牌名字，品牌图片，
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            model.setBrandId(sku.getBrandId());
            model.setBrandName(brandEntity.getName());
            model.setBrandImg(brandEntity.getLogo());

            // 这里就没有保存Attr属性.
            // TODO: 通过分类ID查询出该分类下所关联的所有属性.
            Long catalogId = sku.getCatalogId();
            List<Attr> attrs = attrService.findAttrsByCatalogId(catalogId);
            List<SkuEsModel.Attrs> attrsList = new ArrayList<>();
            BeanUtils.copyProperties(attrs, attrsList);
            model.setAttrs(attrsList);
            // 设置分类ID，分类名字.
            CategoryEntity categoryEntity = categoryService.getById(sku.getCatalogId());
            model.setCatalogId(sku.getCatalogId());
            model.setCatalogName(categoryEntity.getName());

            return model;
        }).collect(Collectors.toList());

        // 将esModels调用远程search服务保存至ES服务中.
        R r = iSearchServiceFeign.saveProduct(esModels);
        // 远程调用ES服务成功之后才修改状态码.
        if (r.getCode() == 0) {

            // 说明调用ES服务成功增加了.
            // 保存之后,还需要修改数据库SPU的发布状态
            log.info("成功从ES服务中返回...");
            SpuInfoEntity infoEntity = new SpuInfoEntity();
            infoEntity.setId(spuId);
            // 将其设置为发布状态.
            infoEntity.setPublishStatus(1);
            baseMapper.updateById(infoEntity);
            log.info("商品SPU{}上架成功", spuId);
        } else {

            log.error("SPU{} 上架失败由于调用ES服务出现错误", spuId);
        }

    }


    private QueryWrapper<SpuInfoEntity> getWrapper(Map<String, Object> params) {

        /*
         * --------------------------------------------------------
         * 需要注意的是如果params里面只有三个参数则表示的是查询所有.
         * --------------------------------------------------------
         * */

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (params.size() == 3) {

            // 查询的是所有 返回一个空的查询条件即可.
            return wrapper;
        }

        /*
         * --------------------------------------------------------
         * String类型不能转换为Integer类型..
         * --------------------------------------------------------
         * */
        // 应该先判断下》
        if (!StringUtils.isEmpty(params.get("status").toString())) {

            int status = Integer.parseInt(params.get("status").toString());
            if (status > 0) {

                // 各个分支条件应该用and连接.
                wrapper.and(w -> {

                    w.eq("publish_status", status);
                });
            }

        }

        // String类型转换为Long 应该会报错.
        Object brandId = params.get("brandId");
        if (!StringUtils.isEmpty(brandId.toString())) {

            long brand_id = Long.parseLong(brandId.toString());
            if (brand_id > 0) {

                wrapper.and(w -> {

                    w.eq("brand_id", brand_id);
                });
            }

        }

        if (!StringUtils.isEmpty(params.get("catelogId").toString())) {

            long catelogId = Long.parseLong(params.get("catelogId").toString());
            if (catelogId > 0) {

                wrapper.and(w -> {

                    w.eq("catalog_id", catelogId);
                });
            }

        }

        // Key所对应的字段为SPU的ID或者为SPU的name
        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {

            wrapper.and(w -> {

                w.eq("id", key).or().like("spu_name", key);
            });
        }

        return wrapper;
    }


    /**
     * 对SPU基本信息的存储.
     *
     * @param vo
     * @return
     */
    private Long saveSpuBaseInfo(SpuSaveVo vo) {

        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        // 拷贝之后还需要设置创建时间和更新时间.
        Date time = new Date();
        spuInfoEntity.setCreateTime(time);
        spuInfoEntity.setUpdateTime(time);


        // 保存SPU的基本信息即可.
        baseMapper.insert(spuInfoEntity);
        return spuInfoEntity.getId();
    }

    /**
     * 从VO中抽出积分信息进行保存.
     *
     * @param spuId
     * @param vo
     */
    private void saveSpuBounds(Long spuId, SpuSaveVo vo) {

        SmsSpuBoundsEntity entity = new SmsSpuBoundsEntity();
        entity.setSpuId(spuId);
        Bounds bounds = vo.getBounds();
        entity.setBuyBounds(bounds.getBuyBounds());
        entity.setGrowBounds(bounds.getGrowBounds());
        // 远程调用进行保存.
        // 这里没有做分布式事务的控制,也没有做服务降级.
        ICouponServiceFeign.save(entity);
    }

}