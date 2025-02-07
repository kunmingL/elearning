package com.changjiang.elearn.api.service;

import com.changjiang.bff.annotation.ServiceConfig;
import com.changjiang.bff.constants.SrvChannel;

/**
 * 英语智能对话
 */
public interface SpokenEnglish {
    /**
     * 英语智能对话
     * @param text
     * @return
     */
    @ServiceConfig(registryId = "demo.api.test",
            url = "/api/demo/test",
            channel = {SrvChannel.PC, SrvChannel.MOBILE})
    String spokenEnglish(String text);
}
