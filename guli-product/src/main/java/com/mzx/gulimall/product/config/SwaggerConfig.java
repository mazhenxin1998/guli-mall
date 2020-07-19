package com.mzx.gulimall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ZhenXinMa
 * @date 2020/7/15 21:30
 */
@Configuration
@EnableSwagger2
@Component
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(this.apiInfo())
                .select()
                // apis配置的扫描当前服务下的那个包下的接口.
                .apis(RequestHandlerSelectors.basePackage("com.mzx.gulimall.product.controller"))
                // any表示给该接口下的所有方法都生成文档
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("小苹果校园超市后台服务API文档")
                .description("小苹果校园超市后台服务API文档")
                .version("1.0")
                .build();
    }

}
