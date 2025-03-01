package com.changjiang.elearn.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.changjiang.python.PythonRestClient;
import com.changjiang.python.model.StanderResponseModle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ElearnPythonRestClient extends PythonRestClient {
    // public <T> T fileObjCallPythonService(String url, Object params, Class<T> responseType) {
    //     try {
    //         // 设置请求头，指定 Content-Type 为 application/json
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.APPLICATION_JSON);
    //
    //         // 将参数对象序列化为 JSON 字符串
    //         String jsonBody = JSON.toJSONString(params);
    //         HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
    //
    //         // 创建 RestTemplate 实例
    //         RestTemplate restTemplate = new RestTemplate();
    //
    //         // 执行 POST 请求
    //         ResponseEntity<String> response = restTemplate.exchange(
    //                 url,
    //                 HttpMethod.POST,
    //                 requestEntity,
    //                 String.class // 接收 JSON 字符串
    //         );
    //         StanderResponseModle standerResponse = (StanderResponseModle)JSON.parseObject((String)response.getBody(), StanderResponseModle.class);
    //         if (!"200".equals(standerResponse.getCode())) {
    //             throw new RuntimeException("Service call failed: " + standerResponse.getMsg());
    //         } else {
    //             standerResponse.getData()
    //         }
    //         // 将响应 JSON 转换为指定类型对象
    //         T result = JSON.parseObject(response.getBody(), responseType);
    //
    //         // 如果返回对象包含 Base64 编码的二进制数据，解码为 byte[]
    //         if (result instanceof FileObject) {
    //             FileObject fileObject = (FileObject) result;
    //             if (fileObject.getFileBase64() != null) {
    //                 // 解码 Base64 字符串为 byte[]
    //                 byte[] fileData = Base64.getDecoder().decode(fileObject.getFileBase64());
    //                 fileObject.setFileContent(fileData);
    //             }
    //         }
    //
    //         return result;
    //     } catch (Exception e) {
    //         // 处理异常，例如记录日志或抛出自定义异常
    //         throw new RuntimeException("调用 Python 服务失败: " + e.getMessage(), e);
    //     }
    // }

    public JSONObject createStudyPlan(String url, Object params) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String jsonBody = JSON.toJSONString(params);
            HttpEntity<String> requestEntity = new HttpEntity(jsonBody, headers);
            // 创建 RestTemplate 实例
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, new Object[0]);
            StanderResponseModle standerResponse = (StanderResponseModle)JSON.parseObject((String)response.getBody(), StanderResponseModle.class);
            if (!"200".equals(standerResponse.getCode())) {
                throw new RuntimeException("Service call failed: " + standerResponse.getMsg());
            } else {
                JSONObject data = standerResponse.getData();
                if (data == null) {
                    return null;
                } else {
                    return data;
                }
            }
        } catch (Exception var10) {
            Exception e = var10;
            log.error("Error calling Python service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to call Python service: " + e.getMessage(), e);
        }
    }

    // /**
    //  * 从字符串中提取 JSON 数据
    //  *
    //  * @param input 包含 JSON 数据的字符串
    //  * @return 提取出的 JSON 字符串
    //  */
    // private static String extractJson(String input) {
    //     // 找到 ```json 和 ``` 之间的内容
    //     int startIndex = input.indexOf("```json") + "```json".length();
    //     int endIndex = input.indexOf("```", startIndex);
    //
    //     if (startIndex < 0 || endIndex < 0) {
    //         throw new IllegalArgumentException("未找到有效的 JSON 数据");
    //     }
    //
    //     // 提取 JSON 数据
    //     String jsonString = input.substring(startIndex, endIndex).trim();
    //
    //     // 去除可能的换行符和空白字符
    //     jsonString = jsonString.replaceAll("\\n", "").replaceAll("\\s+", "");
    //
    //     return jsonString;
    // }
}

