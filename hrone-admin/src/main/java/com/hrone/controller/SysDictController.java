package com.hrone.controller;

import com.hrone.common.annotation.OperLog;
import com.hrone.common.core.controller.BaseController;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.enums.BusinessType;
import com.hrone.common.utils.StringUtils;
import com.hrone.system.domain.SysDictData;
import com.hrone.system.domain.SysDictType;
import com.hrone.system.service.ISysDictDataService;
import com.hrone.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理
 */
@RestController
@RequestMapping("/system/dict")
public class SysDictController extends BaseController {

	@Autowired
	private ISysDictTypeService dictTypeService;

	@Autowired
	private ISysDictDataService dictDataService;

	@GetMapping("/type/list")
	public AjaxResult typeList(SysDictType dictType) {
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		return AjaxResult.success(list).put("total", list.size());
	}

	@GetMapping("/type/{dictId}")
	public AjaxResult getType(@PathVariable Long dictId) {
		return AjaxResult.success(dictTypeService.selectDictTypeById(dictId));
	}

	@OperLog(title = "字典管理", businessType = BusinessType.INSERT)
	@PostMapping("/type")
	public AjaxResult addType(@RequestBody SysDictType dictType) {
		return toAjax(dictTypeService.insertDictType(dictType));
	}

	@OperLog(title = "字典管理", businessType = BusinessType.UPDATE)
	@PutMapping("/type")
	public AjaxResult editType(@RequestBody SysDictType dictType) {
		return toAjax(dictTypeService.updateDictType(dictType));
	}

	@OperLog(title = "字典管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/type")
	public AjaxResult removeType(@RequestBody Long[] dictIds) {
		return toAjax(dictTypeService.deleteDictTypeByIds(dictIds));
	}

	@GetMapping("/data/list")
	public AjaxResult dataList(SysDictData dictData) {
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		return AjaxResult.success(list).put("total", list.size());
	}

	@GetMapping("/data/{dictCode}")
	public AjaxResult getData(@PathVariable Long dictCode) {
		return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
	}

	@GetMapping("/data/type/{dictType}")
	public AjaxResult getDataByType(@PathVariable String dictType) {
		if (StringUtils.isEmpty(dictType)) {
			return AjaxResult.error("字典类型不能为空");
		}
		return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
	}

	@OperLog(title = "字典数据", businessType = BusinessType.INSERT)
	@PostMapping("/data")
	public AjaxResult addData(@RequestBody SysDictData dictData) {
		return toAjax(dictDataService.insertDictData(dictData));
	}

	@OperLog(title = "字典数据", businessType = BusinessType.UPDATE)
	@PutMapping("/data")
	public AjaxResult editData(@RequestBody SysDictData dictData) {
		return toAjax(dictDataService.updateDictData(dictData));
	}

	@OperLog(title = "字典数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/data")
	public AjaxResult removeData(@RequestBody Long[] dictCodes) {
		return toAjax(dictDataService.deleteDictDataByIds(dictCodes));
	}
}

