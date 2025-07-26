<template>
  <div class="ai-chat-container">
    <!-- 左侧会话列表 -->
    <div class="session-sidebar">
      <div class="session-header">
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="createNewSession"
          class="new-session-btn"
        >
          新建对话
        </el-button>
      </div>
      
      <div class="session-list">
        <div
          v-for="session in sessions"
          :key="session.sessionId"
          :class="['session-item', { active: currentSessionId === session.sessionId }]"
          @click="selectSession(session.sessionId)"
        >
          <div class="session-title">{{ session.title }}</div>
          <div class="session-preview">{{ session.lastMessageContent || '暂无消息' }}</div>
          <div class="session-time">{{ formatTime(session.updateTime) }}</div>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click.stop="deleteSession(session.sessionId)"
            class="delete-btn"
          />
        </div>
      </div>
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-area">
      <!-- 聊天标题栏 -->
      <div class="chat-header">
        <div class="header-left">
          <h3>{{ currentSession && currentSession.title ? currentSession.title : 'AI 聊天助手' }}</h3>
          <div class="model-info" v-if="selectedModel">
            <i class="el-icon-cpu"></i>
            <span>{{ selectedModel.resourceName }}</span>
          </div>
        </div>
        <div class="chat-actions">
          <!-- AI模型选择器 -->
          <div class="model-selector">
            <el-select
              v-model="selectedModelId"
              placeholder="选择AI模型"
              size="small"
              @change="onModelChange"
              style="width: 200px; margin-right: 10px;"
            >
              <el-option
                v-for="model in availableModels"
                :key="model.id"
                :label="model.resourceName"
                :value="model.id"
              >
                <span style="float: left">{{ model.resourceName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ model.resourceType }}</span>
              </el-option>
            </el-select>
          </div>
          <el-button
            type="text"
            size="small"
            icon="el-icon-refresh"
            @click="clearCurrentSession"
          >
            清空对话
          </el-button>
        </div>
      </div>

      <!-- 消息显示区域 -->
      <div class="message-container" ref="messageContainer">
        <div v-if="!currentMessages || currentMessages.length === 0" class="welcome-message">
          <div class="welcome-icon">🤖</div>
          <h3>欢迎使用 AI 聊天助手</h3>
          <p>我是您的智能助手，可以帮助您解答问题、提供建议或进行对话交流。</p>
          <p>请选择一个AI模型，然后在下方输入您的问题开始对话吧！</p>
          
          <!-- 模型信息卡片 -->
          <div v-if="selectedModel" class="model-card">
            <div class="model-card-header">
              <i class="el-icon-cpu"></i>
              <span>当前模型: {{ selectedModel.resourceName }}</span>
            </div>
            <div class="model-card-body">
              <p><strong>类型:</strong> {{ selectedModel.resourceType }}</p>
              <p><strong>默认模型:</strong> {{ selectedModel.defaultModel }}</p>
              <p v-if="selectedModel.remark"><strong>说明:</strong> {{ selectedModel.remark }}</p>
            </div>
          </div>
        </div>

        <div
          v-for="message in currentMessages"
          :key="message.messageId"
          :class="['message-item', message.messageType]"
        >
          <div class="message-avatar">
            <div v-if="message.messageType === 'user'" class="user-avatar">
              <i class="el-icon-user"></i>
            </div>
            <div v-else class="ai-avatar">
              <i class="el-icon-magic-stick"></i>
            </div>
          </div>
          
          <div class="message-content">
            <div class="message-text" v-html="formatMessage(message.content)"></div>
            <div class="message-meta">
              <span class="message-time">{{ formatTime(message.createTime) }}</span>
              <span v-if="message.model" class="message-model">{{ message.model }}</span>
              <span v-if="message.tokenUsage" class="token-usage">{{ message.tokenUsage }} tokens</span>
            </div>
            <div v-if="message.error" class="message-error">
              错误: {{ message.error }}
            </div>
          </div>
        </div>

        <!-- 正在输入提示 -->
        <div v-if="isTyping" class="message-item assistant typing">
          <div class="message-avatar">
            <div class="ai-avatar">
              <i class="el-icon-magic-stick"></i>
            </div>
          </div>
          <div class="message-content">
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
            <div class="typing-text">{{ selectedModel ? selectedModel.resourceName : 'AI' }} 正在思考中...</div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="2"
            placeholder="请输入您的问题..."
            @keydown.enter.exact="handleEnterKey"
            @keydown.ctrl.enter.exact="handleSend"
            :disabled="isTyping"
            class="message-input"
          />
          <div class="input-actions">
            <div class="input-info">
              <span class="input-tip">按 Enter 换行，Ctrl+Enter 发送</span>
              <span v-if="selectedModel" class="model-badge">
                <i class="el-icon-cpu"></i>
                {{ selectedModel.resourceName }}
              </span>
            </div>
            <el-button
              type="primary"
              :loading="isTyping"
              @click="handleSend"
              :disabled="!inputMessage.trim() || isTyping || !selectedModelId"
              class="send-btn"
            >
              <i class="el-icon-position"></i>
              发送
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { chatApi } from '@/api/ai/chat'

