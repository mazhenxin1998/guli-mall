package com.mzx.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.Query;
import com.mzx.gulimall.coupon.dao.SmsSeckillSessionDao;
import com.mzx.gulimall.coupon.dao.SmsSeckillSkuRelationDao;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import com.mzx.gulimall.coupon.entity.SmsSeckillSkuRelationEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillSessionService;
import com.mzx.gulimall.coupon.vo.ResultVo;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;
import com.mzx.gulimall.coupon.vo.SecKillSessionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 秒杀会话Service.
 */
@Service("smsSeckillSessionService")
public class SmsSeckillSessionServiceImpl extends ServiceImpl<SmsSeckillSessionDao, SmsSeckillSessionEntity> implements SmsSeckillSessionService {

    @Autowired
    private SmsSeckillSessionDao smsSeckillSessionDao;

    @Autowired
    private SmsSeckillSkuRelationDao smsSeckillSkuRelationDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SmsSeckillSessionEntity> page = this.page(
                new Query<SmsSeckillSessionEntity>().getPage(params),
                new QueryWrapper<SmsSeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public SecKillResultVo listSecKillsPage(Map<String, Object> params) {

        // http://localhost:88/api/coupon/seckillsession/list?t=1603716079503&page=1&limit=10&key=
        // 分页查询.
        Object pageString = params.get("page");
        Integer page = 0;
        Integer size = 10;

        Object limit = params.get("limit");
        if (StringUtils.isEmpty(limit.toString())) {

            size = 10;

        }

        if (StringUtils.isEmpty(pageString.toString())) {

            page = 0;

        } else {

            page = Integer.valueOf(pageString.toString()) - 1;
            page = page * size;

        }

        List<SmsSeckillSessionEntity> entities = smsSeckillSessionDao.listSeckillSessions(page, size);
        List<SecKillSessionVo> collect = entities.stream().map(item -> {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime = format.format(item.getStartTime());
            String endTime = format.format(item.getEndTime());
            String createTime = format.format(item.getCreateTime());
            SecKillSessionVo sessionVo = new SecKillSessionVo();
            sessionVo.setCreateTime(createTime);
            sessionVo.setEndTime(endTime);
            sessionVo.setStartTime(startTime);
            sessionVo.setStatus(item.getStatus());
            sessionVo.setName(item.getName());
            sessionVo.setId(item.getId());
            return sessionVo;

        }).collect(Collectors.toList());
        SecKillResultVo resultVo = new SecKillResultVo();
        SecKillResultVo.Page<SecKillSessionVo> p = new SecKillResultVo.Page();
        p.setList(collect);
        resultVo.setPage(p);
        resultVo.setCode(0);
        return resultVo;

    }

    @Override
    public ResultVo<SmsSeckillSessionEntity> getLatesSession() {

        // SessionEntity表示的是每日秒杀场次的实体.
        List<SmsSeckillSessionEntity> sessions = smsSeckillSessionDao.getLatesSession(this.getStarterTime(),
                this.getEndTime());
        List<SmsSeckillSessionEntity> collect = sessions.stream().map(item -> {

            // 为每个会话设置与其相互关联的商品信息.
            List<SmsSeckillSkuRelationEntity> relations = smsSeckillSkuRelationDao.getById(item.getId());
            item.setRelations(relations);
            return item;

        }).collect(Collectors.toList());

        ResultVo<SmsSeckillSessionEntity> result = new ResultVo<>();
        result.setList(collect);
        result.setCode(0);
        return result;

    }


    private String getStarterTime() {

        LocalDate time = LocalDate.now();
        LocalDateTime min = LocalDateTime.MIN;
        LocalDateTime start = LocalDateTime.of(time, LocalTime.from(min));
        return start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }

    private String getEndTime() {

        LocalDate time = LocalDate.now();
        LocalDateTime max = LocalDateTime.MAX;
        LocalDateTime end = LocalDateTime.of(time, LocalTime.from(max));
        return end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    }


}