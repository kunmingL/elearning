package com.changjiang.elearn.application.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.changjiang.base.utils.CreateIdUtil;
import com.changjiang.elearn.api.dto.*;
import com.changjiang.elearn.api.service.SpokenEnglish;
import com.changjiang.elearn.application.assember.DocumentDTOMapper;
import com.changjiang.elearn.application.assember.StudyPlanDTOMapper;
import com.changjiang.elearn.application.assember.WordDTOMapper;
import com.changjiang.elearn.domain.enums.DailyStudyStatus;
import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import com.changjiang.elearn.domain.model.*;
import com.changjiang.elearn.domain.repository.*;
import com.changjiang.elearn.domain.repository.ConversationHistoryRepository;
import com.changjiang.elearn.domain.repository.ConversationRepository;
import com.changjiang.elearn.domain.repository.DailyStudyRepository;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.domain.repository.StudyPlanRepository;
import com.changjiang.elearn.domain.repository.WordRepository;
import com.changjiang.elearn.infrastructure.exception.BusinessException;
import com.changjiang.elearn.infrastructure.service.FileStorageService;
import com.changjiang.elearn.utils.ElearnPythonRestClient;
import com.changjiang.grpc.annotation.GrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    /**
     * 英语口语练习
     * 将文本转换为语音
     *
     * @param conversationDto 待转换的文本
     * @return 语音文件对象
     */
    @Override
    public FileObject spokenEnglish(ConversationInputDto conversationDto) {
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
            if (!CollectionUtils.isEmpty(conversationDto.getHistory())) {
                List<JSONObject> historyList = conversationDto.getHistory().stream()
                    .map(msg -> {
                        JSONObject msgObj = new JSONObject();
                        msgObj.put("role", msg.getRole());
                        msgObj.put("content", msg.getContent());
                        return msgObj;
                    })
                    .collect(Collectors.toList());
                jsonObject.put("history", historyList);
            }

            // 调用Python服务
            FileObject fileObject = pythonRestClient.fileObjCallPythonService(
                speechApiUrl, 
                jsonObject,
                FileObject.class
            );
            
            // 获取AI回复的文本
            String aiReplyText = fileObject.getFileText();

            // 保存对话历史
            saveConversationHistory(conversationDto, aiReplyText);
            
            // 设置音频文件名
            fileObject.setFileName(UUID.randomUUID().toString() + ".mp3");
            
            log.info("英语口语练习结束：出参:{}", fileObject.getFileName());
            return fileObject;
        } catch (Exception e) {
            log.error("调用语音服务失败", e);
            throw new BusinessException("生成语音失败: " + e.getMessage());
        }
    }

    private void saveConversationHistory(ConversationInputDto conversationDto, String aiReplyText) {
        // 如果是新对话，创建对话记录
        if (StringUtils.isEmpty(conversationDto.getConversationId())) {
            String currentText = conversationDto.getCurrentText();
            int maxLength = Math.min(currentText.length(), 10); // 取字符串长度和10的最小值
            Conversation conversation = Conversation.builder()
                .conversationId(CreateIdUtil.nextIdToString())
                .userId(conversationDto.getUserId())
                .title(conversationDto.getCurrentText().substring(0, maxLength))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
            conversationRepository.save(conversation);
            conversationDto.setConversationId(conversation.getConversationId());
        }

        // 保存用户问题
        ConversationHistory userMessage = ConversationHistory.builder()
            .historyId(CreateIdUtil.nextIdToString())
            .conversationId(conversationDto.getConversationId())
            .role("user")
            .content(conversationDto.getCurrentText())
            .sequence(getNextSequence(conversationDto.getConversationId()))
            .createTime(new Date())
            .build();
        conversationHistoryRepository.save(userMessage);

        // 保存AI回答
        ConversationHistory aiMessage = ConversationHistory.builder()
            .historyId(CreateIdUtil.nextIdToString())
            .conversationId(conversationDto.getConversationId())
            .role("assistant")
            .content(aiReplyText)
            .sequence(getNextSequence(conversationDto.getConversationId()))
            .createTime(new Date())
            .build();
        conversationHistoryRepository.save(aiMessage);
    }

    private int getNextSequence(String conversationId) {
        Integer maxSequence = conversationHistoryRepository.getMaxSequence(conversationId);
        return maxSequence == null ? 1 : maxSequence + 1;
    }

    // 获取用户的对话列表
    public List<Conversation> getUserConversations(String userId) {
        log.info("获取用户的对话列表开始：入参:{}", userId);
        Conversation build = Conversation.builder().userId(userId).build();
        List<Conversation> byCondition = conversationRepository.findByCondition(build);
        log.info("获取用户的对话列表结束：出参:{}", byCondition);
        return byCondition;
    }

    // 获取对话历史
    public List<ConversationHistory> getConversationHistory(String conversationId) {
        log.info("获取对话历史开始：入参:{}", conversationId);
        ConversationHistory build = ConversationHistory.builder().conversationId(conversationId).build();
        List<ConversationHistory> byCondition = conversationHistoryRepository.findByCondition(build);
        log.info("获取对话历史结束：出参:{}", byCondition);
        return byCondition;
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
            String planId = CreateIdUtil.nextIdToString();
            StudyPlanDTO studyPlanDTO = new StudyPlanDTO();
            studyPlanDTO.setPlanId(planId);
            studyPlanDTO.setStatus(StudyPlanStatus.UN_EFFECTIVE.getCode());
            // 默认一批文件同一个用户上传
            studyPlanDTO.setUserId(fileObjects.get(0).getUserId());
            StudyPlan studyPlan = StudyPlanDTOMapper.INSTANCE.toDomain(studyPlanDTO);
            studyPlanRepository.save(studyPlan);
            //保存文件
            for(FileObject file : fileObjects) {
                // 保存文件
                String docId = CreateIdUtil.nextIdToString();
                file.setFileName(docId);
                String filePath = fileStorageService.saveFile(file);
                // 创建文档记录,状态为待处理
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setFileName(file.getFileName());
                documentDTO.setFilePath(filePath);
                documentDTO.setStatus(DocumentStatus.PENDING.getCode());
                documentDTO.setDocId(docId);
                documentDTO.setFileSize((long)file.getFileContent().length);
                documentDTO.setUserId(file.getUserId());
                documentDTO.setPlanId(planId);
                Document document = DocumentDTOMapper.INSTANCE.toDomain(documentDTO);
                documentRepository.save(document);
                
                // 调用Python服务提取单词
                //需要给python提供文件信息及对应的指令
                JSONObject extractRequest = new JSONObject();
                extractRequest.put("filePath", filePath);
                String command = "请你分析提取该文件中的单词数量";
                extractRequest.put("command", command);
                JSONObject result = pythonRestClient.callPythonService(
                    wordExtractApiUrl,
                    extractRequest,
                    JSONObject.class
                );
                
                // 更新文档的单词数
                totalWords += result.getIntValue("wordCount");
            }
            studyPlan.changeTotalwords(totalWords);
            studyPlanRepository.update(studyPlan);
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
     * @param studyPlanDTO 学习计划参数
     * @return 创建结果
     */
    @Override
    public CommonRespDataDto createUserSchedule(StudyPlanDTO studyPlanDTO) {
        CommonRespDataDto response = new CommonRespDataDto();
        try {
            validateScheduleParams(studyPlanDTO);
            // 根据用户id，计划id，查找待处理的文档
            Document document = Document.builder().userId(studyPlanDTO.getUserId())
                    .planId(studyPlanDTO.getPlanId())
                    .status(DocumentStatus.PENDING.getCode())
                    .build();

            List<Document> documents = documentRepository.findByCondition(document);
            if (documents.isEmpty()) {
                throw new BusinessException("未找到待处理的文档");
            }
            //找到所有文档，将其一并送给ai处理，拿到最后结果
            //Ai 按每天输出单词列表 输出要素 Map<day,List<对象>> key是天数，vakue是按模版的单词列表
            //遍历map，将每个list中的单词保存到数据库中
            //在这里需要同时生成每天学习计划和每天具体单词
            //将其拼接成一段提示此输入给ai
            // request.put("dailyWords", studyPlanDTO.getDailyWords());
            // request.put("countWords", userScheduleDto.getCountWords());
            // request.put("studyModel", userScheduleDto.getStudyModel());
            //request.put("documentIds", documents.stream().map(Document::getUserId).toList());

            JSONObject request = new JSONObject();
            JSONObject result = pythonRestClient.callPythonService(
                    studyPlanApiUrl,
                    request,
                    JSONObject.class
            );

            // for (Map.Entry<Integer, List<Object>> entry : result.entrySet()) {
            //     Integer day = entry.getKey();
            //     DailyStudy dailyStudy = DailyStudy.builder().planId(studyPlanDTO.getPlanId())
            //             .userId(studyPlanDTO.getUserId())
            //             .studyDay(day)
            //             .wordCount(entry.getValue().size())
            //             .status(DailyStudyStatus.UN_STARTED.getCode())
            //             .currentWordIdx(0)
            //             .build();
            //     dailyStudyRepository.save(dailyStudy);
            //     List<Object> wordList = entry.getValue();
            //     for (int i = 0; i < wordList.size(); i++) {
            //         JSONObject wordJson = (JSONObject) wordList.get(i);
            //         Word.builder().word(wordJson.getString("word"))
            //                 .pronunciation(wordJson.getString("pronunciation"))
            //                 .wordTranslation(wordJson.getString("wordTranslation"))
            //                 .sentence(wordJson.getString("sentence"))
            //                 .sentenceTranslation(wordJson.getString("sentenceTranslation"))
            //                 .wordId(CreateIdUtil.nextIdToString())
            //                 .wordIdx(i)
            //                 .planId(studyPlanDTO.getPlanId())
            //                 .build();
            //     }
            //
            //     for (Document doc : documents) {
            //         // 调用Python服务生成学习计划和单词列表
            //         // 更新文档用户ID并标记为已完成
            //         doc.changeStatus(DocumentStatus.COMPLETED.getCode());
            //         documentRepository.update(doc);
            //     }
            //     response.setCode("0");
            //     response.setCodeMessage("学习计划创建成功");
            // }
        }catch (BusinessException e) {
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
    public WordDTO startDailySchedule(DailyStudyDTO dailyWordsDto) {
        try {
            // 获取学习计划
            StudyPlan plan = studyPlanRepository.findById(dailyWordsDto.getPlanId());
            if(plan == null) {
                throw new BusinessException("学习计划不存在");
            }
            // 验证学习进度
            if(plan.isCompleted()) {
                throw new BusinessException("学习计划已完成");
            }
            //根据计划id、userid、当前学习天数，查找当天计划
            DailyStudy dailyStudyBuilder = DailyStudy.builder().planId(plan.getPlanId()).userId(plan.getUserId()).studyDay(plan.getCurrentDay()).build();
            List<DailyStudy> dailyStudyList = dailyStudyRepository.findByCondition(dailyStudyBuilder);
            //获取当前要听写的单词
            if (CollectionUtils.isEmpty(dailyStudyList)){
                throw new BusinessException("当前学习进度不存在");
            }
            //根据刚才查询条件，如果存在当天学习进度，那么有且只有一个
            DailyStudy dailyStudy = dailyStudyList.get(0);
            Word wordBuilder = Word.builder().dailyId(dailyStudy.getDailyId()).planId(dailyStudy.getPlanId()).wordIdx(dailyStudy.getCurrentWordIdx()).build();
            // 获取当前的单词
            List<Word> words = wordRepository.findByCondition(wordBuilder);
            if(words.isEmpty()) {
                throw new BusinessException("今日没有需要学习的单词");
            }
            //根据刚才查询条件，如果存在当前学习单词，那么有且只有一个
            Word word = words.get(0);
            WordDTO wordDTO = WordDTOMapper.INSTANCE.toDTO(word);
            //更新当前单词学习进度
            dailyStudy.changeCurrentWordIdx(dailyStudy.getCurrentWordIdx() + 1);
            //验证是否已经完成当天计划
            if(dailyStudy.getCurrentWordIdx() >= dailyStudy.getWordCount()) {
                // 更新学习进度
                plan.moveToNextDay();
                studyPlanRepository.update(plan);
                // 更新当前学习进度
                dailyStudy.changeStatus(DailyStudyStatus.COMPLETED.getCode());
                dailyStudyRepository.update(dailyStudy);
            }
            return wordDTO;
            // 生成单词音频
            // 调用Python服务生成发音 暂时不调用音频 让前端生成
            // byte[] audioBytes = pythonRestClient.callPythonServiceForBytes(
            //         speechApiUrl,
            //         JSONObject.from(word)
            // );
            //
            //     // 设置音频文件
            //     FileObject audio = new FileObject();
            //     audio.setFileName(word.getId() + ".mp3");
            //     audio.setFileContent(audioBytes);
                
            //wordDto.setWord(audio);
            // 更新学习进度
            // plan.moveToNextDay();
            // studyPlanRepository.update(plan);


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
    private void validateScheduleParams(StudyPlanDTO dto) {
        if(dto.getDailyWords() == null || dto.getDailyWords() < 1) {
            throw new BusinessException("每日学习单词数必须大于0");
        }
        
        if(dto.getTotalWords() == null || dto.getTotalWords() < 1) {
            throw new BusinessException("总单词数必须大于0");
        }
        
        if(dto.getDailyWords() > dto.getTotalWords()) {
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