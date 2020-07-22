package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.product.dao.SpuImagesDao;
import com.mzx.gulimall.product.entity.SpuImagesEntity;
import com.mzx.gulimall.product.service.SpuImagesService;
import com.mzx.gulimall.product.vo.SpuSaveVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuImages(Long spuId, SpuSaveVo vo) {


        List<String> images = vo.getImages();
        List<SpuImagesEntity> collect = images.stream().map(image -> {

            SpuImagesEntity entity = new SpuImagesEntity();
            // 视屏上只是保存了SPU的ID和URL.
            entity.setSpuId(spuId);
            entity.setImgUrl(image);
            entity.setImgName(vo.getSpuName());
            return entity;
        }).collect(Collectors.toList());

        this.saveBatch(collect);

    }

}