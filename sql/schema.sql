-- 用户表
CREATE TABLE `t_user` (
  `id` varchar(32) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码(加密)',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文档表
CREATE TABLE `t_document` (
  `id` varchar(32) NOT NULL COMMENT '文档ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `file_name` varchar(200) NOT NULL COMMENT '文件名',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `word_count` int NOT NULL DEFAULT '0' COMMENT '单词数量',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-待处理,1-处理中,2-处理完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档表';

-- 学习计划表
CREATE TABLE `t_study_plan` (
  `id` varchar(32) NOT NULL COMMENT '计划ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `document_id` varchar(32) NOT NULL COMMENT '文档ID',
  `daily_words` int NOT NULL COMMENT '每日单词数',
  `total_days` int NOT NULL COMMENT '总天数',
  `current_day` int NOT NULL DEFAULT '1' COMMENT '当前天数',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-进行中,1-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- 单词表
CREATE TABLE `t_word` (
  `id` varchar(32) NOT NULL COMMENT '单词ID',
  `document_id` varchar(32) NOT NULL COMMENT '文档ID',
  `word` varchar(100) NOT NULL COMMENT '单词',
  `pronunciation` varchar(100) DEFAULT NULL COMMENT '音标',
  `translation` varchar(500) DEFAULT NULL COMMENT '翻译',
  `audio_path` varchar(500) DEFAULT NULL COMMENT '音频文件路径',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_document_id` (`document_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单词表';

-- 每日学习记录表
CREATE TABLE `t_daily_study` (
  `id` varchar(32) NOT NULL COMMENT '记录ID',
  `plan_id` varchar(32) NOT NULL COMMENT '计划ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID', 
  `study_day` int NOT NULL COMMENT '学习天数',
  `word_count` int NOT NULL DEFAULT '0' COMMENT '单词数量',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-未完成,1-已完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日学习记录表'; 