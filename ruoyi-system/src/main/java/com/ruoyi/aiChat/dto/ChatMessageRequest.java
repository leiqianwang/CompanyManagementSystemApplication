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
  private Long sessionId;

  /** 是否流式响应 */
  private Boolean stream = false;

  /** 模型ID */
  private String modelId;

  /** 温度参数 */
  private Double temperature;

  /** 最大令牌数 */
  private Integer maxTokens;

  public ChatMessageRequest() {}

  public ChatMessageRequest(String message, Long sessionId) {
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

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
  }

  public String getModelId() {
    return modelId;
  }

  public void setModelId(String modelId) {
    this.modelId = modelId;
  }

  public Boolean getStream() {
    return stream;
  }

  public void setStream(Boolean stream) {
    this.stream = stream;
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
      ", sessionId=" + sessionId +
      ", modelId='" + modelId + '\'' +
      ", stream=" + stream +
      ", temperature=" + temperature +
      ", maxTokens=" + maxTokens +
      '}';
  }
}
