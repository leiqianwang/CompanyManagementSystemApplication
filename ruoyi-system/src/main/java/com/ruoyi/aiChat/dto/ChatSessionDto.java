package com.ruoyi.aiChat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 聊天会话 DTO
 *
 * @author ruoyi
 * @date 2025-07-03
 */
public class ChatSessionDto {

    /** 会话ID */
    private Long sessionId;

    /** 会话标题 */
    private String title;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 消息数量 */
    private Integer messageCount = 0;

    /** 会话状态：active/archived */
    private String status = "active";

    /** 最后一条消息内容（预览用） */
    private String lastMessageContent;

    /** 消息列表 */
    private List<ChatMessageResponse> messages;

    /** 创建会话时的初始消息内容（可选） */
    private String initialMessage;

    public ChatSessionDto() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public ChatSessionDto(Long sessionId, String title) {
        this();
        this.sessionId = sessionId;
        this.title = title;
    }

    // getters and setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public List<ChatMessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageResponse> messages) {
        this.messages = messages;
    }

    public String getInitialMessage() {
        return initialMessage;
    }

    public void setInitialMessage(String initialMessage) {
        this.initialMessage = initialMessage;
    }

    @Override
    public String toString() {
        return "ChatSessionDto{" +
                "sessionId='" + sessionId + '\'' +
                ", title='" + title + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", messageCount=" + messageCount +
                ", status='" + status + '\'' +
                ", lastMessageContent='" + lastMessageContent + '\'' +
                '}';
    }
}
