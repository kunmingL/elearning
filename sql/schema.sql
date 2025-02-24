-- 用户表
CREATE TABLE `t_user` (
                          `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                          `username` varchar(50) NOT NULL COMMENT '用户名',
                          `password` varchar(128) NOT NULL COMMENT '密码(加密)',
                          `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                          `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                          `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
                          `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`user_id`),
                          UNIQUE KEY `uk_username` (`username`),
                          UNIQUE KEY `uk_email` (`email`),
                          UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文档表
CREATE TABLE `t_document` (
                              `doc_id` varchar(32) NOT NULL COMMENT '文档ID',
                              `plan_id` varchar(32) NOT NULL COMMENT '学习计划ID',
                              `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                              `file_name` varchar(200) NOT NULL COMMENT '文件名',
                              `file_path` varchar(500) NOT NULL COMMENT '文件路径',
                              `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
                              `word_count` int NOT NULL DEFAULT '0' COMMENT '单词数量',
                              `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-待处理,1-处理中,2-处理完成',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`doc_id`),
                              KEY `idx_user_id` (`user_id`),
                              KEY `idx_plan_id` (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档表';

-- 学习计划表
CREATE TABLE `t_study_plan` (
                                `plan_id` varchar(32) NOT NULL COMMENT '学习计划ID',
                                `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                                `daily_words` int NOT NULL COMMENT '每日单词数',
                                `total_days` int NOT NULL COMMENT '总天数',
                                `current_day` int NOT NULL DEFAULT '1' COMMENT '当前天数',
                                `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-进行中,1-已完成',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`plan_id`),
                                KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- 单词表
CREATE TABLE `t_word` (
                          `word_id` varchar(32) NOT NULL COMMENT '单词ID',
                          `plan_id` varchar(32) NOT NULL COMMENT '学习计划ID',
                          `daily_id` varchar(32) NOT NULL COMMENT 'dailyID',
                          `word` varchar(100) NOT NULL COMMENT '单词',
                          `pronunciation` varchar(100) DEFAULT NULL COMMENT '音标',
                          `word_translation` varchar(500) DEFAULT NULL COMMENT '翻译',
                          `sentence` varchar(500) DEFAULT NULL COMMENT '例句',
                          `sentence_translation` varchar(500) DEFAULT NULL COMMENT '例句翻译',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`word_id`),
                          KEY `idx_plan_id` (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';

-- 每日学习记录表
CREATE TABLE `t_daily_study` (
                                 `daily_id` varchar(32) NOT NULL COMMENT 'dailyID',
                                 `plan_id` varchar(32) NOT NULL COMMENT '计划ID',
                                 `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                                 `study_day` int NOT NULL COMMENT '学习天数',
                                 `word_count` int NOT NULL DEFAULT '0' COMMENT '单词数量',
                                 `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-未完成,1-已完成',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`daily_id`),
                                 KEY `idx_plan_id` (`plan_id`),
                                 KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日学习记录表';

-- 对话表
CREATE TABLE `t_conversation` (
                                  `conversation_id` varchar(32) NOT NULL COMMENT '对话ID',
                                  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
                                  `title` varchar(100) NOT NULL COMMENT '对话标题',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`conversation_id`),
                                  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话表';

-- 对话历史记录表
CREATE TABLE `t_conversation_history` (
                                          `history_id` varchar(32) NOT NULL COMMENT '历史记录ID',
                                          `conversation_id` varchar(32) NOT NULL COMMENT '对话ID',
                                          `role` varchar(20) NOT NULL COMMENT '角色:user-用户,assistant-AI助手',
                                          `content` text NOT NULL COMMENT '内容',
                                          `sequence` int NOT NULL COMMENT '顺序号',
                                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          PRIMARY KEY (`history_id`),
                                          KEY `idx_conversation_id` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话历史记录表';