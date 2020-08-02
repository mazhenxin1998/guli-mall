package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.ware.dao.PurchaseDetailDao;
import com.mzx.gulimall.ware.entity.PurchaseDetailEntity;
import com.mzx.gulimall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDedatils(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> wrapper = this.getQueryWrapper(params);
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    @Override
    public List<PurchaseDetailEntity> findPurchaseDetailsByPurchaseId(Long id) {

        if (id > 0) {

            return baseMapper.selectList(
                    new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
        }

        return null;
    }


    private QueryWrapper<PurchaseDetailEntity> getQueryWrapper(Map<String, Object> params) {

        /*
         * --------------------------------------------------------
         * params最少偶遇四个参数.
         * --------------------------------------------------------
         * */
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        Object w = params.get("wareId");
        if (w != null) {

            if (!StringUtils.isEmpty(w.toString())) {

                long wareId = Long.parseLong(w.toString());
                wrapper.and(wr -> {

                    System.out.println("标记处方法执行了");
                    wr.eq("ware_id", wareId);
                });

            }

        }


        // status
        Object status = params.get("status");
        if (status != null) {

            if (!StringUtils.isEmpty(status.toString())) {

                int status_int = Integer.parseInt(status.toString());
                wrapper.and(wr -> {

                    wr.eq("status", status_int);
                });

            }

        }


        // key
        Object key = params.get("key");
        if (key != null) {

            if (!StringUtils.isEmpty(key)) {

                wrapper.and(wr -> {

                    wr.eq("id", key).or().eq("purchase_id", key);
                });

            }

        }

        return wrapper;

    }

}