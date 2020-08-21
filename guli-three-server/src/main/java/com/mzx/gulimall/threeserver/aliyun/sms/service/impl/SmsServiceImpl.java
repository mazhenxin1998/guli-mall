package com.mzx.gulimall.threeserver.aliyun.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mzx.gulimall.common.utils.R;
import com.mzx.gulimall.threeserver.aliyun.constant.RedisConstant;
import com.mzx.gulimall.threeserver.aliyun.constant.SmsConstant;
import com.mzx.gulimall.threeserver.aliyun.sms.service.SmsService;
import com.mzx.gulimall.threeserver.aliyun.util.CodeUtils;
import com.mzx.gulimall.threeserver.aliyun.util.HttpUtils;
import com.mzx.gulimall.threeserver.vo.DxVo;
import com.mzx.gulimall.threeserver.vo.MessageVo;
import com.mzx.gulimall.threeserver.vo.SmsDataVo;
import com.mzx.gulimall.threeserver.vo.SmsParamVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhenXinMa
 * @date 2020/8/21 15:56
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public R sendSmsDx(SmsParamVo param) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String host = SmsConstant.SMS_SEND_HOST;
        String path = SmsConstant.SMS_SEND_PATH;
        String method = SmsConstant.SMS_SEND_METHOD;
        String appcode = SmsConstant.SMS_APP_CODE;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", param.getPhone().toString());
        String code = CodeUtils.getCode();
        querys.put("param", SmsConstant.SMS_CODE_PREFIX + code);
        // 模板ID暂时没有申请.
        querys.put("tpl_id", SmsConstant.SMS_DEFAULT_SIGNATURE_TEMPLATE);
        Map<String, String> bodys = new HashMap<String, String>();

        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String result = EntityUtils.toString(response.getEntity());
            // {"return_code":"00000","order_id":"ALY1598017373079150501"}
            // 将字符串转换为对象。
            DxVo dxVo = JSON.parseObject(result, DxVo.class);
            if ("00000".equals(dxVo.getReturn_code())) {

                // 表示发送成功.将其存入缓存中.
                ops.set(RedisConstant.REDIS_PHONE_KEY_PREFIX + param.getPhone(), code, 5, TimeUnit.MINUTES);
                return R.ok();
            } else {

                return R.error(10002, "发送短信失败!");
            }

        } catch (Exception e) {

            log.error("发送短信的时候出现错误: {}", e.getMessage());
            return R.error(10003, e.getMessage());
        }

    }

    /**
     * 使用以下方法应该是扣除的账户余额. 暂时不使用这种方法.
     *
     * @param param
     * @return
     */
    @Override
    public R send(SmsParamVo param) {

        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        DefaultProfile profile = DefaultProfile.getProfile(SmsConstant.SMS_REGION_ID,
                SmsConstant.SMS_ACCESSKEY_ID, SmsConstant.SMS_ACCESSKEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(SmsConstant.SMS_DO_MAIN);
        // 不知道有什么用.
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", SmsConstant.SMS_REGION_ID);
        request.putQueryParameter("PhoneNumbers", param.getPhone().toString());
        request.putQueryParameter("SignName", SmsConstant.SMS_SIGNATURE);
        request.putQueryParameter("TemplateCode", SmsConstant.SMS_TEMPLATE_CODE);
        // 要发送的消息  JSON格式.
        // 自定义  我们可以定义一个随机的4位数字在加上2个字母
        String code = CodeUtils.getCode();
        MessageVo vo = new MessageVo(code);
        String s = JSON.toJSONString(vo);
        request.putQueryParameter("TemplateParam", s);
        try {

            // 发送请求.
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            SmsDataVo data = JSON.parseObject(response.getData(), SmsDataVo.class);
            if ("OK".equals(data.getCode())) {

                System.out.println("返回判断执行了");
                // 现在应该将这个验证码放入缓存中.
                // 验证码key : GULI:AUTH:CODE:PHONE
                ops.set(RedisConstant.REDIS_PHONE_KEY_PREFIX + param.getPhone(), code, 5, TimeUnit.MINUTES);
                return R.ok();
            } else {

                return R.error(10001, data.getMessage());
            }

        } catch (Exception e) {

            e.printStackTrace();
            return R.error(10002, e.getMessage());
        }

    }


}
