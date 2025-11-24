package com.hrone.controller;

import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.exception.ServiceException;
import com.hrone.common.utils.StringUtils;
import com.hrone.common.utils.ServletUtils;
import com.hrone.common.enums.LoginStatus;
import com.hrone.framework.security.jwt.JwtUtils;
import com.hrone.common.constant.CacheConstants;
import com.hrone.common.constant.Constants;
import com.hrone.common.utils.RedisCache;
import com.hrone.system.domain.SysUser;
import com.hrone.system.service.ISysUserService;
import com.hrone.system.domain.SysLoginLog;
import com.hrone.system.service.ISysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Date;

/**
 * 认证控制器（第6阶段 - 6.2 登录功能）
 *
 * 简化版登录流程：
 * 1. 根据用户名查询用户
 * 2. 校验用户状态与密码（演示环境使用明文对比）
 * 3. 颁发 JWT 并返回
 *
 * 说明：
 * - 为了聚焦学习 JWT，本示例未接入加密与验证码，后续阶段可继续完善
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private ISysUserService userService;

	@Autowired
	private RedisCache redisCache;

	@Autowired
	private ISysLoginLogService loginLogService;

	@Value("${jwt.secret:HROne-Dev-Secret-Key-ChangeMe-For-Production-Use}")
	private String jwtSecret;

	@Value("${jwt.expire-minutes:120}")
	private long jwtExpireMinutes;

	@PostMapping("/login")
	public AjaxResult login(@RequestBody LoginBody body) {
		String username = body != null ? body.getUsername() : null;
		try {
			if (body == null || StringUtils.isEmpty(body.getUsername()) || StringUtils.isEmpty(body.getPassword())) {
				throw new ServiceException("用户名或密码不能为空", 400);
			}
			// 验证码校验（开启）
			if (StringUtils.isEmpty(body.getCaptchaUuid()) || StringUtils.isEmpty(body.getCaptchaCode())) {
				throw new ServiceException("验证码不能为空", 400);
			}
			String redisKey = CacheConstants.CAPTCHA_CODE_KEY + body.getCaptchaUuid();
			String cached = redisCache.get(redisKey);
			if (cached == null) {
				throw new ServiceException("验证码已过期", 400);
			}
			if (!cached.equalsIgnoreCase(body.getCaptchaCode())) {
				throw new ServiceException("验证码错误", 400);
			}
			redisCache.delete(redisKey);

			SysUser user = userService.selectUserByUserName(body.getUsername());
			if (user == null) {
				throw new ServiceException("用户不存在", 404);
			}
			if (!"0".equals(user.getStatus())) {
				throw new ServiceException("用户状态异常，已停用", 403);
			}

			// 密码校验：兼容明文与BCrypt
			if (!passwordMatches(body.getPassword(), user.getPassword())) {
				throw new ServiceException("用户名或密码错误", 401);
			}

			long expireMs = TimeUnit.MINUTES.toMillis(jwtExpireMinutes);
			String token = JwtUtils.generateToken(String.valueOf(user.getUserId()), null, jwtSecret, expireMs);

			Map<String, Object> data = new HashMap<>();
			data.put("token", token);
			data.put("expireMinutes", jwtExpireMinutes);
			data.put("userId", user.getUserId());
			data.put("userName", user.getUserName());
			data.put("nickName", user.getNickName());

			recordLogin(username, LoginStatus.SUCCESS, "登录成功");
			return AjaxResult.success("登录成功").put("data", data);
		} catch (ServiceException e) {
			recordLogin(username, LoginStatus.FAIL, e.getMessage());
			throw e;
		} catch (RuntimeException e) {
			recordLogin(username, LoginStatus.FAIL, e.getMessage());
			throw e;
		}
	}

	/**
	 * 生成简单验证码（文本），返回 uuid 与 code（演示用）
	 */
	@GetMapping("/captcha")
	public AjaxResult captcha() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String code = randomCode(4);
		redisCache.set(CacheConstants.CAPTCHA_CODE_KEY + uuid, code, CacheConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
		Map<String, Object> data = new HashMap<>();
		data.put("uuid", uuid);
		data.put("code", code); // 演示直接返回文本；生产应返回图片/前端不展示真实code
		data.put("expireMinutes", CacheConstants.CAPTCHA_EXPIRATION);
		return AjaxResult.success(data);
	}

	private String randomCode(int len) {
		String digits = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int idx = (int) (Math.random() * digits.length());
			sb.append(digits.charAt(idx));
		}
		return sb.toString();
	}

	private boolean passwordMatches(String raw, String stored) {
		if (stored != null && stored.startsWith("$2a$")) {
			return new BCryptPasswordEncoder().matches(raw, stored);
		}
		return raw != null && raw.equals(stored);
	}

	private void recordLogin(String username, LoginStatus status, String message) {
		SysLoginLog log = new SysLoginLog();
		log.setUserName(StringUtils.isNotEmpty(username) ? username : "anonymous");
		log.setStatus(status.name());
		log.setMsg(message);
		log.setLoginTime(new Date());
		if (ServletUtils.getRequest() != null) {
			log.setIpaddr(ServletUtils.getRequest().getRemoteAddr());
			log.setLoginLocation(ServletUtils.getRequest().getRequestURI());
		}
		loginLogService.insertLoginLog(log);
	}

	/**
	 * 登录请求体
	 */
	public static class LoginBody {
		private String username;
		private String password;
		private String captchaUuid;
		private String captchaCode;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getCaptchaUuid() {
			return captchaUuid;
		}

		public void setCaptchaUuid(String captchaUuid) {
			this.captchaUuid = captchaUuid;
	}

		public String getCaptchaCode() {
			return captchaCode;
		}

		public void setCaptchaCode(String captchaCode) {
			this.captchaCode = captchaCode;
		}
	}
}


