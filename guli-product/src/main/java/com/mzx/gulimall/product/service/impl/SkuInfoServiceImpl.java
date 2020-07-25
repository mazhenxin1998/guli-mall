package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.product.dao.SkuInfoDao;
import com.mzx.gulimall.product.entity.*;
import com.mzx.gulimall.product.feign.ICouponServiceFeign;
import com.mzx.gulimall.product.service.SkuImagesService;
import com.mzx.gulimall.product.service.SkuInfoService;
import com.mzx.gulimall.product.service.SkuSaleAttrValueService;
import com.mzx.gulimall.product.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private ICouponServiceFeign iCouponServiceFeign;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSkuBaseInfo(Long spuId, SpuSaveVo vo) {

        /*
         * --------------------------------------------------------
         * 商品上架接口.
         * --------------------------------------------------------
         * */

        SkuInfoEntity entity = new SkuInfoEntity();
        entity.setSpuId(spuId);
        // skuID是自增的.
        List<Skus> skus = vo.getSkus();
        // 应该先保存SKU 在保存销售属性.
        // 品牌ID和分类ID在Vo里面.
        skus.forEach(skus1 -> {

            SkuInfoEntity infoEntity = new SkuInfoEntity();
            infoEntity.setCatalogId(vo.getCatalogId());
            infoEntity.setBrandId(vo.getBrandId());
            infoEntity.setPrice(skus1.getPrice());
            // 默认销售0件
            infoEntity.setSaleCount(0L);
            // 需要取出来默认的图片.
            List<Images> images = skus1.getImages();
            List<Images> collect = images.stream().filter(item -> {

                // 返回true是有用的.
                // 只要当前的defaultImg 为1 则保留.
                return item.getDefaultImg() == 1;
            }).collect(Collectors.toList());
            // 返回的该collect集合只有一个元素.
            if (collect != null && collect.size() > 0 && collect.get(0) != null) {

                if (!StringUtils.isEmpty(collect.get(0).getImgUrl())) {

                    infoEntity.setSkuDefaultImg(collect.get(0).getImgUrl());
                } else {

                    infoEntity.setSkuDefaultImg("");
                }
            }

            // sescar 描述的是 白色+ 8GB的使用情况.
            List<String> descar = skus1.getDescar();
            String desc = "";
            for (int i = 0; i < descar.size(); i++) {

                if (i == descar.size() - 1) {

                    // 需要添加 逗号.
                    desc += descar.get(i);
                    desc += ",";
                } else {

                    // 不需要添加逗号.
                    desc += descar.get(i);
                }
            }

            infoEntity.setSkuDesc(desc);
            infoEntity.setSkuName(skus1.getSkuName());
            // 副标题有可能为空
            if (!StringUtils.isEmpty(skus1.getSkuSubtitle())) {

                infoEntity.setSkuSubtitle(skus1.getSkuSubtitle());
            }

            infoEntity.setSkuTitle(skus1.getSkuTitle());
            infoEntity.setSpuId(spuId);
            // 保存之后有主键回填.
            baseMapper.insert(infoEntity);
            // 有了SKU的ID之后在对其销售属性进行增加.
            this.saveSaleAttrAndValue(infoEntity.getSkuId(), skus1.getAttr());
            // 有了SKU的ID之后，还需要保存SKU_IMAGES属性
            this.saveSkuImages(infoEntity.getSkuId(), skus1.getImages());

            // 由于用到sku_id 所以满减等优惠信息在这里进行保存

            // 顺便也要保存该SKU的会员价格
            this.transFor(infoEntity.getSkuId(), skus1.getMemberPrice());

            // 保存满几件打多少折扣
            SmsSkuLadderEntity ladderEntity = new SmsSkuLadderEntity();
            ladderEntity.setSkuId(infoEntity.getSkuId());
            BigDecimal price = skus1.getPrice().multiply(skus1.getDiscount());
            ladderEntity.setPrice(price);
            ladderEntity.setSkuId(infoEntity.getSkuId());
            ladderEntity.setFullCount(skus1.getFullCount());
            ladderEntity.setDiscount(skus1.getDiscount());
            iCouponServiceFeign.saveSkuLadder(ladderEntity);
            // 保存花费多少钱,抵消多小一定金额
            SmsSkuFullReductionEntity fullReductionEntity = new SmsSkuFullReductionEntity();
            fullReductionEntity.setFullPrice(skus1.getFullPrice());
            fullReductionEntity.setReducePrice(skus1.getReducePrice());
            fullReductionEntity.setSkuId(infoEntity.getSkuId());
            iCouponServiceFeign.saveFullPriceReducePrice(fullReductionEntity);

        });

    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> wrapper = this.getWrapper(params);

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        Integer count = baseMapper.selectCount(wrapper);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    private QueryWrapper<SkuInfoEntity> getWrapper(Map<String, Object> params) {

        /*
         * --------------------------------------------------------
         * String类型不能转换为Long类型.
         * --------------------------------------------------------
         * */

        String key = (String) params.get("key");
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {

            wrapper.and(w -> {

                w.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        Object catelogId = params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId.toString())) {

            // 如果只是用条件大于0进行判断，
            // long的默认值为0.
            long cate_id = Long.parseLong(catelogId.toString());
            if (cate_id > 0) {

                wrapper.and(w -> {

                    w.eq("catalog_id", cate_id);
                });
            }

        }

        Object brandId = params.get("brandId");
        if (!StringUtils.isEmpty(brandId.toString())) {

            long brand_id = Long.parseLong(brandId.toString());
            if (brand_id > 0) {

                wrapper.and(w -> {

                    w.eq("brand_id", brand_id);
                });
            }

        }

        // TODO 价格区间查找暂时未实现.
        // 每一个SKU查询的时候都有价格区间的.

        return wrapper;
    }


    public void transFor(Long skuId, List<MemberPrice> memberPrices) {

        List<SmsMemberPriceEntity> collect = memberPrices.stream().map(item -> {

            SmsMemberPriceEntity entity = new SmsMemberPriceEntity();
            // 有可能为空
            entity.setSkuId(skuId);
            if (item.getId() != null) {

                entity.setMemberLevelId(item.getId());
            }

            if (item.getPrice() != null) {

                entity.setMemberPrice(item.getPrice());
            }

            if (item.getName() != null) {

                entity.setMemberLevelName(item.getName());
            }

            return entity;
        }).collect(Collectors.toList());
        if (collect == null) {

            System.out.println("collect是空的");
        }
        // 这个远程接口是空的....
        iCouponServiceFeign.saveMemberPriceBatch(collect);

    }

    private void saveSkuImages(Long skuId, List<Images> images) {

        // 针对每一个SKU进行保存图片集.
        // 还要挑出默认的图片.
        List<SkuImagesEntity> collect = images.stream().map(item -> {

            // 1 是默认
            SkuImagesEntity imagesEntity = new SkuImagesEntity();
            imagesEntity.setDefaultImg(item.getDefaultImg());
            imagesEntity.setImgUrl(item.getImgUrl());
            imagesEntity.setSkuId(skuId);
            return imagesEntity;
        }).collect(Collectors.toList());

        skuImagesService.saveBatch(collect);
    }

    private void saveSaleAttrAndValue(Long skuID, List<Attr> attrs) {

        List<SkuSaleAttrValueEntity> collect = attrs.stream().map(item -> {

            SkuSaleAttrValueEntity valueEntity = new SkuSaleAttrValueEntity();
            valueEntity.setSkuId(skuID);
            valueEntity.setAttrId(item.getAttrId());
            valueEntity.setAttrName(item.getAttrName());
            valueEntity.setAttrValue(item.getAttrValue());
            return valueEntity;
        }).collect(Collectors.toList());

        // 批量保存
        skuSaleAttrValueService.saveBatch(collect);

    }

}