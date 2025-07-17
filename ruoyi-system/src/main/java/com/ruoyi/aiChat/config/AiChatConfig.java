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

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * AI Chat Configuration with Multiple AI Models Support
 *
 * @author ruoyi
 */
@Configuration
public class AiChatConfig {

    // OpenAI 配置
    @Value("${ai.openai.api-key:}")
    private String openAiApiKey;

    @Value("${ai.openai.base-url:https://api.openai.com/v1}")
    private String openAiBaseUrl;

    @Value("${ai.openai.model:gpt-3.5-turbo}")
    private String openAiModel;

    @Value("${ai.openai.timeout:30000}")
    private Integer openAiTimeout;

    // Ollama 配置
    @Value("${ai.ollama.api-key:ollama}")
    private String ollamaApiKey;

    @Value("${ai.ollama.base-url:http://localhost:11434/v1}")
    private String ollamaBaseUrl;

    @Value("${ai.ollama.model:llama3.1:8b}")
    private String ollamaModel;

    @Value("${ai.ollama.timeout:60000}")
    private Integer ollamaTimeout;

    // 百度文心一言配置
    @Value("${ai.baidu.api-key:}")
    private String baiduApiKey;

    @Value("${ai.baidu.secret-key:}")
    private String baiduSecretKey;

    @Value("${ai.baidu.base-url:https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat}")
    private String baiduBaseUrl;

    @Value("${ai.baidu.model:ernie-3.5-turbo}")
    private String baiduModel;

    @Value("${ai.baidu.timeout:30000}")
    private Integer baiduTimeout;

    // 百度千帆配置
    @Value("${ai.qianfan.api-key:}")
    private String qianfanApiKey;

    @Value("${ai.qianfan.secret-key:}")
    private String qianfanSecretKey;

    @Value("${ai.qianfan.base-url:https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat}")
    private String qianfanBaseUrl;

    @Value("${ai.qianfan.model:eb-instant}")
    private String qianfanModel;

    @Value("${ai.qianfan.timeout:30000}")
    private Integer qianfanTimeout;

    // 阿里千问配置
    @Value("${ai.alibaba.api-key:}")
    private String alibabaApiKey;

