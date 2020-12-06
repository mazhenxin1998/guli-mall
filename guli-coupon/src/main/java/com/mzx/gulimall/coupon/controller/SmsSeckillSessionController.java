package com.mzx.gulimall.coupon.controller;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.coupon.entity.SmsSeckillSessionEntity;
import com.mzx.gulimall.coupon.service.SmsSeckillSessionService;
import com.mzx.gulimall.coupon.vo.ResultVo;
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
     * 获取最近三天内要上架的秒杀商品.
     *
     * @return
     */
    @GetMapping(value = "/get/lates/three/days/session")
    public ResultVo<SmsSeckillSessionEntity> getLatesSession() {

        return smsSeckillSessionService.getLatesSession();

    }


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
