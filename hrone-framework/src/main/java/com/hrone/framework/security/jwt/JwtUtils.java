package com.hrone.framework.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类（兼容 jjwt 0.9.1）
 *
 * 功能：
 * 1) 生成 Token（HS256）
 * 2) 解析 Token，获取 Claims/Subject
 * 3) 校验 Token 是否有效（签名与是否过期）
 *
 * 说明：
 * - 为了开箱即用，这里提供默认的密钥与过期时间，同时允许外部传入覆盖
 * - 后续集成 Spring Security 时，可将密钥与过期时间放入配置文件
 */
public final class JwtUtils {

	/**
	 * 默认签名密钥（开发环境使用，生产请放入配置）
	 */
	private static final String DEFAULT_SECRET = "HROne-Dev-Secret-Key-ChangeMe-For-Production-Use";

	/**
	 * 默认过期时间（单位：毫秒）— 2 小时
	 */
	private static final long DEFAULT_EXPIRE_MS = TimeUnit.HOURS.toMillis(2);

	private JwtUtils() {
	}

	/**
	 * 生成 JWT
	 *
	 * @param subject    标识主体（如用户名/用户ID）
	 * @param claims     自定义负载（可为空）
	 * @param secret     签名密钥（为空使用默认）
	 * @param expireMs   过期时间毫秒（<=0 使用默认）
	 * @return token 字符串
	 */
	public static String generateToken(String subject, Map<String, Object> claims, String secret, long expireMs) {
		long now = System.currentTimeMillis();
		long exp = now + (expireMs > 0 ? expireMs : DEFAULT_EXPIRE_MS);
		byte[] keyBytes = buildKey(secret);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(new Date(now))
			.setExpiration(new Date(exp))
			.signWith(SignatureAlgorithm.HS256, keyBytes)
			.compact();
	}

	/**
	 * 解析 JWT，返回 Claims（内部不捕获异常，调用方按需处理）
	 */
	public static Claims parseClaims(String token, String secret) {
		byte[] keyBytes = buildKey(secret);
		// 0.9.1 使用 Jwts.parser() 而非 parserBuilder()
		return Jwts.parser()
			.setSigningKey(keyBytes)
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * 获取主体（subject）
	 */
	public static String getSubject(String token, String secret) {
		return parseClaims(token, secret).getSubject();
	}

	/**
	 * Token 是否有效（签名正确且未过期）
	 */
	public static boolean validateToken(String token, String secret) {
		try {
			Claims claims = parseClaims(token, secret);
			return claims.getExpiration() != null && claims.getExpiration().after(new Date());
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 构建签名密钥（byte[]）
	 */
	private static byte[] buildKey(String secret) {
		String raw = (secret == null || secret.trim().isEmpty()) ? DEFAULT_SECRET : secret;
		return raw.getBytes(StandardCharsets.UTF_8);
	}
}