export default {
  name: 'AiChat',
  data() {
    return {
      // 会话相关
      sessions: [],
      currentSessionId: null,
      currentSession: null,
      currentMessages: [],
      
      // 模型相关
      availableModels: [],
      selectedModelId: null,
      selectedModel: null,
      
      // 输入相关
      inputMessage: '',
      isTyping: false,
      
      // 其他
      autoScroll: true
    }
  },
  
  mounted() {
    this.loadAvailableModels()
    this.testApiConnection()
    this.loadSessions()
    this.createNewSession()
  },
  
  methods: {
    // ===== API 测试 =====
    
    async testApiConnection() {
      try {
        console.log('Testing API connection...')
        const response = await chatApi.test()
        console.log('API test response:', response)
        if (response.code === 200) {
          console.log('✅ AI Chat API 连接成功:', response.data)
        } else {
          console.error('❌ AI Chat API 测试失败:', response)
        }
      } catch (error) {
        console.error('❌ AI Chat API 连接失败:', error)
        this.$modal.msgError('AI聊天服务连接失败，请检查后端服务是否正常运行')
      }
    },

    // ===== 模型管理 =====
    
    async loadAvailableModels() {
      try {
        const response = await chatApi.getAvailableModels()
        this.availableModels = response.data || []
        
        // 默认选择第一个可用模型
        if (this.availableModels.length > 0 && !this.selectedModelId) {
          this.selectedModelId = this.availableModels[0].id
          this.selectedModel = this.availableModels[0]
        }
      } catch (error) {
        console.error('加载AI模型列表失败:', error)
        this.$modal.msgError('加载AI模型列表失败')
      }
    },
    
    onModelChange(modelId) {
      this.selectedModel = this.availableModels.find(model => model.id === modelId)
      console.log('切换AI模型:', this.selectedModel)
    },

    // ===== 会话管理 =====
    
    async loadSessions() {
      try {
        const response = await chatApi.getSessions()
        this.sessions = response.data || []
      } catch (error) {
        this.$modal.msgError('加载会话列表失败')
        console.error('Load sessions error:', error)
      }
    },
    
    async createNewSession() {
      try {
        const response = await chatApi.createSession({
          title: '新对话',
          userId: this.$store.state.user ? this.$store.state.user.id : 1,
          username: this.$store.state.user ? this.$store.state.user.name : 'admin'
        })
        
        const newSession = response.data
        this.sessions.unshift(newSession)
        this.selectSession(newSession.sessionId)
      } catch (error) {
        this.$modal.msgError('创建新会话失败')
        console.error('Create session error:', error)
      }
    },
    
    async selectSession(sessionId) {
      if (this.currentSessionId === sessionId) return
      
      try {
        this.currentSessionId = sessionId
        this.currentSession = this.sessions.find(s => s.sessionId === sessionId)
        
        const response = await chatApi.getSession(sessionId)
        this.currentMessages = response.data?.messages || []
        
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      } catch (error) {
        this.$modal.msgError('加载会话失败')
        console.error('Select session error:', error)
      }
    },
    
    async deleteSession(sessionId) {
      try {
        await this.$confirm('确认删除此对话？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await chatApi.deleteSession(sessionId)
        
        this.sessions = this.sessions.filter(s => s.sessionId !== sessionId)
        
        if (this.currentSessionId === sessionId) {
          if (this.sessions.length > 0) {
            this.selectSession(this.sessions[0].sessionId)
          } else {
            this.createNewSession()
          }
        }
        
        this.$modal.msgSuccess('删除成功')
      } catch (error) {
        if (error !== 'cancel') {
          this.$modal.msgError('删除失败')
          console.error('Delete session error:', error)
        }
      }
    },
    
    async clearCurrentSession() {
      if (!this.currentSessionId) return
      
      try {
        await this.$confirm('确认清空当前对话历史？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await chatApi.clearSession(this.currentSessionId)
        this.currentMessages = []
        
        // 更新会话列表中的信息
        const session = this.sessions.find(s => s.sessionId === this.currentSessionId)
        if (session) {
          session.messageCount = 0
          session.lastMessageContent = ''
        }
        
        this.$modal.msgSuccess('清空成功')
      } catch (error) {
        if (error !== 'cancel') {
          this.$modal.msgError('清空失败')
          console.error('Clear session error:', error)
        }
      }
    },
    
    // ===== 消息处理 =====
    
    handleEnterKey(event) {
      if (!event.ctrlKey) {
        event.preventDefault()
        this.inputMessage += '\n'
      }
    },
    
    async handleSend() {
      if (!this.inputMessage.trim() || this.isTyping) return
      
      const message = this.inputMessage.trim()
      this.inputMessage = ''
      
      // 添加用户消息到界面
      const userMessage = {
        messageId: Date.now() + '_user',
        sessionId: this.currentSessionId,
        content: message,
        messageType: 'user',
        createTime: new Date(),
        finished: true
      }
      
      this.currentMessages.push(userMessage)
      this.isTyping = true
      
      this.$nextTick(() => {
        this.scrollToBottom()
      })
      
      try {
        // 发送消息到后端
        const response = await chatApi.sendMessage({
          sessionId: this.currentSessionId,
          message: message,
          modelId: this.selectedModelId, // 使用选中的模型ID
          userId: this.$store.state.user ? this.$store.state.user.id : 1,
          username: this.$store.state.user ? this.$store.state.user.name : 'admin'
        })
        
        const aiMessage = response.data
        this.currentMessages.push(aiMessage)
        
        // 更新会话信息
        const session = this.sessions.find(s => s.sessionId === this.currentSessionId)
        if (session) {
          session.lastMessageContent = aiMessage.content.length > 50 
            ? aiMessage.content.substring(0, 50) + '...' 
            : aiMessage.content
          session.updateTime = aiMessage.createTime
          session.messageCount += 2 // 用户消息 + AI回复
        }
        
      } catch (error) {
        this.$modal.msgError('发送消息失败')
        console.error('Send message error:', error)
        
        // 添加错误消息
        this.currentMessages.push({
          messageId: Date.now() + '_error',
          sessionId: this.currentSessionId,
          content: '抱歉，消息发送失败，请稍后重试。',
          messageType: 'assistant',
          createTime: new Date(),
          error: error.message || '未知错误',
          finished: true
        })
      } finally {
        this.isTyping = false
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    },
    
    // ===== 工具方法 =====
    
    formatMessage(content) {
      if (!content) return ''
      
      // 简单的 Markdown 格式化
      return content
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/`(.*?)`/g, '<code>$1</code>')
    },
    
    formatTime(time) {
      if (!time) return ''
      
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) { // 1分钟内
        return '刚刚'
      } else if (diff < 3600000) { // 1小时内
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) { // 24小时内
        return Math.floor(diff / 3600000) + '小时前'
      } else {
        return date.toLocaleDateString()
      }
    },
    
    scrollToBottom() {
      if (!this.autoScroll) return
      
      this.$nextTick(() => {
        const container = this.$refs.messageContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.ai-chat-container {
  display: flex;
  height: calc(100vh - 120px);
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}

// ===== 左侧会话列表 =====
.session-sidebar {
  width: 280px;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  background: #f8f9fa;

  .session-header {
    padding: 16px;
    border-bottom: 1px solid #e4e7ed;
    background: #fff;

    .new-session-btn {
      width: 100%;
    }
  }

  .session-list {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
  }

  .session-item {
    padding: 12px;
    margin-bottom: 4px;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.2s;
    position: relative;

    &:hover {
      background: #e9ecef;

      .delete-btn {
        opacity: 1;
      }
    }

    &.active {
      background: #e3f2fd;
      border-left: 3px solid #2196f3;
    }

    .session-title {
      font-weight: 500;
      margin-bottom: 4px;
      color: #333;
    }

    .session-preview {
      font-size: 12px;
      color: #666;
      line-height: 1.4;
      max-height: 28px;
      overflow: hidden;
      margin-bottom: 4px;
    }

    .session-time {
      font-size: 11px;
      color: #999;
    }

    .delete-btn {
      position: absolute;
      top: 8px;
      right: 8px;
      opacity: 0;
      transition: opacity 0.2s;
      color: #f56c6c;
    }
  }
}

// ===== 右侧聊天区域 =====
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.chat-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;

  .header-left {
    display: flex;
    align-items: center;

    h3 {
      margin: 0;
      color: #333;
    }

    .model-info {
      display: flex;
      align-items: center;
      margin-left: 15px;
      font-size: 14px;
      color: #666;

      .el-icon-cpu {
        margin-right: 5px;
        font-size: 16px;
      }
    }
  }

  .chat-actions {
    display: flex;
    align-items: center;

    .model-selector {
      margin-right: 15px;
    }
  }
}

.message-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.welcome-message {
  text-align: center;
  padding: 60px 20px;
  color: #666;

  .welcome-icon {
    font-size: 48px;
    margin-bottom: 20px;
  }

  h3 {
    margin-bottom: 16px;
    color: #333;
  }

  p {
    margin-bottom: 8px;
    line-height: 1.6;
  }

  .model-card {
    margin-top: 20px;
    background: #f0f9eb;
    border: 1px solid #e1f3d8;
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

    .model-card-header {
      display: flex;
      align-items: center;
      margin-bottom: 10px;
      color: #67c23a;

      .el-icon-cpu {
        margin-right: 8px;
        font-size: 20px;
      }
    }

    .model-card-body {
      font-size: 13px;
      color: #909399;
      line-height: 1.6;

      p {
        margin-bottom: 5px;
      }
    }
  }
}

.message-item {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;

  &.user {
    flex-direction: row-reverse;

    .message-content {
      margin-right: 12px;
      margin-left: 0;
    }

    .message-text {
      background: #2196f3;
      color: white;
    }
  }

  &.assistant .message-content {
    margin-left: 12px;
  }

  .message-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;

    .user-avatar {
      background: #2196f3;
      color: white;
      width: 100%;
      height: 100%;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .ai-avatar {
      background: #4caf50;
      color: white;
      width: 100%;
      height: 100%;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .message-content {
    max-width: 70%;
    
    .message-text {
      padding: 12px 16px;
      border-radius: 12px;
      background: white;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
      line-height: 1.6;
      word-wrap: break-word;
    }

    .message-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 4px;
      font-size: 11px;
      color: #999;
    }

    .message-time {
      // font-size: 11px;
      // color: #999;
      // margin-top: 4px;
      // text-align: center;
    }

    .message-model {
      background: #e6f7ff;
      color: #1890ff;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 11px;
    }

    .token-usage {
      background: #fffbe6;
      color: #faad14;
      padding: 2px 6px;
      border-radius: 4px;
      font-size: 11px;
    }

    .message-error {
      font-size: 11px;
      color: #f56c6c;
      margin-top: 4px;
      text-align: center;
    }
  }

  &.typing .typing-indicator {
    padding: 12px 16px;
    background: white;
    border-radius: 12px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 4px;
    margin-bottom: 5px;

    span {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #ccc;
      animation: typing 1.4s infinite ease-in-out;

      &:nth-child(1) { animation-delay: -0.32s; }
      &:nth-child(2) { animation-delay: -0.16s; }
    }
  }
  
  &.typing .typing-text {
    font-size: 12px;
    color: #999;
    text-align: center;
  }
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.input-area {
  padding: 20px;
  border-top: 1px solid #e4e7ed;
  background: #fff;

  .input-container {
    border: 1px solid #dcdfe6;
    border-radius: 8px;
    overflow: hidden;
    background: white;

    .message-input {
      ::v-deep .el-textarea__inner {
        border: none;
        resize: none;
        box-shadow: none;
        padding: 12px;
        font-size: 14px;
        line-height: 1.6;
      }
    }

    .input-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 12px;
      background: #f8f9fa;
      border-top: 1px solid #e4e7ed;

      .input-info {
        display: flex;
        align-items: center;
        font-size: 12px;
        color: #999;

        .input-tip {
          margin-right: 10px;
        }

        .model-badge {
          background: #e6f7ff;
          color: #1890ff;
          padding: 2px 8px;
          border-radius: 4px;
          display: flex;
          align-items: center;
          font-size: 12px;

          .el-icon-cpu {
            margin-right: 4px;
            font-size: 14px;
          }
        }
      }

      .send-btn {
        padding: 6px 16px;
      }
    }
  }
}

// ===== 响应式设计 =====
@media (max-width: 768px) {
  .ai-chat-container {
    flex-direction: column;
    height: calc(100vh - 60px);
  }

  .session-sidebar {
    width: 100%;
    height: 200px;
    border-right: none;
    border-bottom: 1px solid #e4e7ed;
  }

  .message-item .message-content {
    max-width: 85%;
  }
}
</style>
