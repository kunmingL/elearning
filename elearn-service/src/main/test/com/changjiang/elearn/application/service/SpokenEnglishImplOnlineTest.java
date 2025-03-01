package com.changjiang.elearn.application.service;

import com.changjiang.elearn.api.dto.DealInputFileRespDto;
import com.changjiang.elearn.api.dto.FileObject;
import com.changjiang.elearn.application.service.impl.SpokenEnglishImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SpokenEnglishImplOnlineTest extends BaseJunitTest{
    @Autowired
    SpokenEnglishImpl spokenEnglish;

    @Test
    public void dealInputFileTest() throws Exception{
        List<FileObject> fileObjects = new ArrayList<>();
        FileObject fileObject = new FileObject();
        File file = new File("C:\\Users\\77032\\Downloads\\300个小短句配套讲义（上）\\05-08.pdf");
        fileObject.setFileName(file.getName());
        fileObject.setUserId("770320622");
        fileObject.setFileContent(Files.readAllBytes(file.toPath()));
        fileObjects.add(fileObject);
        DealInputFileRespDto dealInputFileRespDto = spokenEnglish.dealInputFile(fileObjects);
        System.out.println(dealInputFileRespDto);
    }
}
