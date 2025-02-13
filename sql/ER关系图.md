```Mermaid
erDiagram
    t_user ||--o{ t_document : "创建"
    t_user ||--o{ t_study_plan : "创建"
    t_user ||--o{ t_daily_study : "学习"
    t_document ||--o{ t_word : "包含"
    t_document ||--o{ t_study_plan : "关联"
    t_study_plan ||--o{ t_daily_study : "记录"

    t_user {
        varchar(32) id PK "用户ID"
        varchar(50) username "用户名"
        varchar(128) password "密码"
        varchar(100) email "邮箱"
        varchar(20) phone "手机号"
        tinyint status "状态"
        datetime last_login_time "最后登录时间"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    t_document {
        varchar(32) id PK "文档ID"
        varchar(32) user_id FK "用户ID"
        varchar(200) file_name "文件名"
        varchar(500) file_path "文件路径"
        bigint file_size "文件大小"
        int word_count "单词数量"
        tinyint status "状态"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    t_study_plan {
        varchar(32) id PK "计划ID"
        varchar(32) user_id FK "用户ID"
        varchar(32) document_id FK "文档ID"
        int daily_words "每日单词数"
        int total_days "总天数"
        int current_day "当前天数"
        tinyint status "状态"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    t_word {
        varchar(32) id PK "单词ID"
        varchar(32) document_id FK "文档ID"
        varchar(100) word "单词"
        varchar(100) pronunciation "音标"
        varchar(500) translation "翻译"
        varchar(500) audio_path "音频路径"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }

    t_daily_study {
        varchar(32) id PK "记录ID"
        varchar(32) plan_id FK "计划ID"
        varchar(32) user_id FK "用户ID"
        int study_day "学习天数"
        int word_count "单词数量"
        tinyint status "状态"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }
```

关系说明：
用户(t_user)与文档(t_document)：一对多关系
一个用户可以上传多个文档
每个文档只属于一个用户
文档(t_document)与单词(t_word)：一对多关系
一个文档包含多个单词
每个单词只属于一个文档
用户(t_user)与学习计划(t_study_plan)：一对多关系
一个用户可以创建多个学习计划
每个学习计划只属于一个用户
文档(t_document)与学习计划(t_study_plan)：一对多关系
一个文档可以关联多个学习计划
每个学习计划只关联一个文档
学习计划(t_study_plan)与每日学习记录(t_daily_study)：一对多关系
一个学习计划包含多个每日学习记录
每个每日学习记录只属于一个学习计划
用户(t_user)与每日学习记录(t_daily_study)：一对多关系
一个用户可以有多个每日学习记录
每个每日学习记录只属于一个用户
这个ER图清晰地展示了各个表之间的关系，以及每个表的主要字段。通过这个图可以直观地理解整个数据库的结构和业务逻辑。