package com.ruoyi.aiChat.controller;

import com.ruoyi.aiChat.dto.ChatMessageRequest;
import com.ruoyi.aiChat.dto.ChatMessageResponse;
import com.ruoyi.aiChat.dto.ChatSessionDto;
import com.ruoyi.aiChat.service.AiChatService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI聊天Controller
 *
 * @author ruoyi
 * @date 2025-07-03
 */
@RestController
@RequestMapping("/ai/chat")
public class AiChatController extends BaseController {

    @Autowired
    private AiChatService aiChatService;

    /**
     * 发送聊天消息
     */
    @Log(title = "AI聊天", businessType = BusinessType.OTHER)
    @PostMapping("/send")
    public AjaxResult sendMessage(@RequestBody ChatMessageRequest request) {
        try {
            ChatMessageResponse response = aiChatService.sendMessage(request);
            return success(response);
        } catch (Exception e) {
            logger.error("发送聊天消息失败", e);
            return error("发送消息失败：" + e.getMessage());
        }
    }

    /**
     * 获取会话列表
     */
    @GetMapping("/sessions")
    public AjaxResult getSessions() {
        try {
            Long userId = SecurityUtils.getUserId();
            List<ChatSessionDto> sessions = aiChatService.getSessions(userId);
            return success(sessions);
        } catch (Exception e) {
            logger.error("获取会话列表失败", e);
            return error("获取会话列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取会话详情
     */
    @GetMapping("/sessions/{sessionId}")
    public AjaxResult getSession(@PathVariable Long sessionId) {
        try {
            ChatSessionDto session = aiChatService.getSession(sessionId);
            if (session == null) {
                return error("会话不存在");
            }
            return success(session);
        } catch (Exception e) {
            logger.error("获取会话详情失败", e);
            return error("获取会话详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建新会话
     */
    @Log(title = "AI聊天", businessType = BusinessType.INSERT)
    @PostMapping("/sessions")
    public AjaxResult createSession(@RequestBody ChatSessionDto sessionDto) {
        try {
            Long userId = SecurityUtils.getUserId();
            String username = SecurityUtils.getUsername();

            String title = sessionDto.getTitle();
            if (title == null || title.trim().isEmpty()) {
                title = "新对话";
            }

            ChatSessionDto session = aiChatService.createSession(title, userId, username);
            return success(session);
        } catch (Exception e) {
            logger.error("创建会话失败", e);
            return error("创建会话失败：" + e.getMessage());
        }
    }

    /**
     * 删除会话
     */
    @Log(title = "AI聊天", businessType = BusinessType.DELETE)
    @DeleteMapping("/sessions/{sessionId}")
    public AjaxResult deleteSession(@PathVariable Long sessionId) {
        try {
            boolean success = aiChatService.deleteSession(sessionId);
            return success ? success("删除成功") : error("删除失败");
        } catch (Exception e) {
            logger.error("删除会话失败", e);
            return error("删除会话失败：" + e.getMessage());
        }
    }

    /**
     * 清空会话历史
     */
    @Log(title = "AI聊天", businessType = BusinessType.UPDATE)
    @PostMapping("/sessions/{sessionId}/clear")
    public AjaxResult clearSessionHistory(@PathVariable Long sessionId) {
        try {
            boolean success = aiChatService.clearSessionHistory(sessionId);
            return success ? success("清空成功") : error("清空失败");
        } catch (Exception e) {
            logger.error("清空会话历史失败", e);
            return error("清空会话历史失败：" + e.getMessage());
        }
    }

    /**
     * 健康检查 (无需权限验证)
     */
    @GetMapping("/health")
    public AjaxResult health() {
        return success("AI聊天服务运行正常");
    }

    /**
     * 测试端点 (无需权限验证)
     */
    @GetMapping("/test")
    public AjaxResult test() {
        return success("AI聊天API测试成功 - " + java.time.LocalDateTime.now());
    }
}
