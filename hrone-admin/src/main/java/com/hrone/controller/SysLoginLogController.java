package com.hrone.controller;

import com.hrone.common.annotation.OperLog;
import com.hrone.common.core.controller.BaseController;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.enums.BusinessType;
import com.hrone.system.domain.SysLoginLog;
import com.hrone.system.service.ISysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志接口
 */
@RestController
@RequestMapping("/monitor/login-log")
public class SysLoginLogController extends BaseController {

	@Autowired
	private ISysLoginLogService loginLogService;

	@GetMapping("/list")
	public AjaxResult list(SysLoginLog loginLog) {
		List<SysLoginLog> list = loginLogService.selectLoginLogList(loginLog);
		return AjaxResult.success(list).put("total", list.size());
	}

	@OperLog(title = "登录日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{infoIds}")
	public AjaxResult remove(@PathVariable Long[] infoIds) {
		return toAjax(loginLogService.deleteLoginLogByIds(infoIds));
	}

	@OperLog(title = "登录日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clean")
	public AjaxResult clean() {
		loginLogService.cleanLoginLog();
		return AjaxResult.success("登录日志已清空");
	}
}

