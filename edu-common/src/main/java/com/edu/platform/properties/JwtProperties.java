package com.edu.platform.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性类（在线教育平台专用）
 * 从 application.yml 中读取前缀为 "edu.jwt" 的配置
 */
@Component
@ConfigurationProperties(prefix = "edu.jwt")
@Data
public class JwtProperties {

    /**
     * JWT 签名密钥（必须保密！生产环境建议通过环境变量注入，避免硬编码）
     */
    private String secretKey;

    /**
     * JWT 令牌有效期（单位：毫秒）
     * 示例：86400000 = 24小时（根据业务调整，如7200000=2小时）
     */
    private long ttl;

    /**
     * 前端传递令牌的请求头名称（如：Authorization）
     * 前端需在请求头中携带：Authorization: Bearer {token}
     */
    private String tokenName = "Authorization";

}
