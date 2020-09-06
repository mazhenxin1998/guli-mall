package com.mzx.gulimall.oauth.service.impl;

import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.oauth.constant.RedisConstant;
import com.mzx.gulimall.oauth.fiegn.MemberServiceFeign;
import com.mzx.gulimall.oauth.service.RegService;
import com.mzx.gulimall.oauth.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 22:30
 */
@Slf4j
@Service
public class RegServiceImpl implements RegService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MemberServiceFeign memberServiceFeign;


    @Override
    public R reg(MemberVo member) {

        /*
         * --------------------------------------------------------
         * 如果底层调用第三方服务, 那么在第三方服务中做具体的调用,
         * 所以说这里不需要做具体的细节.
         * --------------------------------------------------------
         * */
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 获取到的是该电话对应的验证码 .
        String s = ops.get(RedisConstant.REDIS_PHONE_KEY_PREFIX + member.getMobile());
        // 先校验下验证码是否正确.
        if (StringUtils.isEmpty(s)) {

            return R.error(20001, "验证码错误,请重新输入!");
        }

        // 验证码校验正确，调用会员服务进行注册.
        try {

            // 另一种思路: 先删除验证码 成功删除之后在进行远程调用注册用户》
            // 远程服务应该返回新增加会员的ID.
            R r = memberServiceFeign.regUser(member);
            if (r.getCode() == 0) {

                // 删除Redis中的验证码.
                stringRedisTemplate.delete(RedisConstant.REDIS_PHONE_KEY_PREFIX + member.getMobile());
            }
            return r;
        } catch (Exception e) {

            log.error("调用第三方服务会员服务进行用户增加的时候出现异常{}", e.getMessage());
            return R.error(20002, "服务内部出现异常(调用远程服务出现异常),请重新尝试！");
        }

    }
}
