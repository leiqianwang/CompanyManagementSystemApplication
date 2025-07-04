package com.ruoyi.aiChat.service;

import com.ruoyi.aiChat.dto.ChatMessageRequest;
import com.ruoyi.aiChat.dto.ChatMessageResponse;
import com.ruoyi.aiChat.dto.ChatSessionDto;
import com.ruoyi.aiChat.config.AiChatConfig;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI 聊天服务
 *
 * @author ruoyi
 * @date 2025-07-03
 */
@Service
public class AiChatService {

    @Autowired
    private AiChatConfig.AiChatAssistant aiChatAssistant;

    // 内存存储聊天会话和消息历史 (生产环境应使用数据库)
    private final Map<String, ChatSessionDto> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> conversationHistory = new ConcurrentHashMap<>();

    /**
     * 发送聊天消息
     */
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        try {
            String sessionId = request.getSessionId();
            if (sessionId == null || sessionId.trim().isEmpty()) {
                sessionId = generateSessionId();
            }

            // 获取或创建会话
            ChatSessionDto session = getOrCreateSession(sessionId, "新对话");

            // 获取会话历史
            List<ChatMessage> history = conversationHistory.getOrDefault(sessionId, new ArrayList<>());

            // 添加用户消息到历史
            UserMessage userMessage = UserMessage.from(request.getMessage());
            history.add(userMessage);

            // 使用带有函数调用功能的AI助手生成回复
            String aiResponse = aiChatAssistant.chat(request.getMessage());

            // 添加AI回复到历史
            AiMessage aiMessage = AiMessage.from(aiResponse);
            history.add(aiMessage);

            // 更新会话历史
            conversationHistory.put(sessionId, history);

            // 更新会话信息
            session.setUpdateTime(LocalDateTime.now());
            session.setMessageCount(history.size());
            session.setLastMessageContent(aiResponse.length() > 50 ?
                aiResponse.substring(0, 50) + "..." : aiResponse);

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
     * 获取会话列表
     */
    public List<ChatSessionDto> getSessions(Long userId) {
        List<ChatSessionDto> sessionList = new ArrayList<>();
        for (ChatSessionDto session : sessions.values()) {
            if (userId == null || Objects.equals(session.getUserId(), userId)) {
                sessionList.add(session);
            }
        }
        sessionList.sort((s1, s2) -> s2.getUpdateTime().compareTo(s1.getUpdateTime()));
        return sessionList;
    }

    /**
     * 获取会话详情
     */
    public ChatSessionDto getSession(String sessionId) {
        ChatSessionDto session = sessions.get(sessionId);
        if (session != null) {
            // 获取消息历史并转换为响应格式
            List<ChatMessage> history = conversationHistory.getOrDefault(sessionId, new ArrayList<>());
            List<ChatMessageResponse> messages = new ArrayList<>();

            for (ChatMessage message : history) {
                // 跳过系统消息
                if (message instanceof SystemMessage) {
                    continue;
                }

                ChatMessageResponse messageResponse = new ChatMessageResponse();
                messageResponse.setMessageId(UUID.randomUUID().toString());
                messageResponse.setSessionId(sessionId);

                // Fix deprecated text() method
                String content = "";
                if (message instanceof UserMessage) {
                    content = ((UserMessage) message).singleText();
                } else if (message instanceof AiMessage) {
                    content = ((AiMessage) message).text();
                }

                messageResponse.setContent(content);
                messageResponse.setMessageType(message instanceof UserMessage ? "user" : "assistant");
                messageResponse.setCreateTime(LocalDateTime.now());
                messageResponse.setFinished(true);
                messages.add(messageResponse);
            }

            session.setMessages(messages);
        }
        return session;
    }

    /**
     * 创建新会话
     */
    public ChatSessionDto createSession(String title, Long userId, String username) {
        String sessionId = generateSessionId();
        ChatSessionDto session = new ChatSessionDto(sessionId, title);
        session.setUserId(userId);
        session.setUsername(username);
        sessions.put(sessionId, session);
        return session;
    }

    /**
     * 删除会话
     */
    public boolean deleteSession(String sessionId) {
        ChatSessionDto removed = sessions.remove(sessionId);
        conversationHistory.remove(sessionId);
        return removed != null;
    }

    /**
     * 清空会话历史
     */
    public boolean clearSessionHistory(String sessionId) {
        if (sessions.containsKey(sessionId)) {
            conversationHistory.remove(sessionId);
            ChatSessionDto session = sessions.get(sessionId);
            session.setMessageCount(0);
            session.setLastMessageContent("");
            return true;
        }
        return false;
    }

    /**
     * 获取或创建会话
     */
    private ChatSessionDto getOrCreateSession(String sessionId, String title) {
        return sessions.computeIfAbsent(sessionId, id -> {
            ChatSessionDto session = new ChatSessionDto(id, title);
            session.setCreateTime(LocalDateTime.now());
            session.setUpdateTime(LocalDateTime.now());
            return session;
        });
    }

    /**
     * 生成会话ID
     */
    private String generateSessionId() {
        return "chat_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
}
