package com.changjiang.elearn.api.dto;







import lombok.Data;

import java.io.Serializable;

@Data
public class FileObject implements Serializable {

    private String fileName;

    private byte[] fileContent;
}
