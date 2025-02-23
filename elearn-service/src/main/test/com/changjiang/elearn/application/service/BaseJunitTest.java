package com.changjiang.elearn.application.service;

import com.changjiang.elearn.SpringBootApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringBootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseJunitTest {
    @Test
    public void test() {
        System.out.println("aaa");
    }
}
