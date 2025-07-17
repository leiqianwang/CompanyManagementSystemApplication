package com.ruoyi.aiChat.service;

import com.ruoyi.aiChat.dto.ChatMessageRequest;
import com.ruoyi.aiChat.dto.ChatMessageResponse;
import com.ruoyi.aiChat.dto.ChatSessionDto;
import com.ruoyi.aiChat.config.AiChatConfig;
import com.ruoyi.aiChat.domain.AiChatMessage;
import com.ruoyi.aiChat.domain.ChatSessionEntity;
import com.ruoyi.aiChat.domain.MessageRole;
import com.ruoyi.aiChat.repository.AiChatMessageRepository;
import com.ruoyi.aiChat.repository.AiChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * AI 聊天服务 - 使用数据库持久化
 *
 * @author ruoyi
 * @date 2025-07-14
 */
@Service
public class AiChatService {

    @Autowired
    private AiChatConfig.AiChatAssistant aiChatAssistant;

    @Autowired
    private AiChatHistoryService aiChatHistoryService;

    @Autowired
    private AiChatSessionRepository aiChatSessionRepository;

    @Autowired
    private AiChatMessageRepository aiChatMessageRepository;

    /**
     * 发送聊天消息 - 使用数据库持久化
     */
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        try {
            String sessionId = request.getSessionId();
            if (sessionId == null || sessionId.trim().isEmpty()) {
                sessionId = generateSessionId();
            }

            // 获取或创建会话
            ChatSessionEntity session = getOrCreateSession(sessionId, "新对话");

            // 保存用户消息到数据库
            AiChatMessage userMessage = new AiChatMessage();
            userMessage.setSessionId(sessionId);
            userMessage.setContent(request.getMessage());
            userMessage.setRole(MessageRole.USER);
            userMessage.setTimestamp(LocalDateTime.now());
            aiChatHistoryService.saveChatMessage(userMessage);

            // 使用AI助手生成回复
            String aiResponse = aiChatAssistant.chat(request.getMessage());

            // 保存AI回复到数据库
            AiChatMessage aiMessage = new AiChatMessage();
            aiMessage.setSessionId(sessionId);
            aiMessage.setContent(aiResponse);
            aiMessage.setRole(MessageRole.AI);
            aiMessage.setTimestamp(LocalDateTime.now());
            aiChatHistoryService.saveChatMessage(aiMessage);

            // 更新会话信息
            List<AiChatMessage> allMessages = aiChatMessageRepository.findBySessionId(sessionId);
            session.setLastActivity(LocalDateTime.now());
            session.setMessageCount(allMessages.size());
            session.setLastMessageContent(aiResponse.length() > 50 ?
                aiResponse.substring(0, 50) + "..." : aiResponse);
            aiChatSessionRepository.save(session);

            // 构建响应
            ChatMessageResponse response = new ChatMessageResponse();
            response.setMessageId(UUID.randomUUID().toString());
            response.setSessionId(sessionId);
            response.setContent(aiResponse);
            response.setMessageType("assistant");
            response.setCreateTime(LocalDateTime.now());
            response.setFinished(true);

            return response;

        } catch (Exception e) {
            // 错误处理
            ChatMessageResponse errorResponse = new ChatMessageResponse();
            errorResponse.setSessionId(request.getSessionId());
            errorResponse.setContent("抱歉，处理您的请求时出现了错误：" + e.getMessage());
            errorResponse.setMessageType("assistant");
            errorResponse.setError(e.getMessage());
            errorResponse.setFinished(true);
            return errorResponse;
        }
    }

    /**
     * 获取会���列表 - 从数据库查询
     */
    public List<ChatSessionDto> getSessions(Long userId) {
        List<ChatSessionEntity> sessionEntities = aiChatSessionRepository.findAll();
        List<ChatSessionDto> sessionList = new ArrayList<>();

        for (ChatSessionEntity entity : sessionEntities) {
            if (userId == null || Objects.equals(entity.getUserId(), userId.toString())) {
                ChatSessionDto dto = convertToDto(entity);
                sessionList.add(dto);
            }
        }

        sessionList.sort((s1, s2) -> s2.getUpdateTime().compareTo(s1.getUpdateTime()));
        return sessionList;
    }

    /**
     * 获取会话详情 - 从数据库查询
     */
    public ChatSessionDto getSession(String sessionId) {
        ChatSessionEntity sessionEntity = aiChatSessionRepository.findBySessionId(sessionId);
        if (sessionEntity != null) {
            ChatSessionDto sessionDto = convertToDto(sessionEntity);

            // 获取消息历史并转换为响应格式
            List<AiChatMessage> dbMessages = aiChatMessageRepository.findBySessionId(sessionId);
            List<ChatMessageResponse> messages = new ArrayList<>();

            for (AiChatMessage message : dbMessages) {
                // 跳过系统消息
                if (MessageRole.SYSTEM.equals(message.getRole())) {
                    continue;
                }

                ChatMessageResponse messageResponse = new ChatMessageResponse();
                messageResponse.setMessageId(UUID.randomUUID().toString());
                messageResponse.setSessionId(sessionId);
                messageResponse.setContent(message.getContent());
                messageResponse.setMessageType(MessageRole.USER.equals(message.getRole()) ? "user" : "assistant");
                messageResponse.setCreateTime(message.getTimestamp());
                messageResponse.setFinished(true);
                messages.add(messageResponse);
            }

            sessionDto.setMessages(messages);
            return sessionDto;
        }
        return null;
    }

    /**
     * 创建新会话 - 保存到数据库
     */
    public ChatSessionDto createSession(String title, Long userId, String username) {
        String sessionId = generateSessionId();

        ChatSessionEntity entity = new ChatSessionEntity();
        entity.setSessionId(sessionId);
        entity.setTitle(title);
        entity.setUserId(userId != null ? userId.toString() : null);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastActivity(LocalDateTime.now());
        entity.setIsActive(true);
        entity.setMessageCount(0);
        entity.setLastMessageContent("");

        ChatSessionEntity savedEntity = aiChatSessionRepository.save(entity);
        return convertToDto(savedEntity);
    }

    /**
     * 删除会话 - 从数据库删除
     */
    public boolean deleteSession(String sessionId) {
        ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
        if (session != null) {
            // 删除会话的所有消息
            aiChatHistoryService.deleteChatHistory(sessionId);
            // 删除会话
            aiChatSessionRepository.delete(session);
            return true;
        }
        return false;
    }

    /**
     * 清空会话历史 - 委托给AiChatHistoryService
     */
    public boolean clearSessionHistory(String sessionId) {
        return aiChatHistoryService.clearSessionHistory(sessionId);
    }

    /**
     * 获取或创建会话
     */
    private ChatSessionEntity getOrCreateSession(String sessionId, String title) {
        ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
        if (session == null) {
            session = new ChatSessionEntity();
            session.setSessionId(Long.parseLong(sessionId));
            session.setTitle(title);
            session.setCreatedAt(LocalDateTime.now());
            session.setLastActivity(LocalDateTime.now());
            session.setIsActive(true);
            session.setMessageCount(0);
            session.setLastMessageContent("");
            session = aiChatSessionRepository.saveAndFlush(session);
        }
        return session;
    }

    /**
     * 实体转DTO
     */
    private ChatSessionDto convertToDto(ChatSessionEntity entity) {
        ChatSessionDto dto = new ChatSessionDto(entity.getSessionId(), entity.getTitle());
        dto.setUserId(entity.getUserId() != null ? Long.parseLong(entity.getUserId()) : null);
        dto.setCreateTime(entity.getCreatedAt());
        dto.setUpdateTime(entity.getLastActivity());
        dto.setMessageCount(entity.getMessageCount());
        dto.setLastMessageContent(entity.getLastMessageContent());
        return dto;
    }

    /**
     * 生成会话ID
     */
    private String generateSessionId() {
        return "chat_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
