package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonRespDataDto implements Serializable {
    private String code;
    private String message;
}
