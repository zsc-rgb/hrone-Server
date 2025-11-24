package com.hrone.controller;

import com.hrone.common.annotation.OperLog;
import com.hrone.common.core.controller.BaseController;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.enums.BusinessType;
import com.hrone.system.domain.SysOperLog;
import com.hrone.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志接口
 */
@RestController
@RequestMapping("/monitor/oper-log")
public class SysOperLogController extends BaseController {

	@Autowired
	private ISysOperLogService operLogService;

	@GetMapping("/list")
	public AjaxResult list(SysOperLog operLog) {
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		return AjaxResult.success(list).put("total", list.size());
	}

	@OperLog(title = "操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{operIds}")
	public AjaxResult remove(@PathVariable Long[] operIds) {
		return toAjax(operLogService.deleteOperLogByIds(operIds));
	}

	@OperLog(title = "操作日志", businessType = BusinessType.CLEAN)
	@DeleteMapping("/clean")
	public AjaxResult clean() {
		operLogService.cleanOperLog();
		return AjaxResult.success("操作日志已清空");
	}
}

