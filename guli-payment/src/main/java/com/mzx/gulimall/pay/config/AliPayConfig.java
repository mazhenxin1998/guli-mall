package com.mzx.gulimall.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-10-19 13:09 周一.
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "ali.pay")
public class AliPayConfig {

    /**
     * 申请支付应用的ID.
     */
    private String appId = "2016102600764432";
    /**
     * 沙箱测试环境的网关.
     */
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do\n";

    /**
     * 商户私钥.
     * 私钥由商家生成,同时生成一个私钥和一个公钥, 公钥需要商家在生成之后配置给支付宝服务端,然后将该公钥在支付宝服务端暴露出来.
     * 商家在发起支付请求, 将会经过该私钥加密, 同时支付宝会由先前保存的公钥进行解密,而之后支付宝服务端向商家反馈信息采取的是支付宝服务端保存的
     * 一个支付宝私钥, 而与该支付宝私钥一起对应的是商家保存的支付宝公钥.商家拿到加密之后的信息之后使用保存在商家的支付宝公钥进行解密.
     */
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCD960upQdtB96j68cbHIIKERwvraEtyTAiLRoKXS+uZBr4BQTGGsHE1BBtiCIYN2vmleV8gPKlirUHF8scWlF6IaxsYkKsQgZRrcdW2XTU+aN+MkAvcQPTlU97gahj+XtKWoVIKI8blj55gkvjPCenNfvANamJhS3ILVmYz1HzO48GYMAry5dwA7ezPH6I8co43wXlITr3Ul43lAWp1ExyT9k+pBHddhIwCRzr8SGj175L4aKRxeFdPLAQHmdEmF+NgFXxszc2leI0Yr2d1lT9WbUfVye0lZRgVCq/2kKZy3povtZehOH3Foy6cVFeg4Rdj0uGY47JQGn8PS3oAEHJAgMBAAECggEALCai9CYWU+38GM97L6DcCM4spe7rK8xRpD1+ptUCbM/lDfFv5W9yQI3KaTrQXVW9bcncm0jw5V2ArzzQoU2+UKQPcbt2Hvd/8be4J2BdbBuRDVl2i+i9pP4hN2rrkXcYPooGaVvYdRxRDon/Jaksgw7WiTnq3K2H4Z76iNtjyYI2pSQVpFynni7wRSDL4yVC5wHpdfibsHsPxp69OyYymxzDKSEokulz9CmkzSLJyk5fhxEi0BgzW+hC/dMaTlPiSqZowiyhSKvPXDKerGDahQ8J5+tRCwRC5/ambu1q9TBXGRZ4u6IaA7+mIAyjCzRy28LkjJPfNVqjwYHYzhBvAQKBgQDsypEVfjtJ5sa1Go2sbyaaIk+nvLj8bp3JqnnfYd2XPGSn/cZuwVepCa00GQ8L9wgsBhxxvoXJSg5NvVvg/kyWNgyggrpVXuH8rP1w9XHbX7nOJR9yXlOwQCoXZx7LDxxq2yeAQb3JEk18jNrSwxp358DOKu/CJxnwqyBNxha98QKBgQCOrD0yyY7y4Gzr8eVJkQkFfgUfcAxQdH9uBnnePmV3PpDpxsIcBZucM0DtcC6ejCP2/jzSGRsEwx013IgBM8yznmKNbQqKxhs/vBmJqbqhejS1ajIvq51J9Tn/GyIk0w7APEl8PmrLNKyNggRlb63pLIiAlf0zY1ZOxOSeYFbJWQKBgQCnt1aJ5a3yyy2FSvh74QJze8f1o9tWmLbUBxZpXTUJkLIutrXc7r67MOpoEzOwItm77NwqyGqURwrYjz3Ln/L5FfFdoxp9q0XfpVuhrYdmfWfqliqu5BbcqKXni5eo9dwH55AT7PyStY8Cn/XCAIT31sOeg5TsbKuX5m1sCNlE0QKBgF/S/TOMPq+P3nhGOprDh8yhjG1GA29SH3hVbyq+u8VwR0JMlXjep1os7nHLA/TG6SzuKwlthUEYjxjq6onsMZN+UHDuiMMWNZ0ECB722Xu1KYujLNvpprXSt8ZIghE7lE+27iA9h9q9oRtV9P/wF11h5LiAhNQr5v72rRpWWFPZAoGBANTufVLTTa4STe2ykfafbcHkyVwBSHubhMSmhP/KWcdgkk7GMFUaX3fg62lhNUvwa+QS6HJYoU70wVn9f9v3Wld6AesrQzFeyIffU2jNJyZr/PPwZrzxEPkm2cBW3xcGNqnM1psgPlruRD2yVy2cQZb36AdIvDK8n+xRbtAl/1Je";

    /**
     * 支付宝支付公钥.
     * 支付宝公钥在配置了商家私钥之后支付宝默认给出的支付宝公钥.
     */
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApu1I1+YBf0a8LqnyeIAe0gIDjkWW6uUyKI5q3uSK6rQ9DOsffWuSMrXYiNBucPJRw6EFLC4rgTGeZBzG2ZaiychAspgbUHLI06YxBq8GEkWcAUaz8Wzk51iXKdXzQC7WirqXvIa4TgPERrzTXf8AwoSfjmIO6x8497ncb+paI17HqxhpPJ369TRFwgsAHKWpOmeI4P5GyW5sHQv6mDwDDeVLrXzppbrqwhqHGUptuGV7MrMWDrsNwqUnevv3s7RGuQM2zLqIGuhQOjUn8eoTbzT1EAi8kveMr6kQ4hkxo1pyvZ8psss067Addtu8Hll7gMXDPgwnhh2AkyntIye/8QIDAQAB";

    /**
     * 支付宝支付成功的异步通知,完整路径是http:// 格式, 后面是不能添加参数的.
     * 并且该异步通知的连接必须是公网可以访问的.
     * 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
     */
    private String notify_url = "https://mall.utools.club/pay/post/pay/sync/result";

    /**
     * 支付宝支付成功之后要展示的页面.
     */
    private String return_url = "https://mall.utools.club/get/orderList";

    /**
     * 签名方式.
     */
    private String sign_type = "RSA2";

    /**
     * 编码解码字符编码.
     */
    private String charset = "utf-8";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getMerchant_private_key() {
        return merchant_private_key;
    }

    public void setMerchant_private_key(String merchant_private_key) {
        this.merchant_private_key = merchant_private_key;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
