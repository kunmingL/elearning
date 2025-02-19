package com.changjiang.elearn.application.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.changjiang.elearn.api.dto.*;
import com.changjiang.elearn.api.service.SpokenEnglish;
import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import com.changjiang.elearn.domain.model.Conversation;
import com.changjiang.elearn.domain.model.ConversationHistory;
import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.domain.model.StudyPlan;
import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.domain.repository.ConversationRepository;
import com.changjiang.elearn.domain.repository.ConversationHistoryRepository;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.domain.repository.StudyPlanRepository;
import com.changjiang.elearn.domain.repository.WordRepository;
import com.changjiang.elearn.infrastructure.exception.BusinessException;
import com.changjiang.elearn.infrastructure.service.AsyncDocumentService;
import com.changjiang.elearn.infrastructure.service.FileStorageService;
import com.changjiang.elearn.utils.ElearnPythonRestClient;
import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.python.PythonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 英语学习服务实现类
 * 提供英语口语练习、文件处理、学习计划生成和每日学习等功能
 */
@GrpcService(registry = "elearn")
@Slf4j
public class SpokenEnglishImpl implements SpokenEnglish {

    // 支持的文件格式
    private static final List<String> SUPPORTED_FORMATS = Arrays.asList(
        "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "png", "jpg"
    );

    @Autowired
    private ElearnPythonRestClient pythonRestClient;
    
    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private StudyPlanRepository studyPlanRepository;
    
    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AsyncDocumentService asyncDocumentService;
    
    @Value("${python.api.speech}")
    private String speechApiUrl;
    
    @Value("${python.api.word-extract}")
    private String wordExtractApiUrl;
    
    @Value("${python.api.study-plan}")
    private String studyPlanApiUrl;
    
    @Value("${document.max-size}")
    private long maxDocumentSize;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationHistoryRepository conversationHistoryRepository;

    /**
     * 英语口语练习
     * 将文本转换为语音
     *
     * @param conversationDto 待转换的文本
     * @return 语音文件对象
     */
    @Override
    public FileObject spokenEnglish(ConversationDto conversationDto) {
        log.info("英语口语练习开始：入参:{}", conversationDto);
        
        if (ObjectUtils.isEmpty(conversationDto) || StringUtils.isEmpty(conversationDto.getCurrentText())) {
            throw new BusinessException("文本内容不能为空");
        }

        try {
            // 构建带上下文的请求
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("conversation_id", conversationDto.getConversationId());
            jsonObject.put("prompt_template", conversationDto.getCurrentText());
            
            // 添加历史对话
            if (conversationDto.getHistory() != null && !conversationDto.getHistory().isEmpty()) {
                jsonObject.put("history", conversationDto.getHistory());
            }

            // 调用Python服务
            FileObject fileObject = pythonRestClient.fileObjCallPythonService(
                speechApiUrl, 
                jsonObject,
                FileObject.class
            );
            
            // 保存对话历史
            saveConversationHistory(conversationDto);
            
            fileObject.setFileName(UUID.randomUUID().toString() + ".mp3");
            
            // 保存音频文件
            try (FileOutputStream fos = new FileOutputStream("output.mp3")) {
                fos.write(fileObject.getFileContent());
            }
            
            log.info("英语口语练习结束：出参:{}", fileObject.getFileName());
            return fileObject;
        } catch (Exception e) {
            log.error("调用语音服务失败", e);
            throw new BusinessException("生成语音失败: " + e.getMessage());
        }
    }

    private void saveConversationHistory(ConversationDto conversationDto) {
        // 如果是新对话，创建对话记录
        if (StringUtils.isEmpty(conversationDto.getConversationId())) {
            Conversation conversation = new Conversation();
            conversation.setId(UUID.randomUUID().toString());
            conversation.setUserId(conversationDto.getUserId());
            conversation.setTitle(conversationDto.getCurrentText().substring(0, 
                Math.min(conversationDto.getCurrentText().length(), 50)));
            conversationRepository.save(conversation);
            conversationDto.setConversationId(conversation.getId());
        }

        // 保存用户问题
        ConversationHistory userMessage = new ConversationHistory();
        userMessage.setId(UUID.randomUUID().toString());
        userMessage.setConversationId(conversationDto.getConversationId());
        userMessage.setRole("user");
        userMessage.setContent(conversationDto.getCurrentText());
        userMessage.setSequence(getNextSequence(conversationDto.getConversationId()));
        conversationHistoryRepository.save(userMessage);

        // 保存AI回答
        ConversationHistory aiMessage = new ConversationHistory();
        aiMessage.setId(UUID.randomUUID().toString());
        aiMessage.setConversationId(conversationDto.getConversationId());
        aiMessage.setRole("assistant");
        aiMessage.setContent("AI语音回答"); // 可以保存语音文件路径或其他相关信息
        aiMessage.setSequence(getNextSequence(conversationDto.getConversationId()));
        conversationHistoryRepository.save(aiMessage);
    }

