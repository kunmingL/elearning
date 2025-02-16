package com.changjiang.elearn.api.dto;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;


import java.io.Serializable;

@Data
public class CommonRespDataDto implements Serializable {
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回码描述
     */
    private String codeMessage;
    /**
     * 返回数据
     */
    private JSONObject data;
}
