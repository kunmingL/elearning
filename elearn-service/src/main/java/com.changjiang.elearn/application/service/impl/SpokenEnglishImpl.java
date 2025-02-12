package com.changjiang.elearn.application.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.changjiang.elearn.api.dto.CommonRespDataDto;
import com.changjiang.elearn.api.dto.FileObject;
import com.changjiang.elearn.api.dto.UserScheduleDto;
import com.changjiang.elearn.api.dto.WordDto;
import com.changjiang.elearn.api.service.SpokenEnglish;
import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.python.PythonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.registry.Registry;
import java.util.List;

@GrpcService(registry = "elearn")
@Slf4j
public class SpokenEnglishImpl implements SpokenEnglish {

    @Autowired
    private PythonRestClient pythonRestClient;

    @Override
    public FileObject spokenEnglish(String text) {
        log.info("英语口语练习开始：入参:{}",text);
        //调用python
        JSONObject jsonObject = JSONObject.parseObject(text);
        FileObject fileObject = new FileObject();
        //python调用地址
        String url = "";
        byte[] bytes = pythonRestClient.callPythonServiceForBytes(url, jsonObject);
        //fileObject.setFileContent(bytes);
        //log.info("英语口语练习结束：出参:{}",fileObject.getFileName());
        return fileObject;
    }

    /**
     * 处理上传文件 支持（*.pdf, *.doc, *.docx, *.ppt, *.pptx, *.xls, *.xlsx,*.png,*.jpg）
     * 这一步对文件进行保存处理，初步从文件中提取的单词总个数并返回给前端
     * 1、控制文件总大小，如果超过特定大小，则返回前端提示
     * 2、绑定用户与上传文件关系，以备后续多次处理
     * 3、对文件进行保存到指定路径，将文件路径、用户id 关系存储到数据库中
     * @param fileObjects
     * @return
     */
    @Override
    public CommonRespDataDto dealInputFile(List<FileObject> fileObjects) {
        return null;
    }

    /**
     * 根据用户上传的学习计划，生成用户的每日学习计划
     * 学习计划调用python接口，由大模型生成，按每天返回，避免超时
     * 返回一天后，就存储在数据库中，以备后续的学习计划调用
     * @param userScheduleDto
     * @return
     */
    @Override
    public CommonRespDataDto createUserSchedule(UserScheduleDto userScheduleDto) {
        return null;
    }



    /**
     * 根据用户的id、学习计划id、学习的天数、单词索引，查询之前创建的学习计划
     * 单词的语音文件由python接口生成
     * 逐个按照单词组进行返回
     * @param userScheduleDto
     * @return
     */
    @Override
    public WordDto startDailySchedule(UserScheduleDto userScheduleDto) {
        return null;
    }
}
