package com.ruoyi.aiChat.service;

import com.ruoyi.aiChat.dto.ChatMessageRequest;
import com.ruoyi.aiChat.dto.ChatMessageResponse;
import com.ruoyi.aiChat.dto.ChatSessionDto;
import com.ruoyi.ai.domain.AiModelResource;
import com.ruoyi.aiChat.config.AiChatConfig;
import com.ruoyi.aiChat.domain.AiChatMessage;
import com.ruoyi.aiChat.domain.ChatSessionEntity;
import com.ruoyi.aiChat.domain.MessageRole;
import com.ruoyi.aiChat.repository.AiChatMessageRepository;
import com.ruoyi.aiChat.repository.AiChatSessionRepository;
import com.ruoyi.ai.service.IAiModelResourceService;
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

    @Autowired
    private IAiModelResourceService aiModelResourceService;

    @Autowired
    private AiChatConfig aiChatConfig;

    /**
     * 更新会话信息和保存响应
     */
    private void updateMessage(ChatSessionEntity session, Long sessionId, String aiResponse) {
        List<AiChatMessage> allMessages = aiChatMessageRepository.findBySessionId(sessionId);
        session.setLastActivity(LocalDateTime.now());
        session.setMessageCount(allMessages.size());
        session.setLastMessageContent(aiResponse.length() > 50 ?
                aiResponse.substring(0, 50) + "..." : aiResponse);
        aiChatSessionRepository.save(session);
    }

    /**
     * 获取或创建会话
     */
    private ChatSessionEntity getOrCreateSession(Long sessionId, String title) {
        ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
        if (session == null) {
            session = new ChatSessionEntity();
            session.setSessionId(sessionId);
            session.setTitle(title);
            session.setCreatedAt(LocalDateTime.now());
            session.setLastActivity(LocalDateTime.now());
            session.setLastMessageContent("");
            session.setMessageCount(0);
            session.setIsActive(true);
            session = aiChatSessionRepository.save(session);
        } else {
            // 更新最后活动时间
            session.setLastActivity(LocalDateTime.now());
            session = aiChatSessionRepository.save(session);
        }
        return session;
    }

    /**
     * 发送聊天消息 - 使用数据库持久化
     */
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        try {
            Long sessionId = request.getSessionId();
            if (sessionId == null) {
                sessionId = generateSessionId(); // 修复：如果sessionId为null，生成新的sessionId
            }

            // 获取或创建会话
            ChatSessionEntity session = getOrCreateSession(sessionId, "新对����");

            // 简化的模型调用逻辑 - 直接使用modelId
            String aiResponse;
            String modelName = "default";
            Integer tokenUsage = null;

            if (request.getModelId() != null && !request.getModelId().isEmpty()) {
                try {
                    Long modelId = Long.parseLong(request.getModelId());
                    // 使用新的基于modelId的方法
                    aiResponse = callAiServiceByModelId(modelId, request.getMessage());

                    // 获取模型名称用于响应
                    AiModelResource modelResource = aiModelResourceService.selectAiModelResourceById(modelId);
                    if (modelResource != null) {
                        modelName = modelResource.getResourceName();
                    }

                    // 计算token使用���
                    tokenUsage = calculateTokenUsage(request.getMessage(), aiResponse);
                } catch (NumberFormatException e) {
                    System.err.println("无效的模型ID: " + request.getModelId() + ", 使用默认模型");
                    aiResponse = aiChatAssistant.chat(request.getMessage());
                }
            } else {
                // 使用默认AI助手
                aiResponse = aiChatAssistant.chat(request.getMessage());
            }

            // 保存用户消息到数据库
            AiChatMessage userMessage = new AiChatMessage();
            userMessage.setSessionId(sessionId);
            userMessage.setContent(request.getMessage());
            userMessage.setRole(MessageRole.USER);
            userMessage.setTimestamp(LocalDateTime.now());
            aiChatHistoryService.saveChatMessage(userMessage);

            // 保存AI回复到数据库
            AiChatMessage aiMessage = new AiChatMessage();
            aiMessage.setSessionId(sessionId);
            aiMessage.setContent(aiResponse);
            aiMessage.setRole(MessageRole.AI);
            aiMessage.setTimestamp(LocalDateTime.now());
            aiChatHistoryService.saveChatMessage(aiMessage);

            // 调用新方法更新会话信息
            updateMessage(session, sessionId, aiResponse);

            // 构建响应
            ChatMessageResponse response = new ChatMessageResponse();
            response.setMessageId(UUID.randomUUID().toString());
            response.setSessionId(sessionId);
            response.setContent(aiResponse);
            response.setMessageType("assistant");
            response.setCreateTime(LocalDateTime.now());
            response.setFinished(true);
            response.setModel(modelName);
            response.setTokenUsage(tokenUsage);

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
     * 根据模型ID调用相应的AI服务
     * @param modelId 模型ID
     * @param message 用户消息
     * @return AI回复
     */
    private String callAiServiceByModelId(Long modelId, String message) {
        try {
            // 根据modelId获取模型资源
            AiModelResource modelResource = aiModelResourceService.selectAiModelResourceById(modelId);

            if (modelResource == null) {
                System.err.println("模型ID " + modelId + " 不存在，使用默认模型");
                return aiChatAssistant.chat(message);
            }

            // 调用现有的方法
            return callAiServiceByModel(modelResource, message);

        } catch (Exception e) {
            System.err.println("根据模型ID " + modelId + " 调用AI服务失败: " + e.getMessage());
            return aiChatAssistant.chat(message);
        }
    }

    /**
     * 根据模型配置调用相应的AI服务
     * @param modelResource 模型资源配置
     * @param message 用户消息
     * @return AI回复
     */
    private String callAiServiceByModel(AiModelResource modelResource, String message) {
        // 根据模型类型调用不同的AI服务
        String modelType = modelResource.getResourceType();

        try {
            // 使用配置的模型类型创建对应的AI助手
            AiChatConfig.AiChatAssistant assistant = aiChatConfig.createAiAssistantForModel(modelType);
            return assistant.chat(message);
        } catch (Exception e) {
            // 如果指定模型失败，降级使用默认AI助手
            System.err.println("模型 " + modelType + " 调用失败，使��默认模型: " + e.getMessage());
            return aiChatAssistant.chat(message);
        }
    }

    /**
     * 计算token使用量
     */
    private Integer calculateTokenUsage(String userMessage, String aiResponse) {
        // 这里实现token计算逻辑
        // 可以使用tiktoken-java库或者其他token计算方法
        int userTokens = userMessage.length() / 4; // 简单估算
        int aiTokens = aiResponse.length() / 4; // 简单估算
        return userTokens + aiTokens;
    }

    /**
     * 生成会话ID
     */
    private Long generateSessionId() {
        // 现在返回时间戳作为Long类型的sessionId
        return System.currentTimeMillis();
    }

    /**
     * 保存初始消息到会话
     */
    public void saveInitialMessage(Long sessionId, String messageContent, String username) {
        // 保存用户的初始消息
        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setContent(messageContent);
        userMessage.setRole(MessageRole.USER);
        userMessage.setUsername(username);
        userMessage.setTimestamp(LocalDateTime.now());
        aiChatHistoryService.saveChatMessage(userMessage);

        // 更新会话信息
        updateSessionLastMessage(sessionId, messageContent);

        // 更新消息计数
        ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
        if (session != null) {
            session.setMessageCount(session.getMessageCount() + 1);
            aiChatSessionRepository.save(session);
        }
    }

    /**
     * 批量保存消息到会话
     */
    public void saveMessagesToSession(Long sessionId, List<ChatMessageResponse> messages, String username) {
        for (ChatMessageResponse messageResponse : messages) {
            AiChatMessage message = new AiChatMessage();
            message.setSessionId(sessionId);
            message.setContent(messageResponse.getContent());

            // 根据messageType设置角色
            if ("user".equals(messageResponse.getMessageType())) {
                message.setRole(MessageRole.USER);
                message.setUsername(username);
            } else if ("assistant".equals(messageResponse.getMessageType())) {
                message.setRole(MessageRole.AI);
            } else {
                message.setRole(MessageRole.SYSTEM);
            }

            message.setTimestamp(messageResponse.getCreateTime() != null ?
                    messageResponse.getCreateTime() : LocalDateTime.now());

            aiChatHistoryService.saveChatMessage(message);
        }

        // 更新会话信息
        if (!messages.isEmpty()) {
            ChatMessageResponse lastMessage = messages.get(messages.size() - 1);
            updateSessionLastMessage(sessionId, lastMessage.getContent());

            // 更新消息计数
            ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
            if (session != null) {
                session.setMessageCount(session.getMessageCount() + messages.size());
                aiChatSessionRepository.save(session);
            }
        }
    }

    /**
     * 更新会话的最后消息内容
     */
    public void updateSessionLastMessage(Long sessionId, String messageContent) {
        ChatSessionEntity session = aiChatSessionRepository.findBySessionId(sessionId);
        if (session != null) {
            session.setLastMessageContent(messageContent.length() > 50 ?
                messageContent.substring(0, 50) + "..." : messageContent);
            session.setLastActivity(LocalDateTime.now());
            aiChatSessionRepository.save(session);
        }
    }

    /**
     * 创建新会话 - 保存到数据库
     */
    public ChatSessionDto createSession(String title, Long userId, String username) {
        Long sessionId = generateSessionId();

        ChatSessionEntity entity = new ChatSessionEntity();
        entity.setSessionId(sessionId);
        entity.setTitle(title);
        entity.setUsername(username);
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
    public boolean deleteSession(Long sessionId) {
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
    public boolean clearSessionHistory(Long sessionId) {
        return aiChatHistoryService.clearSessionHistory(sessionId);
    }

    /**
     * 获取会话列表 - 从数据库查询
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
    public ChatSessionDto getSession(Long sessionId) {
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
     * 实体转DTO
     */
    private ChatSessionDto convertToDto(ChatSessionEntity savedEntity) {
        ChatSessionDto dto = new ChatSessionDto(savedEntity.getSessionId(), savedEntity.getTitle());
        dto.setUserId(savedEntity.getUserId() != null ? Long.parseLong(savedEntity.getUserId()) : null);
        dto.setUsername(savedEntity.getUsername());
        dto.setCreateTime(savedEntity.getCreatedAt());
        dto.setUpdateTime(savedEntity.getLastActivity());
        dto.setMessageCount(savedEntity.getMessageCount());
        dto.setLastMessageContent(savedEntity .getLastMessageContent());
        return dto;
    }
}
