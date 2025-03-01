package com.changjiang.elearn.application.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.changjiang.base.utils.CreateIdUtil;
import com.changjiang.elearn.api.dto.*;
import com.changjiang.elearn.api.service.SpokenEnglish;
import com.changjiang.elearn.application.assember.*;
import com.changjiang.elearn.domain.enums.DailyStudyStatus;
import com.changjiang.elearn.domain.enums.DocumentStatus;
import com.changjiang.elearn.domain.enums.StudyPlanStatus;
import com.changjiang.elearn.domain.enums.TemplateEnum;
import com.changjiang.elearn.domain.model.*;
import com.changjiang.elearn.domain.repository.*;
import com.changjiang.elearn.infrastructure.exception.BusinessException;
import com.changjiang.elearn.infrastructure.service.FileStorageService;
import com.changjiang.elearn.utils.ElearnPythonRestClient;
import com.changjiang.elearn.utils.Language;
import com.changjiang.elearn.utils.OcrUtils;
import com.changjiang.grpc.annotation.GrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    
    @Value("${python.api.word-extract}")
    private String studyPlanApiUrl;
    
    @Value("${document.max-size}")
    private long maxDocumentSize;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationHistoryRepository conversationHistoryRepository;

    @Autowired
    private DailyStudyRepository dailyStudyRepository;

    @Autowired
    private OcrUtils ocrUtils;

    @Autowired
    private ElearnPythonRestClient elearnPythonRestClient;
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
            String aiReplyText = pythonRestClient.callPythonService(
                speechApiUrl, 
                jsonObject,
                String.class
            );
            
            // 获取AI回复的文本
           // String aiReplyText = fileObject.getFileText();

            // 保存对话历史
            saveConversationHistory(conversationDto, aiReplyText);
            
            // 设置音频文件名
            FileObject fileObject = new FileObject();
            fileObject.setFileText(aiReplyText);
            fileObject.setFileName(CreateIdUtil.nextIdToString() + ".mp3");
            
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
    public List<ConversationDTO> getUserConversations(String userId) {
        log.info("获取用户的对话列表开始：入参:{}", userId);
        Conversation build = Conversation.builder().userId(userId).build();
        List<Conversation> conversations = conversationRepository.findByCondition(build);
        List<ConversationDTO> dtoList = ConversationDTOMapper.INSTANCE.toDTOList(conversations);
        log.info("获取用户的对话列表结束：出参:{}", dtoList);
        return dtoList;
    }

    // 获取对话历史
    public List<ConversationHistoryDTO> getConversationHistory(String conversationId) {
        log.info("获取对话历史开始：入参:{}", conversationId);
        ConversationHistory build = ConversationHistory.builder().conversationId(conversationId).build();
        List<ConversationHistory> conversationHistories = conversationHistoryRepository.findByCondition(build);
        List<ConversationHistoryDTO> conversationHistoryDTOS = ConversationHistoryDTOMapper.INSTANCE.toDTOList(conversationHistories);
        log.info("获取对话历史结束：出参:{}", conversationHistoryDTOS);
        return conversationHistoryDTOS;
    }

    /**
     * 处理上传文件
     * 支持多种文件格式,保存文件并提取单词总数
     *
     * @param fileObjects 文件对象列表
     * @return 处理结果,包含提取的单词总数
     */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public DealInputFileRespDto dealInputFile(List<FileObject> fileObjects) {
        DealInputFileRespDto response = new DealInputFileRespDto();
        try {
            validateFiles(fileObjects);
            //在此处由于还未生成用户的学习计划，所以需要先创建学习计划的数据，并且生成一条学习计划id占位，与document关联
            //此处仅保存文件属性即可，待AI分析出这批文件所包含的单词总数，更新到学习计划中。并返回该数据。

            //创建学习计划
            String planId = CreateIdUtil.nextIdToString();
            StudyPlanDTO studyPlanDTO = new StudyPlanDTO();
            studyPlanDTO.setPlanId(planId);
            studyPlanDTO.setStatus(StudyPlanStatus.UN_EFFECTIVE.getCode());
            // 默认一批文件同一个用户上传
            studyPlanDTO.setUserId(fileObjects.get(0).getUserId());

            //studyPlanRepository.save(studyPlan);
            Map<String, List<Language>> fileLanguageMap = new HashMap<>();
            List<String> filePaths = new ArrayList<>();
            //保存文件
            for(FileObject file : fileObjects) {
                // 保存文件
                String docId = CreateIdUtil.nextIdToString();
                file.setFileName(docId + "." + getFileExtension(file.getFileName()));
                String filePath = fileStorageService.saveFile(file);
                // 创建文档记录,状态为待处理
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setFileName(file.getFileName());
                documentDTO.setFilePath(filePath);
                documentDTO.setStatus(DocumentStatus.PENDING.getCode());
                documentDTO.setDocId(docId);
                //documentDTO.setFileSize((long)file.getFileContent().length);
                documentDTO.setUserId(file.getUserId());
                documentDTO.setPlanId(planId);
                Document document = DocumentDTOMapper.INSTANCE.toDomain(documentDTO);
                documentRepository.save(document);
                //fileLanguageMap.put(filePath,Arrays.asList(Language.ENG));
                filePaths.add(filePath);
            }
            //先进行ORC内容识别，再提交给AI处理
            //Map<String, String> stringStringMap = ocrUtils.aggregateOcrResults(fileLanguageMap);
            //String results = ocrUtils.mergeOcrResultsToString(stringStringMap);

            // 调用Python服务提取单词
            //需要给python提供文件信息及对应的指令
             JSONObject extractRequest = new JSONObject();
             extractRequest.put("filePath", filePaths);
             String command = "请你分析提取输入内容的重点英文单词数量。重点单词的提取规则如下：\n" +
                     "1. **特殊标记的单词**：如果文件中有加粗、斜体、下划线或其他特殊标记的单词，提取这些单词。\n" +
                     "2. **考试词汇**：如果单词属于 CET-4、CET-6、雅思、托福等考试词汇，提取这些单词。\n" +
                     "3. **单词列表**：如果文件是一个单词列表，则提取其中全部英文单词。\n" +
                     "4. **重复单词**：如果一个单词在文件中多次出现，只计算一次。\n" +
                     "\n" +
                     "请严格按照以上规则提取重点单词，并只返回一个数字，表示提取出的重点单词数量。不要返回任何其他内容。";
            extractRequest.put("prompt_template", command);
            String wordCount = pythonRestClient.callPythonService(
                    wordExtractApiUrl,
                    extractRequest,
                    String.class
            );
            studyPlanDTO.setTotalWords(Integer.parseInt(wordCount));
            StudyPlan studyPlan = StudyPlanDTOMapper.INSTANCE.toDomain(studyPlanDTO);
            studyPlanRepository.save(studyPlan);
            // response.setCode("200");
            // response.setCodeMessage("文件处理成功");
            // response.setData(wordCount); // 返回单词总数
            response.setTotalWords(studyPlan.getTotalWords());
            response.setStudyPlanId(studyPlan.getPlanId());
        } catch (BusinessException e) {
            log.error("处理上传文件失败: {}", e.getMessage());
            // response.setCode("1");
            // response.setCodeMessage(e.getMessage());
        } catch (Exception e) {
            log.error("处理上传文件发生系统错误", e);
            // response.setCode("2");
            // response.setCodeMessage("系统错误,请稍后重试");
        }
        log.info("处理上传文件结束：出参:{}", response);
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
            Document document = Document.builder().planId(studyPlanDTO.getPlanId())
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
            StringBuilder command = new StringBuilder();
            TemplateEnum templateEnumByCode = TemplateEnum.getTemplateEnumByCode(studyPlanDTO.getTemplateModel());
            String templateName = templateEnumByCode.getTemplateName();
            command.append("请你将这些文档，总共")
                    .append(studyPlanDTO.getTotalWords())
                    .append("个英文单词（除了最后一天外），按照平均每天")
                    .append(studyPlanDTO.getDailyWords())
                    .append("个单词进行整理，并按照以下模版输出：")
                    .append(templateName)
                    .append("具体要求：\n" +
                            "1. 输出一个完整的 JSON 对象，key 是天数（整数类型），value 是每天学习的单词列表。\n" +
                            "2. 每个单词列表中的对象必须包含模版中属性：\n" +
                            "3. 输出一个纯净的 JSON 对象，不要包含任何额外文本或标记（如 ```json 或 Note:）。\n" +
                            "4. 严格按照上述格式输出，不要添加任何解释或多余话术。\n" +
                            "5. 确保 JSON 对象是完整的，并以 `}` 结尾。\n");
            JSONObject request = new JSONObject();
            request.put("prompt_template", command.toString());
            request.put("filePath", documents.stream().map(Document::getFilePath).toList());
            JSONObject jsonObject = elearnPythonRestClient.createStudyPlan(
                    studyPlanApiUrl,
                    request
            );
            // JSONObject jsonObject = new JSONObject();
            // jsonObject.put("response", "{\"1\":[{\"word\":\"cult\",\"pronunciation\":\"/kʌlt/\",\"wordTranslation\":\"崇拜，宗派\",\"sentence\":\"Butthecultoftheauthenticandthepersonal,'doingourownthing',hasspeltthedeathofformalspeech,writing,poetryandmusic.\",\"sentenceTranslation\":\"但是，对真实和个人的崇拜，和对“做我们自己的事”的追求，已经导致了正式演讲、写作、诗歌和音乐的消亡。\"},{\"word\":\"authentic\",\"pronunciation\":\"/ɔːˈθentɪk/\",\"wordTranslation\":\"真实的，可信的\",\"sentence\":\"Butthecultoftheauthenticandthepersonal,'doingourownthing',hasspeltthedeathofformalspeech,writing,poetryandmusic.\",\"sentenceTranslation\":\"但是，对真实和个人的崇拜，和对“做我们自己的事”的追求，已经导致了正式演讲、写作、诗歌和音乐的消亡。\"},{\"word\":\"spelt\",\"pronunciation\":\"/spelt/\",\"wordTranslation\":\"拼写（spell的过去式）；导致\",\"sentence\":\"Butthecultoftheauthenticandthepersonal,'doingourownthing',hasspeltthedeathofformalspeech,writing,poetryandmusic.\",\"sentenceTranslation\":\"但是，对真实和个人的崇拜，和对“做我们自己的事”的追求，已经导致了正式演讲、写作、诗歌和音乐的消亡。\"},{\"word\":\"formal\",\"pronunciation\":\"/ˈfɔːrml/\",\"wordTranslation\":\"正式的\",\"sentence\":\"Butthecultoftheauthenticandthepersonal,'doingourownthing',hasspeltthedeathofformalspeech,writing,poetryandmusic.\",\"sentenceTranslation\":\"但是，对真实和个人的崇拜，和对“做我们自己的事”的追求，已经导致了正式演讲、写作、诗歌和音乐的消亡。\"},{\"word\":\"methodologies\",\"pronunciation\":\"/mɛθəˈdɒlədʒɪz/\",\"wordTranslation\":\"方法论\",\"sentence\":\"Socialsciencemethodologieshadtobeadaptedtoadisciplinegovernedbytheprimacyofhistoricalsourcesratherthantheimperativesofthecontemporaryworld.\",\"sentenceTranslation\":\"社会科学方法论必须进行改变以适应这样一个学科，其受史料至上的支配，而不是受当代社会的需要的支配。\"}],\"2\":[{\"word\":\"discipline\",\"pronunciation\":\"/ˈdɪsɪplɪn/\",\"wordTranslation\":\"学科，训练\",\"sentence\":\"Socialsciencemethodologieshadtobeadaptedtoadisciplinegovernedbytheprimacyofhistoricalsourcesratherthantheimperativesofthecontemporaryworld.\",\"sentenceTranslation\":\"社会科学方法论必须进行改变以适应这样一个学科，其受史料至上的支配，而不是受当代社会的需要的支配。\"},{\"word\":\"primacy\",\"pronunciation\":\"/ˈpraɪməsi/\",\"wordTranslation\":\"首要地位\",\"sentence\":\"Socialsciencemethodologieshadtobeadaptedtoadisciplinegovernedbytheprimacyofhistoricalsourcesratherthantheimperativesofthecontemporaryworld.\",\"sentenceTranslation\":\"社会科学方法论必须进行改变以适应这样一个学科，其受史料至上的支配，而不是受当代社会的需要的支配。\"},{\"word\":\"historical\",\"pronunciation\":\"/hɪˈstɒrɪkl/\",\"wordTranslation\":\"历史的\",\"sentence\":\"Socialsciencemethodologieshadtobeadaptedtoadisciplinegovernedbytheprimacyofhistoricalsourcesratherthantheimperativesofthecontemporaryworld.\",\"sentenceTranslation\":\"社会科学方法论必须进行改变以适应这样一个学科，其受史料至上的支配，而不是受当代社会的需要的支配。\"},{\"word\":\"imperatives\",\"pronunciation\":\"/ɪmˈpɛrətɪvz/\",\"wordTranslation\":\"命令，要求\",\"sentence\":\"Socialsciencemethodologieshadtobeadaptedtoadisciplinegovernedbytheprimacyofhistoricalsourcesratherthantheimperativesofthecontemporaryworld.\",\"sentenceTranslation\":\"社会科学方法论必须进行改变以适应这样一个学科，其受史料至上的支配，而不是受当代社会的需要的支配。\"},{\"word\":\"infer\",\"pronunciation\":\"/ɪnˈfɜːr/\",\"wordTranslation\":\"推断\",\"sentence\":\"Youinferinformationyoufeelthewriterhasinvitedyoutograspbypresentingyouwithspecificevidenceandclues.\",\"sentenceTranslation\":\"你通过提供的具体证据和线索，来推断你认为作者想让你掌握的信息。\"}],\"3\":[{\"word\":\"presenting\",\"pronunciation\":\"/prɪˈzentɪŋ/\",\"wordTranslation\":\"呈现\",\"sentence\":\"Youinferinformationyoufeelthewriterhasinvitedyoutograspbypresentingyouwithspecificevidenceandclues.\",\"sentenceTranslation\":\"你通过提供的具体证据和线索，来推断你认为作者想让你掌握的信息。\"},{\"word\":\"global\",\"pronunciation\":\"/ˈɡloʊbəl/\",\"wordTranslation\":\"全球的\",\"sentence\":\"Withtheglobalpopulationpredictedtohitcloseto10billionby2050,andforecaststhatagriculturalproductioninsomeregionswillneedtonearlydoubletokeeppace,foodsecurityisincreasinglymakingheadlines.\",\"sentenceTranslation\":\"预计到2050年，全球人口将多达近100亿，预计若要跟上步伐，某些地区的农业产量需接近翻倍，因此，粮食保障问题在越来越多地占据头条新闻。\"},{\"word\":\"forecast\",\"pronunciation\":\"/ˈfɔːrkɑːst/\",\"wordTranslation\":\"预测\",\"sentence\":\"Withtheglobalpopulationpredictedtohitcloseto10billionby2050,andforecaststhatagriculturalproductioninsomeregionswillneedtonearlydoubletokeeppace,foodsecurityisincreasinglymakingheadlines.\",\"sentenceTranslation\":\"预计到2050年，全球人口将多达近100亿，预计若要跟上步伐，某些地区的农业产量需接近翻倍，因此，粮食保障问题在越来越多地占据头条新闻。\"},{\"word\":\"agricultural\",\"pronunciation\":\"/ˌæɡrɪˈkʌltʃərəl/\",\"wordTranslation\":\"农业的\",\"sentence\":\"Withtheglobalpopulationpredictedtohitcloseto10billionby2050,andforecaststhatagriculturalproductioninsomeregionswillneedtonearlydoubletokeeppace,foodsecurityisincreasinglymakingheadlines.\",\"sentenceTranslation\":\"预计到2050年，全球人口将多达近100亿，预计若要跟上步伐，某些地区的农业产量需接近翻倍，因此，粮食保障问题在越来越多地占据头条新闻。\"},{\"word\":\"headlines\",\"pronunciation\":\"/ˈhedlaɪnz/\",\"wordTranslation\":\"头条新闻\",\"sentence\":\"Withtheglobalpopulationpredictedtohitcloseto10billionby2050,andforecaststhatagriculturalproductioninsomeregionswillneedtonearlydoubletokeeppace,foodsecurityisincreasinglymakingheadlines.\",\"sentenceTranslation\":\"预计到2050年，全球人口将多达近100亿，预计若要跟上步伐，某些地区的农业产量需接近翻倍，因此，粮食保障问题在越来越多地占据头条新闻。\"}],\"4\":[{\"word\":\"vision\",\"pronunciation\":\"/ˈvɪʒn/\",\"wordTranslation\":\"远见，愿景\",\"sentence\":\"Itishardtogetright,andrequiresaremarkabledegreeofvision,aswellascooperationbetweencityauthorities,theprivatesector,communitygroupsandculturalorganizations.\",\"sentenceTranslation\":\"成功做到这些很难，不仅需要非同凡响的远见卓识，还需要地方当局、私营部门、社会团体和文化组织间的合作。\"},{\"word\":\"cooperation\",\"pronunciation\":\"/koʊˌɑːpəˈreɪʃn/\",\"wordTranslation\":\"合作\",\"sentence\":\"Itishardtogetright,andrequiresaremarkabledegreeofvision,aswellascooperationbetweencityauthorities,theprivatesector,communitygroupsandculturalorganizations.\",\"sentenceTranslation\":\"成功做到这些很难，不仅需要非同凡响的远见卓识，还需要地方当局、私营部门、社会团体和文化组织间的合作。\"},{\"word\":\"authority\",\"pronunciation\":\"/əˈθɔːrɪti/\",\"wordTranslation\":\"权威，当局\",\"sentence\":\"Itishardtogetright,andrequiresaremarkabledegreeofvision,aswellascooperationbetweencityauthorities,theprivatesector,communitygroupsandculturalorganizations.\",\"sentenceTranslation\":\"成功做到这些很难，不仅需要非同凡响的远见卓识，还需要地方当局、私营部门、社会团体和文化组织间的合作。\"},{\"word\":\"sector\",\"pronunciation\":\"/ˈsektər/\",\"wordTranslation\":\"部门，领域\",\"sentence\":\"Itishardtogetright,andrequiresaremarkabledegreeofvision,aswellascooperationbetweencityauthorities,theprivatesector,communitygroupsandculturalorganizations.\",\"sentenceTranslation\":\"成功做到这些很难，不仅需要非同凡响的远见卓识，还需要地方当局、私营部门、社会团体和文化组织间的合作。\"},{\"word\":\"organization\",\"pronunciation\":\"/ˌɔːrɡənaɪˈzeɪʃn/\",\"wordTranslation\":\"组织\",\"sentence\":\"Itishardtogetright,andrequiresaremarkabledegreeofvision,aswellascooperationbetweencityauthorities,theprivatesector,communitygroupsandculturalorganizations.\",\"sentenceTranslation\":\"成功做到这些很难，不仅需要非同凡响的远见卓识，还需要地方当局、私营部门、社会团体和文化组织间的合作。\"}],\"5\":[{\"word\":\"economic\",\"pronunciation\":\"/ɪˈkɑːnəmɪk/\",\"wordTranslation\":\"经济的\",\"sentence\":\"That'sbecauseeconomicgrowthcanbecorrelatedwithenvironmentaldegradation,whileprotectingtheenvironmentissometimescorrelatedwithgreaterpoverty.\",\"sentenceTranslation\":\"这是因为经济增长可能与环境恶化相关，而环境保护有时与贫困恶化相关。\"},{\"word\":\"correlated\",\"pronunciation\":\"/ˈkɔːrəleɪtɪd/\",\"wordTranslation\":\"相关的\",\"sentence\":\"That'sbecauseeconomicgrowthcanbecorrelatedwithenvironmentaldegradation,whileprotectingtheenvironmentissometimescorrelatedwithgreaterpoverty.\",\"sentenceTranslation\":\"这是因为经济增长可能与环境恶化相关，而环境保护有时与贫困恶化相关。\"},{\"word\":\"degradation\",\"pronunciation\":\"/ˌdiːɡrəˈdeɪʃn/\",\"wordTranslation\":\"退化，恶化\",\"sentence\":\"That'sbecauseeconomicgrowthcanbecorrelatedwithenvironmentaldegradation,whileprotectingtheenvironmentissometimescorrelatedwithgreaterpoverty.\",\"sentenceTranslation\":\"这是因为经济增长可能与环境恶化相关，而环境保护有时与贫困恶化相关。\"},{\"word\":\"environment\",\"pronunciation\":\"/ɪnˈvaɪrənmənt/\",\"wordTranslation\":\"环境\",\"sentence\":\"That'sbecauseeconomicgrowthcanbecorrelatedwithenvironmentaldegradation,whileprotectingtheenvironmentissometimescorrelatedwithgreaterpoverty.\",\"sentenceTranslation\":\"这是因为经济增长可能与环境恶化相关，而环境保护有时与贫困恶化相关。\"},{\"word\":\"poverty\",\"pronunciation\":\"/ˈpɑːvərti/\",\"wordTranslation\":\"贫困\",\"sentence\":\"That'sbecauseeconomicgrowthcanbecorrelatedwithenvironmentaldegradation,whileprotectingtheenvironmentissometimescorrelatedwithgreaterpoverty.\",\"sentenceTranslation\":\"这是因为经济增长可能与环境恶化相关，而环境保护有时与贫困恶化相关。\"}]}");
            System.out.println(jsonObject);
            // 解析为 Map<Integer, List<WordInfo>>
            Map<String, List<WordDTO>> wordMap = JSON.parseObject(
                    jsonObject.get("response").toString(),
                    new TypeReference<Map<String, List<WordDTO>>>() {}
            );
            wordMap.forEach((day, wordList) -> {
                String dailyId = CreateIdUtil.nextIdToString();
                    DailyStudy dailyStudy = DailyStudy.builder().dailyId(dailyId).planId(studyPlanDTO.getPlanId())
                            .userId(studyPlanDTO.getUserId())
                            .studyDay(Integer.parseInt(day))
                            .wordCount(wordList.size())
                            .status(DailyStudyStatus.UN_STARTED.getCode())
                            .currentWordIdx(0)
                            .build();
                    dailyStudyRepository.save(dailyStudy);
                for (WordDTO wordDTO : wordList) {
                    wordDTO.setPlanId(studyPlanDTO.getPlanId());
                    wordDTO.setWordId(CreateIdUtil.nextIdToString());
                    wordDTO.setDailyId(dailyId);
                    wordDTO.setWordIdx(wordList.indexOf(wordDTO));
                    wordRepository.save(WordDTOMapper.INSTANCE.toDomain(wordDTO));
                }
            });
                for (Document doc : documents) {
                    // 调用Python服务生成学习计划和单词列表
                    // 更新文档用户ID并标记为已完成
                    doc.changeStatus(DocumentStatus.COMPLETED.getCode());
                    documentRepository.update(doc);
                }
                response.setCode("0");
                response.setCodeMessage("学习计划创建成功");
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

    public List<StudyPlanDTO> queryStudyPlan(String userid){
        log.info("查询学习计划开始：入参:{}",userid);
        StudyPlan build = StudyPlan.builder().userId(userid).build();
        List<StudyPlan> studyPlans = studyPlanRepository.findByCondition(build);
        List<StudyPlanDTO> dtoList = StudyPlanDTOMapper.INSTANCE.toDTOList(studyPlans);
        log.info("查询学习计划结束：出参:{}",dtoList);
        return dtoList;
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
            dailyStudy.changeCurrentWordIdx(dailyStudy.getCurrentWordIdx()+1);
            //验证是否已经完成当天计划
            if(dailyStudy.getCurrentWordIdx() >= dailyStudy.getWordCount()) {
                // 更新学习进度
                plan.moveToNextDay();
                studyPlanRepository.update(plan);
                // 更新当前学习进度
                dailyStudy.changeStatus(DailyStudyStatus.COMPLETED.getCode());
                dailyStudyRepository.update(dailyStudy);
            }
            dailyStudyRepository.update(dailyStudy);
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
            // // 检查文件大小
            // if(file.getFileContent().length > maxDocumentSize) {
            //     throw new BusinessException("文件过大,最大支持" + maxDocumentSize/1024/1024 + "MB");
            // }
            
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