    private int getNextSequence(String conversationId) {
        Integer maxSequence = conversationHistoryRepository.getMaxSequence(conversationId);
        return maxSequence == null ? 1 : maxSequence + 1;
    }

    // 获取用户的对话列表
    public List<Conversation> getUserConversations(String userId) {
        return conversationRepository.findByUserId(userId);
    }

    // 获取对话历史
    public List<ConversationHistory> getConversationHistory(String conversationId) {
        return conversationHistoryRepository.findByConversationId(conversationId);
    }

    /**
     * 处理上传文件
     * 支持多种文件格式,保存文件并提取单词总数
     *
     * @param fileObjects 文件对象列表
     * @return 处理结果,包含提取的单词总数
     */
    @Override
    public CommonRespDataDto dealInputFile(List<FileObject> fileObjects) {
        CommonRespDataDto response = new CommonRespDataDto();
        
        try {
            validateFiles(fileObjects);
            
            int totalWords = 0;
            //在此处由于还未生成用户的学习计划，所以需要先创建学习计划的数据，并且生成一条学习计划id占位，与document关联
            //此处仅保存文件属性即可，待AI分析出这批文件所包含的单词总数，更新到学习计划中。并返回该数据。

            //创建学习计划

            //保存文件
            for(FileObject file : fileObjects) {
                // 保存文件
                String filePath = fileStorageService.saveFile(file);
                
                // 创建文档记录,状态为待处理
                Document document = new Document();
                document.setUserId(UUID.randomUUID().toString());
                document.setUserId(null); // 此时还未绑定用户
                document.setFileName(file.getFileName());
                document.setFilePath(filePath);
                document.setFileSize((long)file.getFileContent().length);
                document.setStatus(DocumentStatus.PENDING);
                documentRepository.save(document);
                
                // 调用Python服务提取单词
                //需要给python提供文件信息及对应的指令
                JSONObject extractRequest = new JSONObject();
                extractRequest.put("filePath", filePath);
                extractRequest.put("documentId", document.getUserId());
                String command = "请你分析提取该文件中的单词数量";
                extractRequest.put("command", command);
                JSONObject result = pythonRestClient.callPythonService(
                    wordExtractApiUrl,
                    extractRequest,
                    JSONObject.class
                );
                
                // 更新文档的单词数
                totalWords += result.getIntValue("wordCount");
                document.setWordCount(result.getIntValue("wordCount"));
                documentRepository.update(document);
            }
            
            response.setCode("0");
            response.setCodeMessage("文件处理成功");
            response.setData(JSONObject.parseObject(String.valueOf(totalWords))); // 返回单词总数
        } catch (BusinessException e) {
            log.error("处理上传文件失败: {}", e.getMessage());
            response.setCode("1");
            response.setCodeMessage(e.getMessage());
        } catch (Exception e) {
            log.error("处理上传文件发生系统错误", e);
            response.setCode("2");
            response.setCodeMessage("系统错误,请稍后重试");
        }
        
        return response;
    }

