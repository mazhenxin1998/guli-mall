package com.mzx.gulimall.threeserver.aliyun.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.mzx.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 阿里云OSS获取存储上传文件的签名.
 *
 * @author ZhenXinMa
 * @date 2020/7/14 22:01
 */
@RestController
@RequestMapping(value = "thirdparty/oss")
public class OssController {

    @Value(value = "${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value(value = "${spring.cloud.alicloud.secret-key}")
    private String accessKey;

    @Value(value = "${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value(value = "${spring.cloud.alicloud.bucket}")
    private String bucket;

    @Resource
    private OSS ossClient;


    @RequestMapping(value = "/policy")
    public R policy() {

        // host的格式为 bucketname.endpoint
        String host = "https://" + bucket + "." + endpoint;
        // 生成一个以当前时间为目录
        // 用户上传文件时指定的前缀。
        String dir = LocalDate.now().toString() + "/";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        Map<String, String> respMap = null;
        try {

            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));

        } catch (Exception e) {

            System.out.println(e.getMessage());
        } finally {

            ossClient.shutdown();
        }

        return R.ok().put("data",respMap);
    }

}
