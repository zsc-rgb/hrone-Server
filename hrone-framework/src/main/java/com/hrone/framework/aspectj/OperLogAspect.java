package com.hrone.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.hrone.common.annotation.OperLog;
import com.hrone.common.constant.Constants;
import com.hrone.common.utils.ServletUtils;
import com.hrone.system.domain.SysOperLog;
import com.hrone.system.service.ISysOperLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 操作日志记录切面
 */
@Aspect
@Component
public class OperLogAspect {

	@Autowired
	private ISysOperLogService operLogService;

	@AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
	public void doAfterReturning(JoinPoint joinPoint, OperLog controllerLog, Object jsonResult) {
		handleLog(joinPoint, controllerLog, null, jsonResult);
	}

	@AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, OperLog controllerLog, Exception e) {
		handleLog(joinPoint, controllerLog, e, null);
	}

	protected void handleLog(final JoinPoint joinPoint, OperLog controllerLog, final Exception e, Object jsonResult) {
		HttpServletRequest request = ServletUtils.getRequest();
		if (request == null) {
			return;
		}

		SysOperLog operLog = new SysOperLog();
		operLog.setOperTime(new Date());
		operLog.setTitle(controllerLog.title());
		operLog.setOperatorType(controllerLog.operatorType().ordinal());
		operLog.setBusinessType(controllerLog.businessType().ordinal());
		operLog.setOperIp(request.getRemoteAddr());
		operLog.setOperUrl(request.getRequestURI());
		operLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName());
		operLog.setRequestMethod(request.getMethod());

		Object userId = request.getAttribute(Constants.LOGIN_USER_KEY);
		if (userId != null) {
			operLog.setOperName(String.valueOf(userId));
		}

		operLog.setOperParam(JSON.toJSONString(joinPoint.getArgs()));
		if (jsonResult != null) {
			operLog.setJsonResult(JSON.toJSONString(jsonResult));
			operLog.setStatus(0);
		}
		if (e != null) {
			operLog.setStatus(1);
			operLog.setErrorMsg(e.getMessage());
		}

		operLogService.insertOperLog(operLog);
	}
}

