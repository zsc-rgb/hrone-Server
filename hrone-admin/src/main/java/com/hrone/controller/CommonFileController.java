package com.hrone.controller;

import com.hrone.common.core.controller.BaseController;
import com.hrone.common.core.domain.AjaxResult;
import com.hrone.common.exception.ServiceException;
import com.hrone.framework.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用文件上传接口（第8阶段）
 */
@RestController
@RequestMapping("/common/file")
public class CommonFileController extends BaseController {

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public AjaxResult upload(@RequestPart("file") MultipartFile file,
							 @RequestParam(value = "module", required = false) String module) {
		try {
			FileStorageService.FileInfo info = fileStorageService.store(file, module);
			return AjaxResult.success("上传成功")
				.put("fileName", info.getFileName())
				.put("originalName", info.getOriginalName())
				.put("module", info.getModule())
				.put("path", info.getPath())
				.put("size", info.getSize());
		} catch (Exception e) {
			throw new ServiceException("上传失败：" + e.getMessage());
		}
	}
}

