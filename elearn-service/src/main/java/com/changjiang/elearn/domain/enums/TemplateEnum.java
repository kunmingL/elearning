package com.changjiang.elearn.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemplateEnum {
    TEMPLATE_1("1", "{\n" +
            "  \"1\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  \"2\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  ...\n" +
            "}"),
    TEMPLATE_2("2", "{\n" +
            "  \"1\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  \"2\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  ...\n" +
            "}"),
    TEMPLATE_3("3", "{\n" +
            "  \"1\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "      \"sentence\": \"英文例句\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  \"2\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "      \"sentence\": \"英文例句\",\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  ...\n" +
            "}"),
    TEMPLATE_4("4", "{\n" +
            "  \"1\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "      \"sentence\": \"英文例句\",\n" +
            "      \"sentenceTranslation\": \"例句翻译\"\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  \"2\": [\n" +
            "    {\n" +
            "      \"word\": \"英文单词\",\n" +
            "      \"pronunciation\": \"音标\",\n" +
            "      \"wordTranslation\": \"中文翻译\",\n" +
            "      \"sentence\": \"英文例句\",\n" +
            "      \"sentenceTranslation\": \"例句翻译\"\n" +
            "    },\n" +
            "    ...\n" +
            "  ],\n" +
            "  ...\n" +
            "}");


    private String code;
    private String templateName;


    public static TemplateEnum getTemplateEnumByCode(String code) {
        for (TemplateEnum templateEnum : TemplateEnum.values()) {
            if (templateEnum.getCode().equals(code)) {
                return templateEnum;
            }
        }
        return null;
    }
}
