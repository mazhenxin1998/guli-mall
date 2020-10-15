package com.mzx.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.mq.StockDetailTo;
import com.mzx.gulimall.common.mq.StockLockTo;
import com.mzx.gulimall.common.order.OrderTo;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.ware.dao.WareOrderTaskDao;
import com.mzx.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.mzx.gulimall.ware.dao.WareSkuDao;
import com.mzx.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.mzx.gulimall.ware.entity.WareOrderTaskEntity;
import com.mzx.gulimall.ware.entity.WareSkuEntity;
import com.mzx.gulimall.ware.feign.OrderServiceFeign;
import com.mzx.gulimall.ware.mq.StockRabbitTemplate;
import com.mzx.gulimall.ware.service.WareOrderTaskService;
import com.mzx.gulimall.ware.service.WareSkuService;
import com.mzx.gulimall.ware.vo.LockStockResult;
import com.mzx.gulimall.ware.vo.SkuWareStockTo;
import com.mzx.gulimall.ware.vo.WareSkuLockVo;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private WareOrderTaskService wareOrderTaskService;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private StockRabbitTemplate stockRabbitTemplate;

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

    @Transactional(rollbackFor = Exception.class)
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
        WareOrderTaskDetailDao taskDetailDao = sqlSession.getMapper(WareOrderTaskDetailDao.class);
        ArrayList<LockStockResult> results = new ArrayList<>();
        try {

            wareSkuLockVo.getLocks().stream().forEach(item -> {

                LockStockResult lockStockResult = new LockStockResult();
                // 远程锁定的时候还需要确定锁定那个仓库的.
                mapper.lockStock(item);
                // 顺便在发送给MQ进行库存自动解锁的队列.
                lockStockResult.setLocked(true);
                lockStockResult.setNum(item.getNum());
                lockStockResult.setSkuId(item.getSkuId());
                results.add(lockStockResult);
                // 如果该方法出现异常整体回滚.
                this.saveOrderTaskDetail(taskDetailDao, item, taskEntity.getId(), wareSkuLockVo.getOrderSn());

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

        } finally {

            // 不管修改成功还是失败,都应该将创建出来的对象进行关闭.
            // 其实这里应该是不用对其进行null值设置的,因为方法执行完毕,对象将不会存在GcRoots上.JVM会自动进行GC.
            sqlSession = null;

        }

        return results;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void listReleaseStocks(List<WareOrderTaskDetailEntity> detailEntities) {

        // TODO: 2020/10/15 如果对库存锁定进行释放和对库存工作详情单状态修改出现了异常,放心直接抛出即可.
        // 从该SqlSession中获取的dao是批量操作，手动进行commit.
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        WareSkuDao mapper = session.getMapper(WareSkuDao.class);
        WareOrderTaskDetailDao taskDetailDao = session.getMapper(WareOrderTaskDetailDao.class);
        // item.lockStatus==1 为true表示当前当前库存工作详情单没有解锁过.
        // 那么我现在就可以对其进行解锁.
        // 解锁之后需要改变库存工作单的锁定状态.
        // 出现异常将会进行回滚.而在StockReleaseStockListenerImpl中对异常进行捕获,如果在外面捕获到异常,那么MQ就回滚.
        detailEntities.stream().filter(item -> item.getLockStatus() == 1).forEach(item -> {

            System.out.println("对库存" + item.getSkuId() + "解锁成功");
            // 对SKU的库存进行解锁.
            mapper.releaseLockStocks(item.getSkuId(), item.getSkuNum(), item.getWareId());
            // 对库存工作单详情的锁定状态修改为已解锁.
            // 这里修改的时候必须以库存工作详情单的ID进行修改.
            taskDetailDao.updateLockStatus(item.getId(), 2L);

        });

        // 将当前事务进行提交.
        session.commit();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void listReleaseStocks(OrderTo order) {

        // 有任何异常向外抛出即可 上层方法捕捉到异常将会对当前的消息打向日志和拒绝签收.
        WareOrderTaskEntity entity = wareOrderTaskService.getOrderTaskByOrderSn(order.getOrderSn());
        if (entity != null) {

            List<WareOrderTaskDetailEntity> detailEntities = wareOrderTaskDetailDao
                    .getOrderTaskDetailsByStockId(entity.getId());
            this.listReleaseStocks(detailEntities);

        } else {

            throw new RuntimeException("订单超时库存自动解锁 查询到WareOrderTaskEntity为null.");

        }

    }

    private void saveOrderTaskDetail(WareOrderTaskDetailDao wareOrderTaskDetailDao, WareSkuLockVo.Item item,
                                     Long taskId, String orderSn) {

        // 每次锁定一次, 应该保存库存锁定单详情.
        // 要进行保存的库存工作单详情.
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setSkuId(item.getSkuId());
        taskDetailEntity.setSkuNum(item.getNum());
        taskDetailEntity.setTaskId(taskId);
        // 库存工作单详情里面可以不用保存库存. 因为本来也就没有做.
        taskDetailEntity.setWareId(item.getWareId());
        wareOrderTaskDetailDao.insert(taskDetailEntity);
        // 最后每次增加了库存工作单详情,应该做库存自动解锁的保底方法.
        this.autoUnlocking(item, taskId, orderSn);

    }

    /**
     * 库存自动解锁.
     * <p>
     * 该方法解决的是当订单服务调用库存服务成功之后，但是订单服务之后的代码出现了异常导致订单回滚, 而这里向MQ发送消息则保证就算订单服务出现异常，那么
     * 当前系统在一定时间之后仍然可以保证数据的最终一致性.
     */
    private void autoUnlocking(WareSkuLockVo.Item item, Long taskId, String orderSn) {

        StockLockTo lockTo = new StockLockTo();
        // 保存该库存工作单详情的订单号.
        lockTo.setOrderSn(orderSn);
        // 保存的是库存工作单的ID.
        lockTo.setId(taskId);
        StockDetailTo stockDetailTo = new StockDetailTo();
        // 初始化的时候应该设置为已解锁.
        stockDetailTo.setLockStatus(1);
        stockDetailTo.setSkuId(item.getSkuId());
        stockDetailTo.setSkuName(item.getTitle());
        stockDetailTo.setSkuNum(item.getNum());
        stockDetailTo.setTaskId(taskId);
        stockDetailTo.setWareId(item.getWareId());
        lockTo.setDetail(stockDetailTo);
        // 为了实现库存服务自动解锁,现在需要将lockTo方法发送到MQ的延时队列中.
        // 只要用户来下单, 那么都会向MQ发送消息记录下本次下单所关联的SKU库存信息.
        // 并且在解锁哪里,会对订单或者库存详情单的状态来进行解锁.
        this.stockRabbitTemplate.lockStock(lockTo);

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