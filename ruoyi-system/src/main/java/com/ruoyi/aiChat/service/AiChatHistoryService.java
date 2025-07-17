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

@Service
public class AiChatHistoryService {

    private final AiChatMessageRepository aiChatMessageRepository;
    private final AiChatSessionRepository aiChatSessionRepository;

    @Autowired
    public AiChatHistoryService(AiChatMessageRepository aiChatMessageRepository, AiChatSessionRepository aiChatSessionRepository) {
        this.aiChatMessageRepository = aiChatMessageRepository;
        this.aiChatSessionRepository = aiChatSessionRepository;
    }

    public Boolean clearSessionHistory(String sessionId) {
        // 使用会话Repository查找会话实体
        ChatSessionEntity chatSessionEntity = aiChatSessionRepository.findBySessionId(sessionId);
        if(chatSessionEntity != null){
            // 删除该会话下的所有消息
            aiChatMessageRepository.deleteBySessionId(sessionId);
            // 同步更新会话信息
            chatSessionEntity.setMessageCount(0);
            chatSessionEntity.setLastMessageContent("");
            aiChatSessionRepository.saveAndFlush(chatSessionEntity);
            return true; // 成功清除会话历史
        }
        return false; // 会话不存在，无法清除历史
    }

    public List<ChatMessage> getChatHistory(String sessionId) {
        // 从数据库获取聊天历史记录
        List<AiChatMessage> dbMessages = aiChatMessageRepository.findBySessionId(sessionId);
        // 声明一个新ArrayList来存储转换后的消息
        List<ChatMessage> chatMessages = new ArrayList<>();

        for(AiChatMessage message : dbMessages){
            // 检查消息是用户发送的还是AI生成的
            if(MessageRole.USER.equals(message.getRole())) {
                // 如果是用户消息，创建UserMessage对象
                chatMessages.add(new UserMessage(message.getContent()));
            } else if(MessageRole.AI.equals(message.getRole())) {
                // 如果是AI消息，创建AiMessage对象
                chatMessages.add(new AiMessage(message.getContent()));
            } else if(MessageRole.SYSTEM.equals(message.getRole())) {
                // 如果是系统消息，创建SystemMessage对象
                chatMessages.add(new SystemMessage(message.getContent()));
            }
        }

        return chatMessages;
    }

    public AiChatMessage saveChatMessage(AiChatMessage message) {
        // 保存聊天消息到数据库
        return aiChatMessageRepository.save(message);
    }

    public void deleteChatHistory(String sessionId) {
        // 删除指定会话的聊天历史记录
        aiChatMessageRepository.deleteBySessionId(sessionId);
    }
}
