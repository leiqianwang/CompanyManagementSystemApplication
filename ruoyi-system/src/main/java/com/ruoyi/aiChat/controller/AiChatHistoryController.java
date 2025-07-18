package com.ruoyi.aiChat.controller;

import com.ruoyi.aiChat.domain.ChatSessionEntity;
import com.ruoyi.aiChat.service.AiChatHistoryService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import dev.langchain4j.data.message.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI聊天历史控制器
 * 提供聊天会话和消息历史的REST API
 */
@RestController
@RequestMapping("/ai/chat/history")
public class AiChatHistoryController extends BaseController {

  private final AiChatHistoryService aiChatHistoryService;

  @Autowired
  public AiChatHistoryController(AiChatHistoryService aiChatHistoryService) {
    this.aiChatHistoryService = aiChatHistoryService;
  }

  /**
   * 获取所有聊天会话
   * @param userId 可选的用户ID过滤条件
   * @return 聊天会话列表
   */
  @GetMapping()
  public AjaxResult getAllChatHistory(@RequestParam(required = false) String userId) {
    try {
      List<ChatSessionEntity> sessions = aiChatHistoryService.getAllChatHistory(userId);
      return success(sessions);
    } catch (Exception e) {
      logger.error("获取聊天会话列表失败", e);
      return error("获取聊天会话列表失败：" + e.getMessage());
    }
  }

  /**
   * 获取指定会话的聊天历史
   * @param sessionId 会话ID
   * @return 聊天消息列表
   */
  @GetMapping("/{sessionId}")
  public AjaxResult getChatHistory(@PathVariable Long sessionId) {
    if (sessionId == null) {
      return error("会话ID不能为空");
    }

    try {
      List<ChatMessage> chatHistory = aiChatHistoryService.getChatHistory(sessionId);
      if (chatHistory.isEmpty()) {
        return warn("该会话暂无聊天记录");
      }
      return success(chatHistory);
    } catch (Exception e) {
      logger.error("获取聊天历史失败", e);
      return error("获取聊天历史失败：" + e.getMessage());
    }
  }

  /**
   * 删除指定会话的聊天历史
   * @param sessionId 会话ID
   * @return 操作结果
   */
  @DeleteMapping("/{sessionId}")
  public AjaxResult deleteChatHistory(@PathVariable Long sessionId) {
    if (sessionId == null) {
      return error("会话ID不能为空");
    }

    try {
      aiChatHistoryService.deleteChatHistory(sessionId);
      return success("聊天历史记录已删除");
    } catch (Exception e) {
      logger.error("删除聊天历史失败", e);
      return error("删除聊天历史失败：" + e.getMessage());
    }
  }

  /**
   * 删除指定用户的所有聊天历史
   * @param username 用户名
   * @return 操作结果
   */
  @DeleteMapping("/user/{username}")
  public AjaxResult deleteChatHistoryByUser(@PathVariable String username) {
    if (username == null || username.trim().isEmpty()) {
      return error("用户名不能为空");
    }

    try {
      aiChatHistoryService.deleteChatHistoryByUser(username);
      return success("用户的聊天历史记录已删除");
    } catch (Exception e) {
      logger.error("删除用户聊天历史失败", e);
      return error("删除用户聊天历史失败：" + e.getMessage());
    }
  }
}
