# 英语学习助手接口文档

## 1. 英语语音合成接口
### 接口信息
- 接口名称：spokenEnglish
- 接口URL：/changjiang/elearn/spokenEnglish
- 请求方式：POST
- 接口用途：将文本转换为英语语音

### 请求参数
| 参数名 | 类型   | 必填 | 说明     |
|--------|--------|------|----------|
| text   | String | 是   | 英文文本 |

### 返回参数 (FileObject)
| 参数名      | 类型   | 说明         |
|-------------|--------|--------------|
| fileName    | String | 音频文件名   |
| fileContent | byte[] | 音频文件内容 |

## 2. 文件处理接口
### 接口信息
- 接口名称：dealInputFile
- 接口URL：/changjiang/elearn/dealInputFile
- 请求方式：POST
- 接口用途：处理上传的文件，提取单词总数
- 支持格式：pdf, doc, docx, ppt, pptx, xls, xlsx, png, jpg

### 请求参数 (List<FileObject>)
| 参数名      | 类型   | 必填 | 说明         |
|-------------|--------|------|--------------|
| fileName    | String | 是   | 文件名       |
| fileContent | byte[] | 是   | 文件内容     |

### 返回参数 (CommonRespDataDto)
| 参数名    | 类型   | 说明                     |
|-----------|--------|--------------------------|
| code      | String | 响应码：0-成功，其他-失败 |
| message   | String | 响应信息                 |
| data      | String | 提取的单词总数           |

## 3. 创建学习计划接口
### 接口信息
- 接口名称：createUserSchedule
- 接口URL：/changjiang/elearn/createUserSchedule
- 请求方式：POST
- 接口用途：根据用户设置生成学习计划

### 请求参数 (UserScheduleDto)
| 参数名      | 类型   | 必填 | 说明         |
|-------------|--------|------|--------------|
| userId      | String | 是   | 用户ID       |
| dailyWords  | Integer| 是   | 每日学习单词数|
| countWords  | Integer| 是   | 总单词数     |
| studyModel  | String | 是   | 学习模式     |

### 返回参数 (CommonRespDataDto)
| 参数名    | 类型   | 说明                     |
|-----------|--------|--------------------------|
| code      | String | 响应码：0-成功，其他-失败 |
| message   | String | 响应信息                 |
| data      | Object | 学习计划信息             |

## 4. 获取每日学习内容接口
### 接口信息
- 接口名称：startDailySchedule
- 接口URL：/changjiang/elearn/startDailySchedule
- 请求方式：POST
- 接口用途：获取指定日期的学习内容

### 请求参数 (DailyWordsDto)
| 参数名     | 类型    | 必填 | 说明         |
|------------|---------|------|--------------|
| userId     | String  | 是   | 用户ID       |
| scheduleId | String  | 是   | 学习计划ID   |
| studyDay   | Integer | 是   | 学习天数     |
| wordIndex  | Integer | 是   | 单词索引     |

### 返回参数 (WordDto)
| 参数名 | 类型       | 说明         |
|--------|------------|--------------|
| word   | FileObject | 单词音频文件 |

### FileObject 结构
| 参数名      | 类型   | 说明         |
|-------------|--------|--------------|
| fileName    | String | 音频文件名   |
| fileContent | byte[] | 音频文件内容 |

## 错误码说明
| 错误码 | 说明           |
|--------|----------------|
| 0      | 成功           |
| 1      | 业务错误       |
| 2      | 系统错误       |

## 注意事项
1. 所有接口支持PC端和移动端访问
2. 文件上传大小限制请参考系统配置
3. 音频文件格式为MP3
4. 所有时间格式均为ISO-8601标准 