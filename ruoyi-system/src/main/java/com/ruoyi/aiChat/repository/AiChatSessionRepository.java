package com.ruoyi.aiChat.repository;

import com.ruoyi.aiChat.domain.ChatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiChatSessionRepository extends JpaRepository<ChatSessionEntity, Long> {
    /**
     * 根据sessionId查找会话
     */
    ChatSessionEntity findBySessionId(String sessionId);

    /**
     * 根据用户ID查找会话
     */
    List<ChatSessionEntity> findByUserId(String userId);

    /**
     * 根据用户ID按更新时间降序查找会话
     */
    List<ChatSessionEntity> findByUserIdOrderByLastActivityDesc(String userId);

    /**
     * 查找活跃的会话
     */
    List<ChatSessionEntity> findByIsActiveTrue();

    /**
     * 根据标题模糊查找会话
     */
    List<ChatSessionEntity> findByTitleContaining(String title);

    /**
     * 根据用户ID和活跃状态查找会话
     */
    List<ChatSessionEntity> findByUserIdAndIsActiveTrue(String userId);

    /**
     * 根据sessionId和用户ID查找会话
     */
    ChatSessionEntity findBySessionIdAndUserId(String sessionId, String userId);

    /**
     * 根据用户ID和标题模糊查找会话
     */
    List<ChatSessionEntity> findByUserIdAndTitleContaining(String userId, String title);
}
