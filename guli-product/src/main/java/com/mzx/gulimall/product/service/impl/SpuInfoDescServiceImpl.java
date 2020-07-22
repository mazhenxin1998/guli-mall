package com.mzx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.product.dao.SpuInfoDescDao;
import com.mzx.gulimall.product.entity.SpuInfoDescEntity;
import com.mzx.gulimall.product.service.SpuInfoDescService;
import com.mzx.gulimall.product.vo.SpuSaveVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSpuDesc(Long spuId, SpuSaveVo vo) {

        List<String> decript = vo.getDecript();
        // 通过decript批量将其保存在数据库中.
        decript.forEach(item -> {

            SpuInfoDescEntity entity = new SpuInfoDescEntity();
            entity.setDecript(item);
            entity.setSpuId(spuId);
            // 进行保存.
            baseMapper.insert(entity);
        });

    }

}