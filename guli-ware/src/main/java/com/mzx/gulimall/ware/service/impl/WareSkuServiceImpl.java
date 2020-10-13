package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.dao.WareOrderTaskDao;
import com.mzx.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.mzx.gulimall.ware.dao.WareSkuDao;
import com.mzx.gulimall.ware.entity.WareOrderTaskEntity;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.service.WareSkuService;
import com.mzx.gulimall.ware.vo.LockStockResult;
import com.mzx.gulimall.ware.vo.SkuWareStockTo;
import com.mzx.gulimall.ware.vo.WareSkuLockVo;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;

    @Autowired
    private WareOrderTaskDao wareOrderTaskDao;

    @Autowired
    private WareOrderTaskDetailDao wareOrderTaskDetailDao;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageDetails(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wrapper = this.getQueryWrapper(params);

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        Integer count = baseMapper.selectCount(wrapper);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setTotalCount(count);

        return pageUtils;
    }

    @Override
    public boolean getStock(Long skuId) {

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuId);
        List<WareSkuEntity> entities = baseMapper.selectList(wrapper);
        // 考虑到由于不同仓库存有相同的货物，所以说根据SKU查询的结果为一个集合
        // 现在要做的就是但凡一个仓库有存货就返回true.
        boolean flag = false;
        for (int i = 0; i < entities.size(); i++) {

            if (entities.get(i).getStock() > 0) {

                // 这里不需要再添加break了,直接返回就行.
                flag = true;
                return true;
            }

        }

        return flag;
    }

    @Override
    public R getListStock(List<Long> ids) {

        // 查询每个Sku对应的库存.
        if (ids == null || ids.size() <= 0) {

            return R.ok();

        } else {

            List<SkuWareStockTo> list = wareSkuDao.listFindStock(ids);
            return R.ok().put("data", list);

        }

    }

    @Override
    public List<LockStockResult> lockStock(WareSkuLockVo wareSkuLockVo) {

        // 一个订单对应着一个taskEntity.
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        // 保存库存工作单.
        wareOrderTaskDao.insert(taskEntity);
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,
                false);
        WareSkuDao mapper = sqlSession.getMapper(WareSkuDao.class);
        ArrayList<LockStockResult> results = new ArrayList<>();
        try {

            // TODO: 为什么lambada表达式只能引用'final'语义的变量.
            wareSkuLockVo.getLocks().stream().forEach(item -> {

                // TODO: 2020/10/9 根据仓库查询不做了.SQL如果是一次性的话就没有必要的.
                LockStockResult lockStockResult = new LockStockResult();
                // 远程锁定的时候还需要确定锁定那个仓库的.
                // 返回一个就行.
                mapper.lockStock(item);
                // TODO: 2020/10/12 每次对一个SKU的库存进行锁定,那么应该对锁定工作单详情来进行增加.
                // 顺便在发送给MQ进行库存自动解锁的队列.
                lockStockResult.setLocked(true);
                lockStockResult.setNum(item.getNum());
                lockStockResult.setSkuId(item.getSkuId());
                results.add(lockStockResult);
                // 每次锁定一次, 应该保存库存锁定单详情.
                //

            });
            sqlSession.commit();

        } catch (Exception e) {

            // 清空返回列表中的所有元素.
            // 如果有一个出现错误,那么就重新全部设置下值.
            results.clear();
            wareSkuLockVo.getLocks().stream().forEach(item -> {

                LockStockResult lockStockResult = new LockStockResult();
                lockStockResult.setLocked(false);
                lockStockResult.setNum(item.getNum());
                lockStockResult.setSkuId(item.getSkuId());
                results.add(lockStockResult);

            });
            log.error("批量更新锁定库存的出现error: {}", e);
            e.printStackTrace();
            // 这里返回的和上面进行返回的构造的数据是不一样的.

        } finally {

            // 不管修改成功还是失败,都应该将创建出来的对象进行关闭.
            // 其实这里应该是不用对其进行null值设置的,因为方法执行完毕,对象将不会存在GcRoots上.JVM会自动进行GC.
            sqlSession = null;

        }

        return results;

    }

    private QueryWrapper<WareSkuEntity> getQueryWrapper(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        // skuId
        Object skuId = params.get("skuId");
        if (skuId != null) {

            if (!StringUtils.isEmpty(skuId)) {

                long skuid = Long.parseLong(skuId.toString());
                wrapper.and(wr -> {

                    wr.eq("sku_id", skuid);
                });

            }

        }

        Object wareId = params.get("wareId");
        if (wareId != null) {

            if (!StringUtils.isEmpty(wareId)) {

                long wareid = Long.parseLong(wareId.toString());
                wrapper.and(wr -> {

                    wr.eq("ware_id", wareid);
                });

            }

        }

        return wrapper;
    }

}