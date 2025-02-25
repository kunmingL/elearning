# 英语学习助手接口文档

## 1. 英语语音合成接口
### 接口信息
- 接口名称：spokenEnglish
- 接口URL：/changjiang/elearn/spokenEnglish
- 请求方式：POST
- 接口用途：提供AI驱动的英语对话功能,支持上下文对话,并生成语音回复

### 请求参数 (ConversationInputDto)
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | String | 是 | 用户ID |
| conversationId | String | 否 | 对话ID(新对话不需要) |
| currentText | String | 是 | 当前输入的文本 |
| history | List<Message> | 否 | 历史对话记录 |

### Message结构
| 参数名 | 类型 | 说明 |
|--------|------|------|
| role | String | 角色(user/assistant) |
| content | String | 对话内容 |

### 返回参数 (FileObject)
| 参数名 | 类型 | 说明 |
|--------|------|------|
| fileName | String | 音频文件名 |
| fileContent | byte[] | 音频文件内容 |
| fileText | String | AI回复的文本内容 |

### 业务流程
1. 检查输入参数合法性
2. 构建对话上下文
3. 调用AI服务生成回复
4. 保存对话历史
5. 生成语音文件
6. 返回结果

## 2. 文件处理接口
### 接口信息
- 接口名称：dealInputFile
- 接口URL：/changjiang/elearn/dealInputFile
- 请求方式：POST
- 接口用途：处理上传的学习材料文件,提取单词并创建初始学习计划
- 支持格式：pdf, doc, docx, ppt, pptx, xls, xlsx, png, jpg

### 请求参数 (List<FileObject>)
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | String | 是 | 用户ID |
| fileName | String | 是 | 文件名 |
| fileContent | byte[] | 是 | 文件内容 |

### 返回参数 (CommonRespDataDto)
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | String | 响应码：0-成功，1-业务错误，2-系统错误 |
| codeMessage | String | 响应信息 |
| data | Integer | 提取的单词总数 |

### 业务流程
1. 验证文件格式和大小
2. 创建初始学习计划
3. 保存文件并创建文档记录
4. 调用AI服务提取单词
5. 更新单词总数
6. 返回处理结果

## 3. 创建学习计划接口
### 接口信息
- 接口名称：createUserSchedule
- 接口URL：/changjiang/elearn/createUserSchedule
- 请求方式：POST
- 接口用途：根据用户设置生成详细的学习计划和每日单词列表

### 请求参数 (StudyPlanDTO)
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | String | 是 | 用户ID |
| planId | String | 是 | 计划ID |
| dailyWords | Integer | 是 | 每日学习单词数 |
| totalWords | Integer | 是 | 总单词数 |

### 返回参数 (CommonRespDataDto)
| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | String | 响应码：0-成功，1-业务错误，2-系统错误 |
| codeMessage | String | 响应信息 |
| data | Object | 学习计划信息 |

### 业务流程
1. 验证学习计划参数
2. 查找待处理文档
3. 调用AI服务生成学习计划
4. 创建每日学习计划
5. 保存单词列表
6. 更新文档状态

## 4. 获取每日学习内容接口
### 接口信息
- 接口名称：startDailySchedule
- 接口URL：/changjiang/elearn/startDailySchedule
- 请求方式：POST
- 接口用途：获取当天需要学习的单词内容

### 请求参数 (DailyStudyDTO)
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | String | 是 | 用户ID |
| planId | String | 是 | 学习计划ID |

### 返回参数 (WordDTO)
| 参数名 | 类型 | 说明 |
|--------|------|------|
| wordId | String | 单词ID |
| word | String | 单词 |
| pronunciation | String | 音标 |
| wordTranslation | String | 单词翻译 |
| sentence | String | 例句 |
| sentenceTranslation | String | 例句翻译 |

### 业务流程
1. 获取学习计划
2. 验证学习进度
3. 获取当天学习内容
4. 更新学习进度
5. 返回单词信息

## 5. 查询学习计划接口
### 接口信息
- 接口名称：queryStudyPlan
- 接口URL：/changjiang/elearn/queryStudyPlan
- 请求方式：GET
- 接口用途：查询用户的所有学习计划

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | String | 是 | 用户ID |

### 返回参数 (List<StudyPlanDTO>)
| 参数名 | 类型 | 说明 |
|--------|------|------|
| planId | String | 计划ID |
| userId | String | 用户ID |
| dailyWords | Integer | 每日单词数 |
| totalDays | Integer | 总天数 |
| currentDay | Integer | 当前天数 |
| status | Integer | 状态(0-进行中,1-已完成) |

## 错误码说明
| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 1 | 业务错误 |
| 2 | 系统错误 |

## 注意事项
1. 所有接口支持PC端和移动端访问
2. 文件上传大小限制为10MB
3. 单次最多上传10个文件
4. 音频文件格式为MP3
5. 所有时间格式均为ISO-8601标准 