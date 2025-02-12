package com.changjiang.elearn.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WordDto implements Serializable {
    private FileObject word;
    private FileObject chinese;
    private FileObject sentence;
    private FileObject Csentence;
}
