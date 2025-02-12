package com.changjiang.elearn.api.service;

import com.changjiang.bff.annotation.ServiceConfig;
import com.changjiang.bff.constants.SrvChannel;
import com.changjiang.elearn.api.dto.*;

import java.util.List;

/**
 * 英语智能对话
 */
public interface SpokenEnglish {
    /**
     * 英语智能对话
     * @param text
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/spokenEnglish", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    FileObject spokenEnglish(String text);


    /**
     * 处理上传文件 支持（*.pdf, *.doc, *.docx, *.ppt, *.pptx, *.xls, *.xlsx,*.png,*.jpg）
     * 这一步对文件进行保存处理，分析出文件中提取的单词总个数并返回给前端
     * @param fileObjects
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/dealInputFile", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    CommonRespDataDto dealInputFile(List<FileObject> fileObjects);

    /**
     * 根据处理用户上传的学习计划，生成计划
     * 计划包含 每天学习个数、学习天数
     * @return
     */
    CommonRespDataDto createUserSchedule(UserScheduleDto userScheduleDto);


    /**
     * 根据用户的id、学习计划id、学习的天数、单词索引，查询之前创建的学习计划
     * 逐个按照单词组进行返回
     * @param dailyWordsDto
     * @return
     */
    WordDto startDailySchedule(DailyWordsDto dailyWordsDto);



}
