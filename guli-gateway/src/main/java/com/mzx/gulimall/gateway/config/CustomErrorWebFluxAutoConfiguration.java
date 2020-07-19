package com.mzx.gulimall.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * @author ZhenXinMa
 * @date 2020/7/16 13:36
 */
@Configuration
@EnableConfigurationProperties({ ServerProperties.class, ResourceProperties.class })
public class CustomErrorWebFluxAutoConfiguration {

    private final ServerProperties serverProperties;

    private final ApplicationContext applicationContext;

    private final ResourceProperties resourceProperties;

    private final ObjectProvider<List<ViewResolver>> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public CustomErrorWebFluxAutoConfiguration(ServerProperties serverProperties, ApplicationContext applicationContext,
                                               ResourceProperties resourceProperties,
                                               ObjectProvider<List<ViewResolver>> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorWebExceptionHandler.class, search = SearchStrategy.CURRENT)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes attributes) {

        JsonErrorWebExceptionHandler handler = new JsonErrorWebExceptionHandler(attributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext);
        handler.setViewResolvers(viewResolvers.getIfAvailable());
        handler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        handler.setMessageReaders(this.serverCodecConfigurer.getReaders());

        return handler;
    }


}
