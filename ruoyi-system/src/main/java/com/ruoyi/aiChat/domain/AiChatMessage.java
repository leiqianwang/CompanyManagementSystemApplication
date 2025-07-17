package com.ruoyi.aiChat.domain;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class AiChatMessage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long sessionId;
  private Long userId;
  private String username;
  private String content;
  private LocalDateTime timestamp;

  @Enumerated(EnumType.STRING)
  private MessageRole role;

  // 默认构造函数
  public AiChatMessage() {}

  public AiChatMessage(Long id, Long sessionId, Long userId, String username, String content, LocalDateTime timestamp, MessageRole role) {
    this.id = id;
    this.sessionId = sessionId;
    this.userId = userId;
    this.username = username;
    this.content = content;
    this.timestamp = timestamp;
    this.role = role;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSessionId() {
    return sessionId;
  }

  public void setSessionId(Long sessionId) {
    this.sessionId = sessionId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public MessageRole getRole() {
    return role;
  }

  public void setRole(MessageRole role) {
    this.role = role;
  }
}
