package com.ruoyi.aiChat.service;

import com.ruoyi.aiChat.domain.AiChatMessage;
import com.ruoyi.aiChat.domain.ChatSessionEntity;
import com.ruoyi.aiChat.domain.MessageRole;
import com.ruoyi.aiChat.repository.AiChatSessionRepository;
import com.ruoyi.aiChat.repository.AiChatMessageRepository;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * AI聊天历史服务类
 * 处理聊天会话和消息的业务逻辑
 */
@Service
public class AiChatHistoryService {

    @Autowired
    private final AiChatMessageRepository aiChatMessageRepository;
    private final AiChatSessionRepository aiChatSessionRepository;

    public AiChatHistoryService(AiChatMessageRepository aiChatMessageRepository, AiChatSessionRepository aiChatSessionRepository) {
        this.aiChatMessageRepository = aiChatMessageRepository;
        this.aiChatSessionRepository = aiChatSessionRepository;
    }

    /**
     * 清除指定会话的历史记录
     * @param sessionId 会话ID
     * @return 是否成功清除
     */
    public Boolean clearSessionHistory(Long sessionId) {
        // 使用会话Repository查找会话实体
        ChatSessionEntity chatSessionEntity = aiChatSessionRepository.findBySessionId(sessionId);
        if(chatSessionEntity != null){
            // 删除该会话下的所有消息
            aiChatMessageRepository.deleteBySessionId(sessionId);
            // 同步更新会话信息
            chatSessionEntity.setMessageCount(0);
            chatSessionEntity.setLastMessageContent("");
            aiChatSessionRepository.save(chatSessionEntity);
            return true; // 成功清除会话历史
        }
        return false; // 会话不存在，无法清除历史
    }

    /**
     * 获取所有聊天会话，可选择按用户ID过滤
     * @param userId 用户ID（可选）
     * @return 聊天会话列表
     */
    public List<ChatSessionEntity> getAllChatHistory(String userId) {
        // 获取所有聊天会话
        List<ChatSessionEntity> chatSessions = aiChatSessionRepository.findAll();
        // 如果需要根据用户名过滤，可以在这里添加逻辑
        if (userId != null && !userId.isEmpty()) {
            chatSessions.removeIf(session -> !session.getUserId().equals(userId));
        }
        return chatSessions;
    }

    /**
     * 获取指定会话的聊天历史记录
     * @param sessionId 会话ID
     * @return 聊天消息列表
     */
    public List<ChatMessage> getChatHistory(Long sessionId) {
        // 从数据库获取聊天历史记录
        List<AiChatMessage> dbMessages = aiChatMessageRepository.findBySessionId(sessionId);
        // 转换为ChatMessage列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        for(AiChatMessage message : dbMessages){
            // 根据消息角色创建对应的消息对象
            if(MessageRole.USER.equals(message.getRole())) {
                // 用户消息
                chatMessages.add(new UserMessage(message.getContent()));
            } else if(MessageRole.AI.equals(message.getRole())) {
                // AI消息
                chatMessages.add(new AiMessage(message.getContent()));
            } else if(MessageRole.SYSTEM.equals(message.getRole())) {
                // 系统消息
                chatMessages.add(new SystemMessage(message.getContent()));
            }
        }
        return chatMessages;
    }

    /**
     * 保存聊天消息到数据库
     * @param message 聊天消息
     */
    public void saveChatMessage(AiChatMessage message) {
        aiChatMessageRepository.save(message);
    }

    /**
     * 删除指定会话的聊天历史记录
     * @param sessionId 会话ID
     */
    public void deleteChatHistory(Long sessionId) {
        aiChatMessageRepository.deleteBySessionId(sessionId);
    }

    /**
     * 删除指定用户的所有聊天历史记录
     * @param username 用户名
     */
    public void deleteChatHistoryByUser(String username) {
        // 获取用户的所有消息并批量删除
        List<AiChatMessage> messages = aiChatMessageRepository.findByUsername(username);
        if (!messages.isEmpty()) {
            aiChatMessageRepository.deleteAll(messages);
        }
    }
}
