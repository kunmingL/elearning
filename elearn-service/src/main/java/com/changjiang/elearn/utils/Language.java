package com.changjiang.elearn.utils;
/**
 * 定义 OCR 支持的语言枚举。
 */
public enum Language {
    ENG("eng"), // 英文
    CHI_SIM("chi_sim"), // 简体中文
    CHI_TRA("chi_tra"), // 繁体中文
    SPA("spa"), // 西班牙语
    FRA("fra"); // 法语

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