    /**
     * 创建用户学习计划
     * 根据用户选择的参数生成个性化学习计划,并创建每日单词列表
     *
     * @param userScheduleDto 学习计划参数
     * @return 创建结果
     */
    @Override
    public CommonRespDataDto createUserSchedule(UserScheduleDto userScheduleDto) {
        CommonRespDataDto response = new CommonRespDataDto();
        
        try {
            validateScheduleParams(userScheduleDto);

            // 查找待处理的文档
            List<Document> documents = documentRepository.findByStatus(DocumentStatus.PENDING);
            if(documents.isEmpty()) {
                throw new BusinessException("未找到待处理的文档");
            }

            // 更新文档用户ID并标记为已完成
            for(Document doc : documents) {
                doc.setUserId(userScheduleDto.getUserId());
                doc.setStatus(DocumentStatus.COMPLETED);
                documentRepository.update(doc);
            }

            // 调用Python服务生成学习计划和单词列表
            JSONObject request = new JSONObject();
            request.put("userId", userScheduleDto.getUserId());
            request.put("dailyWords", userScheduleDto.getDailyWords());
            request.put("countWords", userScheduleDto.getCountWords());
            request.put("studyModel", userScheduleDto.getStudyModel());
            request.put("documentIds", documents.stream().map(Document::getUserId).toList());
            
            JSONObject result = pythonRestClient.callPythonService(
                studyPlanApiUrl,
                request,
                JSONObject.class
            );
            
            // 保存学习计划
            StudyPlan plan = new StudyPlan();
            plan.setId(UUID.randomUUID().toString());
            plan.setUserId(userScheduleDto.getUserId());
            //plan.setDocumentId(documents.get(0).getPlanId()); // 如果有多个文档,可能需要调整
            plan.setDailyWords(userScheduleDto.getDailyWords());
            plan.setTotalDays(calculateTotalDays(userScheduleDto));
            plan.setCurrentDay(1);
            plan.setStatus(StudyPlanStatus.IN_PROGRESS);
            studyPlanRepository.save(plan);
            
            // 保存单词列表
            List<JSONObject> wordList = result.getJSONArray("words").toList(JSONObject.class);
            for(int i = 0; i < wordList.size(); i++) {
                JSONObject wordJson = wordList.get(i);
                Word word = new Word();
                word.setId(UUID.randomUUID().toString());
                word.setDocumentId(plan.getDocumentId());
                word.setWord(wordJson.getString("word"));
                word.setTranslation(wordJson.getString("translation"));
                word.setPronunciation(wordJson.getString("pronunciation"));
                wordRepository.save(word);
            }
            
            response.setCode("0");
            response.setCodeMessage("学习计划创建成功");
        } catch (BusinessException e) {
            log.error("创建学习计划失败: {}", e.getMessage());
            response.setCode("1");
            response.setCodeMessage(e.getMessage());
        } catch (Exception e) {
            log.error("创建学习计划发生系统错误", e);
            response.setCode("2");
            response.setCodeMessage("系统错误,请稍后重试");
        }
        
        return response;
    }

    /**
     * 开始每日学习计划
     * 获取当天需要学习的单词并生成语音
     *
     * @param dailyWordsDto 每日学习参数
     * @return 学习内容
     */
    @Override
    public WordDto startDailySchedule(DailyWordsDto dailyWordsDto) {
        try {
            // 获取学习计划
            StudyPlan plan = studyPlanRepository.findById(dailyWordsDto.getScheduleId());
            if(plan == null) {
                throw new BusinessException("学习计划不存在");
            }

            // 验证学习进度
            if(plan.isCompleted()) {
                throw new BusinessException("学习计划已完成");
            }
            
            // 获取当天要学习的单词
            List<Word> words = wordRepository.findDailyWords(
                plan.getId(),
                plan.getCurrentDay(),
                plan.getDailyWords()
            );
            
            if(words.isEmpty()) {
                throw new BusinessException("今日没有需要学习的单词");
            }

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
            
            // 更新学习进度
            plan.moveToNextDay();
            studyPlanRepository.update(plan);
            
            return wordDto;
        } catch (BusinessException e) {
            log.error("获取每日单词失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("获取每日单词发生系统错误", e);
            throw new BusinessException("获取学习内容失败");
        }
    }

    /**
     * 验证上传文件
     * 检查文件格式和大小是否符合要求
     */
    private void validateFiles(List<FileObject> files) {
        if(files == null || files.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }
        
        if(files.size() > 10) {
            throw new BusinessException("单次最多上传10个文件");
        }

        for(FileObject file : files) {
            // 检查文件大小
            if(file.getFileContent().length > maxDocumentSize) {
                throw new BusinessException("文件过大,最大支持" + maxDocumentSize/1024/1024 + "MB");
            }
            
            // 检查文件格式
            String extension = getFileExtension(file.getFileName());
            if(!SUPPORTED_FORMATS.contains(extension.toLowerCase())) {
                throw new BusinessException("不支持的文件格式: " + extension);
            }
        }
    }

    /**
     * 验证学习计划参数
     */
    private void validateScheduleParams(UserScheduleDto dto) {
        if(dto.getDailyWords() == null || dto.getDailyWords() < 1) {
            throw new BusinessException("每日学习单词数必须大于0");
        }
        
        if(dto.getCountWords() == null || dto.getCountWords() < 1) {
            throw new BusinessException("总单词数必须大于0");
        }
        
        if(dto.getDailyWords() > dto.getCountWords()) {
            throw new BusinessException("每日学习单词数不能大于总单词数");
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
    
    /**
     * 计算总学习天数
     */
    private int calculateTotalDays(UserScheduleDto dto) {
        return (int)Math.ceil(dto.getCountWords() / (double)dto.getDailyWords());
    }
} 