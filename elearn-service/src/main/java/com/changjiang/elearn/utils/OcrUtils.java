package com.changjiang.elearn.utils;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class OcrUtils {

    private static final Logger log = LoggerFactory.getLogger(OcrUtils.class);

    @Value("${ocr.data.path}")
    private String ocrDataPath;

    /**
     * 获取 OCR 识别结果。
     *
     * @param filePath 文件路径
     * @param languages 可变参数列表，表示需要识别的语言枚举
     * @return 识别结果字符串，若识别失败则抛出异常
     */
    public String getOcrResult(String filePath, Language... languages) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        File imageFile = new File(filePath);
        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IllegalArgumentException("文件不存在或不是有效的文件");
        }

        if (languages == null || languages.length == 0) {
            throw new IllegalArgumentException("至少需要指定一种语言");
        }

        // 拼接语言代码，用 "+" 连接
        String languageCode = Arrays.stream(languages)
                .map(Language::getCode)
                .collect(Collectors.joining("+"));

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(ocrDataPath);
        tesseract.setLanguage(languageCode);

        try {
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            log.error("OCR 识别失败: {}", e.getMessage(), e);
            throw new RuntimeException("OCR 识别失败", e);
        }
    }

    /**
     * 汇总多次 OCR 识别结果。
     *
     * @param fileLanguageMap 文件路径与语言的映射关系
     * @return 汇总的识别结果
     */
    public Map<String, String> aggregateOcrResults(Map<String, List<Language>> fileLanguageMap) {
        Map<String, String> resultMap = new ConcurrentHashMap<>();

        fileLanguageMap.forEach((filePath, languages) -> {
            try {
                String result = getOcrResult(filePath, languages.toArray(new Language[0]));
                resultMap.put(filePath, result);
            } catch (RuntimeException e) {
                log.error("处理文件 {} 时发生错误: {}", filePath, e.getMessage());
                resultMap.put(filePath, "ERROR: " + e.getMessage());
            }
        });

        return resultMap;
    }

    /**
     * 将多个 OCR 结果合并为单个字符串。
     *
     * @param resultMap 文件路径与识别结果的映射
     * @return 合并后的字符串
     */
    public String mergeOcrResultsToString(Map<String, String> resultMap) {
        return resultMap.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n--- 分割线 ---\n"));
    }
}