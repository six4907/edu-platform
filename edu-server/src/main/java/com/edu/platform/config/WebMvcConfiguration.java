package com.edu.platform.config;

import com.edu.platform.interceptor.JwtTokenInterceptor;
import com.edu.platform.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * Web MVC配置类（在线教育平台专用）
 * 作用：注册拦截器、配置接口文档、静态资源映射、扩展消息转换器
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor; // 注入通用JWT拦截器（已修正类名）

    /**
     * 注册自定义拦截器（适配教育平台接口路径）
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenInterceptor)
                // 拦截所有/api/**路径（用户相关、课程相关等接口）
                .addPathPatterns("/api/**")
                // 放行登录接口（无需登录即可访问）
                .excludePathPatterns("/api/user/login")
                // 可根据业务添加其他放行路径（如注册接口、验证码接口等）
                .excludePathPatterns("/api/user/register");
    }

    /**
     * 通过Knife4j/Swagger生成接口文档（适配教育平台）
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("在线教育平台接口文档")
                .version("1.0")
                .description("包含用户管理、课程管理、学习记录等核心接口")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                // 扫描Controller包路径（按你的实际包路径调整，确保能扫描到所有Controller）
                .apis(RequestHandlerSelectors.basePackage("com.edu.platform.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射（支持Swagger/Knife4j文档访问）
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("配置静态资源映射...");
        // 放行Swagger/Knife4j相关静态资源
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 若有其他静态资源（如前端页面、图片等），可在此添加映射
         registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 扩展Spring MVC消息转换器（使用自定义JacksonObjectMapper）
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器...");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置自定义JacksonObjectMapper（统一日期格式、兼容未知属性）
        converter.setObjectMapper(new JacksonObjectMapper());
        // 添加到转换器列表最前面，优先使用自定义转换器
        converters.add(0, converter);
    }
}