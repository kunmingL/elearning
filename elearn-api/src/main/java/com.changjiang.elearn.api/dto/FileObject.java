package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class FileObject implements Serializable {
    /**
     *文件名
     */
    private String fileName;

    /**
     *文件路径
     */
    private String filePath;


    /**
     *文件内容
     */
    private byte[] fileContent;

    /**
     *文件文本内容
     */
    private String fileText;

    /**
     *文件base64编码
     */
    private String fileBase64;

    /**
     *上传人Id
     */
    private String userId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        this.fileText = fileText;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
    }
}
