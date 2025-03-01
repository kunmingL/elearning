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
     * @param  conversationDto
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/spokenEnglish", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    FileObject spokenEnglish(ConversationInputDto conversationDto);

    /**
     * 根据用户id获取用户所有对话记录简要列表
     * @param userId
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/getUserConversations", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    List<ConversationDTO> getUserConversations(String userId);

    /**
     * 根据对话id获取具体每个会话的对话记录
     * @param conversationId
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/getConversationHistory", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    List<ConversationHistoryDTO> getConversationHistory(String conversationId);

    /**
     * 处理上传文件 支持（*.pdf, *.doc, *.docx, *.ppt, *.pptx, *.xls, *.xlsx,*.png,*.jpg）
     * 这一步对文件进行保存处理，分析出文件中提取的单词总个数并返回给前端
     * @param fileObjects
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/dealInputFile", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    DealInputFileRespDto dealInputFile(List<FileObject> fileObjects);

    /**
     * 根据处理用户上传的学习计划，生成计划
     * 计划包含 每天学习个数、学习天数
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/createUserSchedule", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    CommonRespDataDto createUserSchedule(StudyPlanDTO studyPlanDTO);

    /**
     * 根据用户id查询学习计划
     * @param userid
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/queryStudyPlan", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    List<StudyPlanDTO> queryStudyPlan(String userid);

    /**
     * 根据用户的id、学习计划id、学习的天数、单词索引，查询之前创建的学习计划
     * 逐个按照单词组进行返回
     * @param dailyWordsDto
     * @return
     */
    @ServiceConfig(registryId = "elearn", url = "/elearn/startDailySchedule", channel = {SrvChannel.PC, SrvChannel.MOBILE})
    WordDTO startDailySchedule(DailyStudyDTO dailyWordsDto);



}
