package com.changjiang.elearn.utils;

import com.alibaba.fastjson2.JSON;
import com.changjiang.elearn.api.dto.FileObject;
import com.changjiang.python.PythonRestClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class ElearnPythonRestClient extends PythonRestClient {
    public <T> T fileObjCallPythonService(String url, Object params, Class<T> responseType) {
        try {
            // 设置请求头，指定 Content-Type 为 application/json
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 将参数对象序列化为 JSON 字符串
            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();

            // 执行 POST 请求
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class // 接收 JSON 字符串
            );

            // 将响应 JSON 转换为指定类型对象
            T result = JSON.parseObject(response.getBody(), responseType);

            // 如果返回对象包含 Base64 编码的二进制数据，解码为 byte[]
            if (result instanceof FileObject) {
                FileObject fileObject = (FileObject) result;
                if (fileObject.getFileBase64() != null) {
                    // 解码 Base64 字符串为 byte[]
                    byte[] fileData = Base64.getDecoder().decode(fileObject.getFileBase64());
                    fileObject.setFileContent(fileData);
                }
            }

            return result;
        } catch (Exception e) {
            // 处理异常，例如记录日志或抛出自定义异常
            throw new RuntimeException("调用 Python 服务失败: " + e.getMessage(), e);
        }
    }


}
