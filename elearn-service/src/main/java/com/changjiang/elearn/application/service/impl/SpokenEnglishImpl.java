package com.changjiang.elearn.application.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.changjiang.elearn.api.dto.*;
import com.changjiang.elearn.api.service.SpokenEnglish;

import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.domain.model.StudyPlan;
import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.domain.repository.StudyPlanRepository;
import com.changjiang.elearn.domain.repository.WordRepository;


import com.changjiang.elearn.infrastructure.exception.BusinessException;
import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.python.PythonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.UUID;

@GrpcService(registry = "elearn")
@Slf4j
public class SpokenEnglishImpl implements SpokenEnglish {

    @Autowired
    private PythonRestClient pythonRestClient;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private StudyPlanRepository studyPlanRepository;
    
    @Autowired
    private WordRepository wordRepository;
    
    @Value("${python.api.speech}")
    private String speechApiUrl;
    
    @Value("${python.api.word-extract}")
    private String wordExtractApiUrl;
    
    @Value("${python.api.study-plan}")
    private String studyPlanApiUrl;
    
    @Value("${document.max-size}")
    private long maxDocumentSize;

    @Override
    public FileObject spokenEnglish(String text) {
        log.info("英语口语练习开始：入参:{}", text);
        JSONObject jsonObject = JSONObject.parseObject(text);
        
        try {
            byte[] audioBytes = pythonRestClient.callPythonServiceForBytes(speechApiUrl, jsonObject);
            
            FileObject fileObject = new FileObject();
            fileObject.setFileName(UUID.randomUUID().toString() + ".mp3");
            fileObject.setFileContent(audioBytes);
            
            log.info("英语口语练习结束：出参:{}", fileObject.getFileName());
            return fileObject;
        } catch (Exception e) {
            log.error("调用语音服务失败", e);
            throw new BusinessException("生成语音失败");
        }
    }

    @Override
    public CommonRespDataDto dealInputFile(List<FileObject> fileObjects) {
        CommonRespDataDto response = new CommonRespDataDto();
        
        try {
            for(FileObject file : fileObjects) {
                // 检查文件大小
                if(file.getFileContent().length > maxDocumentSize) {
                    throw new BusinessException("文件过大,最大支持" + maxDocumentSize/1024/1024 + "MB");
                }
                
                // 保存文件
                String filePath = saveFile(file);
                
                // 创建文档记录
                Document document = new Document();
                document.setId(UUID.randomUUID().toString());
                document.setFileName(file.getFileName());
                document.setFilePath(filePath);
                document.setFileSize((long)file.getFileContent().length);
                document.setStatus(DocumentStatus.PENDING);
                
                documentRepository.save(document);
                
                // 异步处理文档提取单词
                asyncProcessDocument(document);
            }
            
            response.setCode("0");
            response.setMessage("文件上传成功");
        } catch (Exception e) {
            log.error("处理上传文件失败", e);
            response.setCode("1");
            response.setMessage(e.getMessage());
        }
        
        return response;
    }

    @Override
    public CommonRespDataDto createUserSchedule(UserScheduleDto userScheduleDto) {
        CommonRespDataDto response = new CommonRespDataDto();
        
        try {
            // 调用Python服务生成学习计划
            JSONObject result = pythonRestClient.callPythonService(
                studyPlanApiUrl,
                JSONObject.from(userScheduleDto),
                JSONObject.class
            );
            
            // 保存学习计划
            StudyPlan plan = new StudyPlan();
            plan.setId(UUID.randomUUID().toString());
            plan.setUserId(userScheduleDto.getUserId());
            plan.setDailyWords(userScheduleDto.getDailyWords());
            plan.setTotalDays(calculateTotalDays(userScheduleDto));
            plan.setStatus(StudyPlanStatus.IN_PROGRESS);
            
            studyPlanRepository.save(plan);
            
            response.setCode("0");
            response.setMessage("学习计划创建成功");
        } catch (Exception e) {
            log.error("创建学习计划失败", e);
            response.setCode("1"); 
            response.setMessage(e.getMessage());
        }
        
        return response;
    }

    @Override
    public WordDto startDailySchedule(DailyWordsDto dailyWordsDto) {
        try {
            // 获取学习计划
            StudyPlan plan = studyPlanRepository.findById(dailyWordsDto.getScheduleId());
            if(plan == null) {
                throw new BusinessException("学习计划不存在");
            }
            
            // 获取当天要学习的单词
            List<Word> words = wordRepository.findDailyWords(
                plan.getId(),
                plan.getCurrentDay(),
                plan.getDailyWords()
            );
            
            // 生成单词音频
            WordDto wordDto = new WordDto();
            for(Word word : words) {
                // 调用Python服务生成发音
                byte[] audioBytes = pythonRestClient.callPythonServiceForBytes(
                    speechApiUrl,
                    JSONObject.from(word)
                );
                
                // 设置音频文件
                FileObject audio = new FileObject();
                audio.setFileName(word.getId() + ".mp3");
                audio.setFileContent(audioBytes);
                
                wordDto.setWord(audio);
            }
            
            return wordDto;
        } catch (Exception e) {
            log.error("获取每日单词失败", e);
            throw new BusinessException("获取学习内容失败");
        }
    }
    
    private String saveFile(FileObject file) {
        // 实现文件保存逻辑
        return null;
    }
    
    private void asyncProcessDocument(Document document) {
        // 实现异步文档处理逻辑
    }
    
    private int calculateTotalDays(UserScheduleDto dto) {
        return (int)Math.ceil(dto.getCountWords() / (double)dto.getDailyWords());
    }
} 