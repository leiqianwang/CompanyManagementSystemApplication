package com.ruoyi.aiChat.repository;

import com.ruoyi.aiChat.domain.AiChatMessage;
import dev.langchain4j.data.message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {
    /**
     * 根据用户ID按时间戳降序查找消息
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    // 这里可以根据实际需求添加更多查询方法
    List<AiChatMessage> findByUserId(Long userId);
    // 可根据实际需求扩展更多方法

  List<AiChatMessage> findBySessionId(Long sessionId);

  List<AiChatMessage> findByTimestampBetween(LocalDateTime start, LocalDateTime end);


  List<AiChatMessage> findByUsername(String username);

  List<AiChatMessage> findByContentContaining(String keyword);


  void deleteBySessionId(Long sessionId);
}

