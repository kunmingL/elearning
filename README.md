# 英语学习系统

## 项目简介
这是一个基于DDD架构设计的英语学习系统,主要功能包括:
- 英语口语练习
- 文档单词提取
- 学习计划生成
- 每日单词学习

## 系统架构
采用DDD分层架构:
- Interface层: 接口层,处理外部请求
- Application层: 应用服务层,编排领域服务
- Domain层: 领域层,包含核心业务逻辑
- Infrastructure层: 基础设施层,提供技术支持

### 核心领域模型
- User: 用户
- StudyPlan: 学习计划
- Word: 单词
- Document: 学习文档

## 主要功能模块
1. 口语练习模块
2. 文档处理模块 
3. 学习计划模块
4. 每日学习模块

## 技术栈
- Spring Boot 
- MyBatis
- MySQL
- Python REST API 