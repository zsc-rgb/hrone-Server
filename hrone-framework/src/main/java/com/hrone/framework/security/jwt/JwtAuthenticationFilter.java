package com.hrone.framework.security.jwt;

import com.alibaba.fastjson2.JSON;
import com.hrone.common.constant.Constants;
import com.hrone.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * JWT 认证过滤器（第6阶段 6.3）
 *
 * 说明：
 * - 从 Authorization: Bearer xxx 读取 Token 并校验
 * - 校验通过：将 userId 放入 request attribute，键为 Constants.LOGIN_USER_KEY
 * - 校验失败：对受保护路径返回 401
 * - 白名单：/auth/**、/error、/druid/**、/test/**、/actuator/**
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${jwt.secret:HROne-Dev-Secret-Key-ChangeMe-For-Production-Use}")
	private String jwtSecret;

	private static final Set<String> WHITELIST_PREFIX = new HashSet<>(Arrays.asList(
		"/auth/", "/error", "/druid/", "/test/", "/actuator/"
	));

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String path = request.getRequestURI();
		if (isWhitelisted(path)) {
			filterChain.doFilter(request, response);
			return;
		}

		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		String prefix = Constants.TOKEN_PREFIX; // "Bearer "
		if (auth != null && auth.startsWith(prefix)) {
			String token = auth.substring(prefix.length());
			if (JwtUtils.validateToken(token, jwtSecret)) {
				// 将 userId 放入请求作用域
				String userId = JwtUtils.getSubject(token, jwtSecret);
				request.setAttribute(Constants.LOGIN_USER_KEY, userId);
				filterChain.doFilter(request, response);
				return;
			}
		}

		// 未携带或无效 Token
		writeUnauthorized(response);
	}

	private boolean isWhitelisted(String path) {
		for (String p : WHITELIST_PREFIX) {
			if (path.startsWith(p)) {
				return true;
			}
		}
		return "/".equals(path);
	}

	private void writeUnauthorized(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		AjaxResult body = AjaxResult.error("未认证，或Token无效").put("code", HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(JSON.toJSONString(body));
	}
}


