package com.edu.platform.utils;

import com.edu.platform.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类（在线教育平台专用）
 * 特性：1. 注入 JwtProperties 配置，无需手动传秘钥/有效期；2. 增强密钥安全性；3. 完善日志和异常提示
 */
@Slf4j
@Component // 交给 Spring 管理，支持依赖注入
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey; // 预加载密钥，避免重复创建

    /**
     * 构造方法注入 JwtProperties（无需手动传递配置参数，更简洁）
     */
    @Autowired
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // 强制校验密钥长度（避免隐性问题）
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT密钥长度必须≥32个UTF-8字符（256位），当前长度：" + keyBytes.length);
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成 JWT 令牌（简化调用：直接用配置文件的秘钥和有效期）
     * @param claims 需存储的用户信息（如 userId、role）
     * @return 纯 token 字符串（无前缀）
     */
    public String createToken(Map<String, Object> claims) {
        long expMillis = System.currentTimeMillis() + jwtProperties.getTtl();
        Date expiration = new Date(expMillis);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date()) // 补充签发时间（可选，增强令牌完整性）
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(expiration)
                .compact();

        log.info("生成令牌使用的密钥hash：{}，长度：{}",
                secretKey.hashCode(), secretKey.getEncoded().length);
        return token;
    }

    /**
     * 解析 JWT 令牌（简化调用：直接用配置文件的秘钥）
     * @param token 前端传递的纯 token 字符串
     * @return 解析后的用户信息（Claims）
     * @throws RuntimeException 令牌无效/过期时抛出异常（由全局异常处理器捕获）
     */
    public Claims parseToken(String token) {
        try {
            // 强制校验令牌格式（避免空/非法字符）
            if (token == null || !token.contains(".")) {
                throw new RuntimeException("令牌格式错误");
            }
            if (log.isInfoEnabled()) {log.info("生成令牌使用的密钥hash：{}，长度：{}",
                    secretKey.hashCode(), secretKey.getEncoded().length);}
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT令牌已过期：{}", e.getMessage());
            throw new RuntimeException("登录已过期，请重新登录");
        } catch (io.jsonwebtoken.SignatureException e) {
            log.error("JWT令牌签名无效：{}", e.getMessage());
            throw new RuntimeException("令牌无效，请重新登录");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("JWT令牌格式错误：{}", e.getMessage());
            throw new RuntimeException("令牌格式错误，请重新登录");
        } catch (Exception e) {
            log.error("JWT令牌解析失败：{}", e.getMessage());
            throw new RuntimeException("令牌异常，请重新登录");
        }
    }
    /**
     * 保留静态方法（兼容特殊场景：需手动传秘钥/有效期）
     * 建议优先使用上面的注入式方法，此方法仅作为备用
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date expiration = new Date(expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiration)
                .compact();
    }

    /**
     * 保留静态解析方法（备用）
     */
    public static Claims parseJWT(String secretKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}