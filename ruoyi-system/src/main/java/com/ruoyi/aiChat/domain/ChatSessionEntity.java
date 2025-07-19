package com.ruoyi.aiChat.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_chat_session")
public class ChatSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String userId;

    private String title;

    private Integer messageCount;

  public void setUsername(String username) {
    this.username = username;
  }



  public void setActive(Boolean active) {
    isActive = active;
  }

  private String username;

    private String lastMessageContent;

    private LocalDateTime createdAt;

    private LocalDateTime lastActivity;

    private Boolean isActive;

    // getter/setter
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


  public String getUsername() {
      return username;
  }

  public Integer getMessageCount() { return messageCount; }
    public void setMessageCount(Integer messageCount) { this.messageCount = messageCount; }

    public String getLastMessageContent() { return lastMessageContent; }
    public void setLastMessageContent(String lastMessageContent) { this.lastMessageContent = lastMessageContent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastActivity() { return lastActivity; }
    public void setLastActivity(LocalDateTime lastActivity) { this.lastActivity = lastActivity; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
