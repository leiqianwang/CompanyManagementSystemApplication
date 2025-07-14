package com.ruoyi.aiChat.repository;

import com.ruoyi.aiChat.domain.AiChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {
    /**
     * 根据会话ID查找消息
     */
    List<AiChatMessage> findBySessionId(String sessionId);

    /**
     * 根据用户ID查找消息
     */
    List<AiChatMessage> findByUserId(Long userId);

    /**
     * 根据用户名查找消息
     */
    List<AiChatMessage> findByUsername(String username);

    /**
     * 根据内容关键词查找消息
     */
    List<AiChatMessage> findByContentContaining(String keyword);

    /**
     * 根据时间范围查找消息
     */
    List<AiChatMessage> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 删除指定会话的所有消息
     */
    void deleteBySessionId(String sessionId);
}
