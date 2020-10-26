package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.PageUtils;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillSessionService;
import com.mzx.gulimall.coupon.vo.SecKillResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author ZhenXinMa
 * @email 2280480546@qq.com
 * @date 2020-07-10 17:45:21
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SmsSeckillSessionController {
    @Autowired
    private SmsSeckillSessionService smsSeckillSessionService;

    /**
     * 查询.
     * http://localhost:88/api/coupon/seckillsession/list?t=1603716079503&page=1&limit=10&key=
     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//
//        PageUtils page = smsSeckillSessionService.queryPage(params);
//        // 分页查询.
//        // 将日期进行格式化返回.
//        smsSeckillSessionService.listSecKillsPage(params);
//        return R.ok().put("page", page);
//
//    }


    @RequestMapping("/list")
    public SecKillResultVo list(@RequestParam Map<String, Object> params) {

        // 分页查询.
        // 将日期进行格式化返回.
        return smsSeckillSessionService.listSecKillsPage(params);

    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SmsSeckillSessionEntity smsSeckillSession = smsSeckillSessionService.getById(id);

        return R.ok().put("smsSeckillSession", smsSeckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsSeckillSessionEntity smsSeckillSession) {
        smsSeckillSessionService.save(smsSeckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SmsSeckillSessionEntity smsSeckillSession) {
        smsSeckillSessionService.updateById(smsSeckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        smsSeckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
