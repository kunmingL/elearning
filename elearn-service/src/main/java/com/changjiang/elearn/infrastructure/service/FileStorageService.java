package com.changjiang.elearn.infrastructure.service;


import com.changjiang.base.utils.Base64Utils;
import com.changjiang.elearn.api.dto.FileObject;
import com.changjiang.elearn.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${file.upload.path}")
    private String uploadPath;

    public String saveFile(FileObject fileObject) {
        try {
            // 创建目录
            Path dirPath = Paths.get(uploadPath);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // 生成文件名

            String filePath = uploadPath + fileObject.getFileName();

            // 写入文件
            Base64Utils.decodeBase64ToFile(fileObject.getFileBase64(), filePath);

            return filePath;
        } catch (Exception e) {
            throw new BusinessException("保存文件失败: " + e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";
    }
} 