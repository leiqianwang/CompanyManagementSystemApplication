package com.ruoyi.aiChat.repository;

import com.ruoyi.aiChat.domain.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {
    // 根据sessionId查找会话
    ChatSessionEntity findBySessionId(Long sessionId);
    // 你可以根据需要扩展更多查询方法，如按userId查找等


}

