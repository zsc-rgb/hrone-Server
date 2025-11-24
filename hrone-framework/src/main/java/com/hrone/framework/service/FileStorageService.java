package com.hrone.framework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.nio.file.StandardCopyOption;

/**
 * 简单文件存储服务（第8阶段）
 */
@Service
public class FileStorageService {

	@Value("${hrone.file.upload-dir:uploads}")
	private String uploadDir;

	public FileInfo store(MultipartFile file, String module) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("文件不能为空");
		}

		String originalFilename = file.getOriginalFilename();
		String ext = "";
		if (originalFilename != null && originalFilename.contains(".")) {
			ext = originalFilename.substring(originalFilename.lastIndexOf("."));
		}
		String safeModule = StringUtils.hasText(module) ? module : "common";
		String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		Path root = Paths.get(uploadDir, safeModule, datePath);
		Files.createDirectories(root);

		String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;
		Path target = root.resolve(newFileName);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
		}

		FileInfo info = new FileInfo();
		info.setFileName(newFileName);
		info.setOriginalName(originalFilename);
		info.setModule(safeModule);
		info.setPath(target.toString());
		info.setSize(file.getSize());
		return info;
	}

	public static class FileInfo {
		private String fileName;
		private String originalName;
		private String module;
		private String path;
		private long size;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getOriginalName() {
			return originalName;
		}

		public void setOriginalName(String originalName) {
			this.originalName = originalName;
		}

		public String getModule() {
			return module;
		}

		public void setModule(String module) {
			this.module = module;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}
	}
}

