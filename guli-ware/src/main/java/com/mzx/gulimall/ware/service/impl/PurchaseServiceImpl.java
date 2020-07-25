package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.dao.PurchaseDao;
import com.mzx.gulimall.ware.entity.PurchaseDetailEntity;
import com.mzx.gulimall.ware.entity.PurchaseEntity;
import com.mzx.gulimall.ware.service.PurchaseDetailService;
import com.mzx.gulimall.ware.service.PurchaseService;
import com.mzx.gulimall.ware.vo.PurchaseMergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> wrapper = this.getQueryWrapper(params);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    @Override
    public PageUtils queryPurchaseDetails(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> wrapper = this.getNewAndNotQueryWrapper(params);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper);

        return new PageUtils(page);
    }

    @Override
    public R saveMerge(PurchaseMergeVo vo) {

        /*
         * --------------------------------------------------------
         * 采购需求合并成一个采购单.
         * Vo中Items是采购需求的ID.
         * --------------------------------------------------------
         * */

        // 采购需求状态标志: 0是新建的 1是已分配的.

        Long purchaseId = vo.getPurchaseId();
        if (purchaseId <= 0) {

            // 表示需要新建一个采购单
            PurchaseEntity entity = new PurchaseEntity();
            Date date = new Date();
            entity.setCreateTime(date);
            entity.setUpdateTime(date);
            // 主键回填.
            this.save(entity);
            purchaseId = entity.getId();
        }

        // 在purchase_details表中为每一个采购需求增加一个采购单，并且设置状态为已经分配
        List<Long> items = vo.getItems();
        Long p = purchaseId;
        items.forEach(item -> {

            // item就是每一个采购需求的ID.
            // 修改每一个采购需求.
            // 要修改的每一个实体类.
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setId(item);
            entity.setPurchaseId(p);
            entity.setStatus(1);
            purchaseDetailService.updateById(entity);
        });


        return R.ok();
    }

    private QueryWrapper<PurchaseEntity> getNewAndNotQueryWrapper(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();

        // 只查询新建状态或者已经完成的.
        // 新建的状态为0
        wrapper.eq("status", "0").or().eq("status","1");

        return wrapper;

    }


    private QueryWrapper<PurchaseEntity> getQueryWrapper(Map<String, Object> params) {

        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();

        Object key = params.get("key");

        if (key != null) {

            if (!StringUtils.isEmpty(key)) {

                wrapper.and(wr -> {

                    wr.eq("assignee_id", key).or().like("assignee_name", key);
                });

            }

        }

        Object status = params.get("status");
        if (status != null) {

            if (!StringUtils.isEmpty(status.toString())) {

                wrapper.and(wr -> {

                    wr.eq("status", status);
                });

            }

        }

        return wrapper;
    }

}