package com.changjiang.elearn.application.service.impl;

import com.changjiang.elearn.api.service.SpokenEnglish;
import com.changjiang.grpc.annotation.GrpcService;

import java.rmi.registry.Registry;

@GrpcService("elearn")
public class SpokenEnglishImpl implements SpokenEnglish {

    @Override
    public String spokenEnglish(String text) {
        return "hello world";
    }

    @Override
    public String spokenEnglishNone(String text) {
        return "";
    }
}