    @Value("${ai.alibaba.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String alibabaBaseUrl;

    @Value("${ai.alibaba.model:qwen-turbo}")
    private String alibabaModel;

    @Value("${ai.alibaba.timeout:30000}")
    private Integer alibabaTimeout;

    // ���谱清言配置
    @Value("${ai.zhipu.api-key:}")
    private String zhipuApiKey;

    @Value("${ai.zhipu.base-url:https://open.bigmodel.cn/api/paas/v4}")
    private String zhipuBaseUrl;

    @Value("${ai.zhipu.model:glm-4}")
    private String zhipuModel;

    @Value("${ai.zhipu.timeout:30000}")
    private Integer zhipuTimeout;

    // 月之暗面 Kimi 配置
    @Value("${ai.moonshot.api-key:}")
    private String moonshotApiKey;

    @Value("${ai.moonshot.base-url:https://api.moonshot.cn/v1}")
    private String moonshotBaseUrl;

    @Value("${ai.moonshot.model:moonshot-v1-8k}")
    private String moonshotModel;

    @Value("${ai.moonshot.timeout:30000}")
    private Integer moonshotTimeout;

    @Autowired
    private McpFunctionService mcpFunctionService;

    /**
     * 创建多个 ChatLanguageModel 实例的工厂方法
     */
    @Bean
    public Map<String, ChatLanguageModel> chatLanguageModels() {
        Map<String, ChatLanguageModel> models = new HashMap<>();

        // OpenAI 模型
        if (openAiApiKey != null && !openAiApiKey.isEmpty()) {
            models.put("OpenAI", OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .baseUrl(openAiBaseUrl)
                    .modelName(openAiModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(openAiTimeout))
                    .build());
        }

        // Ollama 模型
        if (ollamaApiKey != null && !ollamaApiKey.isEmpty()) {
            models.put("Ollama", OpenAiChatModel.builder()
                    .apiKey(ollamaApiKey)
                    .baseUrl(ollamaBaseUrl)
                    .modelName(ollamaModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(ollamaTimeout))
                    .build());
        }

        // 阿里千问模型 (使用 OpenAI 兼容格式)
        if (alibabaApiKey != null && !alibabaApiKey.isEmpty()) {
            models.put("Alibaba", OpenAiChatModel.builder()
                    .apiKey(alibabaApiKey)
                    .baseUrl(alibabaBaseUrl)
                    .modelName(alibabaModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(alibabaTimeout))
                    .build());
        }

        // 智谱清言模型
        if (zhipuApiKey != null && !zhipuApiKey.isEmpty()) {
            models.put("Zhipu", OpenAiChatModel.builder()
                    .apiKey(zhipuApiKey)
                    .baseUrl(zhipuBaseUrl)
                    .modelName(zhipuModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(zhipuTimeout))
                    .build());
        }

        // 月之暗面 Kimi 模型
        if (moonshotApiKey != null && !moonshotApiKey.isEmpty()) {
            models.put("Moonshot", OpenAiChatModel.builder()
                    .apiKey(moonshotApiKey)
                    .baseUrl(moonshotBaseUrl)
                    .modelName(moonshotModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(moonshotTimeout))
                    .build());
        }

        // 百度文心一言模型
        if (baiduApiKey != null && !baiduApiKey.isEmpty()) {
            models.put("Baidu", OpenAiChatModel.builder()
                    .apiKey(baiduApiKey)
                    .baseUrl(baiduBaseUrl)
                    .modelName(baiduModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(baiduTimeout))
                    .build());
        }

        // 百度千帆模型
        if (qianfanApiKey != null && !qianfanApiKey.isEmpty()) {
            models.put("Qianfan", OpenAiChatModel.builder()
                    .apiKey(qianfanApiKey)
                    .baseUrl(qianfanBaseUrl)
                    .modelName(qianfanModel)
                    .temperature(0.7)
                    .maxTokens(2000)
                    .timeout(Duration.ofMillis(qianfanTimeout))
                    .build());
        }

        return models;
    }

    /**
     * 默认的 ChatLanguageModel - 优先使用 Ollama 本地模型
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        Map<String, ChatLanguageModel> models = chatLanguageModels();

        // 第一优先级：Ollama 本地模型
        if (models.containsKey("Ollama")) {
            return models.get("Ollama");
        }

        // 第二优先级：百度千帆
        if (models.containsKey("Qianfan")) {
            return models.get("Qianfan");
        }

        // 第三优先级：百度文心一言
        if (models.containsKey("Baidu")) {
            return models.get("Baidu");
        }

        // 第四优先级：阿里千问
        if (models.containsKey("Alibaba")) {
            return models.get("Alibaba");
        }

        // 第五优先级：智谱清言
        if (models.containsKey("Zhipu")) {
            return models.get("Zhipu");
        }

        // 第六优先级：月之暗面
        if (models.containsKey("Moonshot")) {
            return models.get("Moonshot");
        }

        // 第七优先级：OpenAI
        if (models.containsKey("OpenAI")) {
            return models.get("OpenAI");
        }

        // 如果都没有配置，使用 Ollama 作为默认
        return OpenAiChatModel.builder()
                .apiKey("ollama")
                .baseUrl("http://localhost:11434/v1")
                .modelName("llama3.1:8b")
                .temperature(0.7)
                .maxTokens(2000)
                .build();
    }

    /**
     * 根据模型类型获取对应的 ChatLanguageModel
     */
    public ChatLanguageModel getModelByType(String modelType) {
        Map<String, ChatLanguageModel> models = chatLanguageModels();
        return models.getOrDefault(modelType, chatLanguageModel());
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

    /**
     * 创建特定类型的 AI 助手
     */
    public AiChatAssistant createAiAssistantForModel(String modelType) {
        ChatLanguageModel model = getModelByType(modelType);
        return AiServices.builder(AiChatAssistant.class)
                .chatLanguageModel(model)
                .tools(mcpFunctionService)
                .build();
    }
}
