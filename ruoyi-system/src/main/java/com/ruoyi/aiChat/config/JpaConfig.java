package com.ruoyi.aiChat.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA配置类 - 确保JPA正确初始化
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ruoyi.aiChat.repository")
@EntityScan(basePackages = "com.ruoyi.aiChat.domain")
public class JpaConfig {
    // 配置类无需额外代码，注解已完成配置工作
}
