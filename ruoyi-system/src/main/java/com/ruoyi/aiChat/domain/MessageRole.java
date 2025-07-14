package com.ruoyi.aiChat.domain;

/**
 * 消息角色枚举
 */
public enum MessageRole {
    USER("user", "用户"),
    AI("ai", "AI助手"),
    SYSTEM("system", "系统");

    private final String code;
    private final String description;

    MessageRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取对应的枚举
     */
    public static MessageRole fromCode(String code) {
        for (MessageRole role : MessageRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的消息角色: " + code);
    }
}
