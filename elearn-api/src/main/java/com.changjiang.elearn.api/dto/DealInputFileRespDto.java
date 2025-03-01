package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DealInputFileRespDto implements Serializable {

    private String studyPlanId;

    private Integer totalWords;
}
