package com.edu.platform.interceptor;

import com.edu.platform.constant.JwtClaimsConstant;
import com.edu.platform.constant.MessageConstant;
import com.edu.platform.context.BaseContext;
import com.edu.platform.properties.JwtProperties;
import com.edu.platform.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JWT令牌校验拦截器（通用版，适配学生/教师/管理员多角色）
 * 作用：1. 校验请求头中的JWT令牌有效性；2. 解析用户ID和角色存入上下文；3. 未登录/令牌无效返回401
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor { 

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 非Controller方法（如静态资源）直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 2. 从请求头获取令牌（使用JwtProperties配置的tokenName，如Authorization）
        String token = request.getHeader(jwtProperties.getTokenName());
        log.info("JWT令牌校验：token={}", token);

        // 3. 令牌为空，返回401（未登录）
        if (token == null || token.trim().isEmpty()) {
            log.warn("请求头中未携带令牌，请求路径：{}", request.getRequestURI());
            sendUnauthorizedResponse(response, MessageConstant.USER_NOT_LOGIN);
            return false;
        }

        if(token!=null&&token.startsWith("Bearer ")){
            token = token.substring("Bearer ".length()).trim();
        }

        // 4. 校验并解析令牌
        try {
            // 适配优化后的JwtUtil（无需手动传secretKey）
            Claims claims = jwtUtil.parseToken(token);

            // 5. 解析用户ID和角色，存入BaseContext（供业务层获取）
            Number userIdNum = (Number)claims.get(JwtClaimsConstant.USER_ID);
            Long userId = userIdNum.longValue();
            Integer userRole = (Integer) claims.get(JwtClaimsConstant.ROLE);
            BaseContext.setUserId(userId);
            BaseContext.setUserRole(userRole);

            log.info("令牌校验通过，当前用户ID：{}，角色：{}", userId, userRole);
            return true; // 校验通过，放行
        } catch (RuntimeException ex) {
            // 捕获JwtUtil中抛出的异常（令牌过期、签名无效等）
            log.error("令牌校验失败：{}", ex.getMessage());
            sendUnauthorizedResponse(response, ex.getMessage());
            return false;
        }
    }

    /**
     * 拦截器postHandle后清理上下文，避免线程池复用导致内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.clear(); // 关键：请求结束后清空ThreadLocal
        log.info("请求结束，清理ThreadLocal上下文");
    }

    /**
     * 发送401未授权响应（返回统一Result格式，便于前端统一处理）
     */
    private void sendUnauthorizedResponse(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401状态码
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        // 返回统一的Result错误格式，前端可按code=401或msg处理跳转登录
        writer.write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
        writer.flush();
        writer.close();
    }
}