package com.changjiang.elearn.infrastructure.service;

import com.changjiang.elearn.domain.model.Document;
import com.changjiang.elearn.domain.model.Word;
import com.changjiang.elearn.domain.repository.DocumentRepository;
import com.changjiang.elearn.domain.repository.WordRepository;
import com.changjiang.python.PythonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AsyncDocumentService {

    @Autowired
    private PythonRestClient pythonRestClient;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private WordRepository wordRepository;

    @Value("${python.api.word-extract}")
    private String wordExtractApiUrl;

    @Async
    public void processDocument(Document document) {
        try {
            // 更新文档状态为处理中
            document.markAsProcessing();
            documentRepository.update(document);

            // 调用Python服务提取单词
            List<Word> words = pythonRestClient.callPythonService(
                wordExtractApiUrl,
                document,
                List.class
            );

            // 保存单词
            for (Word word : words) {
                wordRepository.save(word);
            }

            // 更新文档状态为完成
            document.completeProcessing(words.size());
            documentRepository.update(document);

        } catch (Exception e) {
            log.error("处理文档失败: " + document.getId(), e);
            throw new RuntimeException("处理文档失败", e);
        }
    }
} 