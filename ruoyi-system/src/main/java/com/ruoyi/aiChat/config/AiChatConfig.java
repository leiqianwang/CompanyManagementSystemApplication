package com.ruoyi.aiChat.config;

import com.ruoyi.aiChat.service.McpFunctionService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI Chat Configuration with MCP Function Calling
 *
 * @author ruoyi
 */
@Configuration
public class AiChatConfig {

    @Value("${ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${ai.openai.base-url:https://api.openai.com/v1}")
    private String openAiBaseUrl;

    @Value("${ai.openai.model:gpt-3.5-turbo}")
    private String openAiModel;

    @Autowired
    private McpFunctionService mcpFunctionService;

    /**
     * 配置支持函数调用的ChatLanguageModel
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .baseUrl(openAiBaseUrl)
                .modelName(openAiModel)
                .temperature(0.7)
                .maxTokens(2000)
                .build();
    }

    /**
     * AI服务接口定义
     */
    public interface AiChatAssistant {
        @SystemMessage("你是一个有用的AI助手，可以调用各种工具函数来帮助用户完成任务。" +
                      "当用户需要时间、计算、天气、翻译等信息时，请使用相应的工具函数。" +
                      "请用中文回答用户的问题。")
        String chat(String userMessage);
    }

    /**
     * 配置带有函数调用功能的AI助手
     */
    @Bean
    public AiChatAssistant aiChatAssistant() {
        return AiServices.builder(AiChatAssistant.class)
                .chatLanguageModel(chatLanguageModel())
                .tools(mcpFunctionService)
                .build();
    }
}
