package com.ruoyi.aiChat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 聊天消息请求 DTO
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public class ChatMessageRequest {
    
    /** 消息内容 */
    private String message;
    
    /** 会话ID */
    private String sessionId;
    
    /** 是否流式响应 */
    private Boolean stream = false;
    
    /** 模型名称 */
    private String model;
    
    /** 温度参数 */
    private Double temperature;
    
    /** 最大令牌数 */
    private Integer maxTokens;

    public ChatMessageRequest() {}

    public ChatMessageRequest(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    // getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    @Override
    public String toString() {
        return "ChatMessageRequest{" +
                "message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", stream=" + stream +
                ", model='" + model + '\'' +
                ", temperature=" + temperature +
                ", maxTokens=" + maxTokens +
                '}';
    }
}
