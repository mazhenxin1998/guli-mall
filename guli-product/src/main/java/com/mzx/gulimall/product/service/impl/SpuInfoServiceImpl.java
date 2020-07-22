package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.product.dao.SpuInfoDao;
import com.mzx.gulimall.product.entity.SmsSpuBoundsEntity;
import com.mzx.gulimall.product.entity.SpuInfoEntity;
import com.mzx.gulimall.product.feign.ICouponServiceFeign;
import com.mzx.gulimall.product.service.*;
import com.mzx.gulimall.product.vo.Bounds;
import com.mzx.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


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
        // 上上一步就完成了.
        // 6.4 sku的优惠券 满减等信息  跨服务进行保存.
        // 积分和优惠券需要跨服务进行保存.
        return R.ok();
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