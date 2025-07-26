import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import './AiChat.css';

const AiChatReact = () => {
  // State management using React hooks
  const [sessions, setSessions] = useState([]);
  const [currentSessionId, setCurrentSessionId] = useState(null);
  const [currentSession, setCurrentSession] = useState(null);
  const [currentMessages, setCurrentMessages] = useState([]);
  
  // AI Model management
  const [availableModels, setAvailableModels] = useState([]);
  const [selectedModelId, setSelectedModelId] = useState(null);
  const [selectedModel, setSelectedModel] = useState(null);
  
  // Input and interaction state
  const [inputMessage, setInputMessage] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [autoScroll] = useState(true);
  
  // Refs
  const messageContainerRef = useRef(null);
  
  // API configuration
  const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';
  
  const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // API methods
  const chatApi = {
    test: () => api.get('/ai/chat/test'),
    getAvailableModels: () => api.get('/ai/modelResource/available'),
    sendMessage: (data) => api.post('/ai/chat/send', data),
    getSessions: (params) => api.get('/ai/chat/sessions', { params }),
    getSession: (sessionId) => api.get(`/ai/chat/sessions/${sessionId}`),
    createSession: (data) => api.post('/ai/chat/sessions', data),
    deleteSession: (sessionId) => api.delete(`/ai/chat/sessions/${sessionId}`),
    clearSession: (sessionId) => api.post(`/ai/chat/sessions/${sessionId}/clear`),
  };

  // Effects
  useEffect(() => {
    initializeChat();
  }, []);

  useEffect(() => {
    if (autoScroll && messageContainerRef.current) {
      messageContainerRef.current.scrollTop = messageContainerRef.current.scrollHeight;
    }
  }, [currentMessages, isTyping]);

  // Initialization
  const initializeChat = async () => {
    try {
      await loadAvailableModels();
      await testApiConnection();
      await loadSessions();
      await createNewSession();
    } catch (error) {
      console.error('初始化聊天失败:', error);
    }
  };

  // API connection test
  const testApiConnection = async () => {
    try {
      console.log('Testing API connection...');
      const response = await chatApi.test();
      if (response.data.code === 200) {
        console.log('✅ AI Chat API 连接成功:', response.data.data);
      } else {
        console.error('❌ AI Chat API 测试失败:', response.data);
      }
    } catch (error) {
      console.error('❌ AI Chat API 连接失败:', error);
      alert('AI聊天服务连接失败，请检查后端服务是否正常运行');
    }
  };

  // Model management
  const loadAvailableModels = async () => {
    try {
      const response = await chatApi.getAvailableModels();
      const models = response.data.data || [];
      setAvailableModels(models);
      
      // Default to first available model
      if (models.length > 0 && !selectedModelId) {
        setSelectedModelId(models[0].id);
        setSelectedModel(models[0]);
      }
    } catch (error) {
      console.error('加载AI模型列表失败:', error);
      alert('加载AI模型列表失败');
    }
  };

  const handleModelChange = (modelId) => {
    const model = availableModels.find(m => m.id === parseInt(modelId));
    setSelectedModelId(parseInt(modelId));
    setSelectedModel(model);
    console.log('切换AI模型:', model);
  };

  // Session management
  const loadSessions = async () => {
    try {
      const response = await chatApi.getSessions();
      setSessions(response.data.data || []);
    } catch (error) {
      console.error('加载会话列表失败:', error);
      alert('加载会话列表失败');
    }
  };

  const createNewSession = async () => {
    try {
      const response = await chatApi.createSession({
        title: '新对话',
        userId: 1, // Mock user ID
        username: 'admin', // Mock username
      });
      
      const newSession = response.data.data;
      setSessions(prev => [newSession, ...prev]);
      selectSession(newSession.sessionId);
    } catch (error) {
      console.error('创建新会话失败:', error);
      alert('创建新会话失败');
    }
  };

  const selectSession = async (sessionId) => {
    if (currentSessionId === sessionId) return;
    
    try {
      setCurrentSessionId(sessionId);
      const session = sessions.find(s => s.sessionId === sessionId);
      setCurrentSession(session);
      
      const response = await chatApi.getSession(sessionId);
      setCurrentMessages(response.data.data?.messages || []);
    } catch (error) {
      console.error('加载会话失败:', error);
      alert('加载会话失败');
    }
  };

  const deleteSession = async (sessionId, event) => {
    event.stopPropagation();
    
    if (!window.confirm('确认删除此对话？')) return;
    
    try {
      await chatApi.deleteSession(sessionId);
      const updatedSessions = sessions.filter(s => s.sessionId !== sessionId);
      setSessions(updatedSessions);
      
      if (currentSessionId === sessionId) {
        if (updatedSessions.length > 0) {
          selectSession(updatedSessions[0].sessionId);
        } else {
          createNewSession();
        }
      }
      
      alert('删除成功');
    } catch (error) {
      console.error('删除会话失败:', error);
      alert('删除失败');
    }
  };

  const clearCurrentSession = async () => {
    if (!currentSessionId) return;
    
    if (!window.confirm('确认清空当前对话历史？')) return;
    
    try {
      await chatApi.clearSession(currentSessionId);
      setCurrentMessages([]);
      
      // Update session in list
      setSessions(prev => prev.map(s => 
        s.sessionId === currentSessionId 
          ? { ...s, messageCount: 0, lastMessageContent: '' }
          : s
      ));
      
      alert('清空成功');
    } catch (error) {
      console.error('清空会话失败:', error);
      alert('清空失败');
    }
  };

  // Message handling
  const handleKeyPress = (event) => {
    if (event.key === 'Enter' && !event.ctrlKey) {
      event.preventDefault();
      setInputMessage(prev => prev + '\n');
    } else if (event.key === 'Enter' && event.ctrlKey) {
      event.preventDefault();
      handleSend();
    }
  };

  const handleSend = async () => {
    if (!inputMessage.trim() || isTyping || !selectedModelId) return;
    
    const message = inputMessage.trim();
    setInputMessage('');
    
    // Add user message to UI
    const userMessage = {
      messageId: Date.now() + '_user',
      sessionId: currentSessionId,
      content: message,
      messageType: 'user',
      createTime: new Date(),
      finished: true,
    };
    
    setCurrentMessages(prev => [...prev, userMessage]);
    setIsTyping(true);
    
    try {
      // Send message to backend
      const response = await chatApi.sendMessage({
        sessionId: currentSessionId,
        message: message,
        modelId: selectedModelId,
        userId: 1, // Mock user ID
        username: 'admin', // Mock username
      });
      
      const aiMessage = response.data.data;
      setCurrentMessages(prev => [...prev, aiMessage]);
      
      // Update session info
      setSessions(prev => prev.map(s => 
        s.sessionId === currentSessionId
          ? {
              ...s,
              lastMessageContent: aiMessage.content.length > 50 
                ? aiMessage.content.substring(0, 50) + '...' 
                : aiMessage.content,
              updateTime: aiMessage.createTime,
              messageCount: s.messageCount + 2, // User + AI message
            }
          : s
      ));
      
    } catch (error) {
      console.error('发送消息失败:', error);
      alert('发送消息失败');
      
      // Add error message
      const errorMessage = {
        messageId: Date.now() + '_error',
        sessionId: currentSessionId,
        content: '抱歉，消息发送失败，请稍后重试。',
        messageType: 'assistant',
        createTime: new Date(),
        error: error.message || '未知错误',
        finished: true,
      };
      setCurrentMessages(prev => [...prev, errorMessage]);
    } finally {
      setIsTyping(false);
    }
  };

  // Utility functions
  const formatMessage = (content) => {
    if (!content) return '';
    
    return content
      .replace(/\n/g, '<br>')
      .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
      .replace(/\*(.*?)\*/g, '<em>$1</em>')
      .replace(/`(.*?)`/g, '<code>$1</code>');
  };

  const formatTime = (time) => {
    if (!time) return '';
    
    const date = new Date(time);
    const now = new Date();
    const diff = now - date;
    
    if (diff < 60000) {
      return '刚刚';
    } else if (diff < 3600000) {
      return Math.floor(diff / 60000) + '分钟前';
    } else if (diff < 86400000) {
      return Math.floor(diff / 3600000) + '小时前';
    } else {
      return date.toLocaleDateString();
    }
  };

  return (
    <div className="ai-chat-container">
      {/* Left sidebar - Session list */}
      <div className="session-sidebar">
        <div className="session-header">
          <button
            className="new-session-btn"
            onClick={createNewSession}
          >
            <span className="icon">+</span>
            新建对话
          </button>
        </div>
        
        <div className="session-list">
          {sessions.map(session => (
            <div
              key={session.sessionId}
              className={`session-item ${currentSessionId === session.sessionId ? 'active' : ''}`}
              onClick={() => selectSession(session.sessionId)}
            >
              <div className="session-title">{session.title}</div>
              <div className="session-preview">
                {session.lastMessageContent || '暂无消息'}
              </div>
              <div className="session-time">{formatTime(session.updateTime)}</div>
              <button
                className="delete-btn"
                onClick={(e) => deleteSession(session.sessionId, e)}
                aria-label="删除会话"
              >
                ×
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Right side - Chat area */}
      <div className="chat-area">
        {/* Chat header */}
        <div className="chat-header">
          <div className="header-left">
            <h3>{currentSession?.title || 'AI 聊天助手'}</h3>
            {selectedModel && (
              <div className="model-info">
                <span className="icon">🤖</span>
                <span>{selectedModel.resourceName}</span>
              </div>
            )}
          </div>
          <div className="chat-actions">
            {/* AI Model Selector */}
            <div className="model-selector">
              <select
                value={selectedModelId || ''}
                onChange={(e) => handleModelChange(e.target.value)}
                className="model-select"
              >
                <option value="">选择AI模型</option>
                {availableModels.map(model => (
                  <option key={model.id} value={model.id}>
                    {model.resourceName} ({model.resourceType})
                  </option>
                ))}
              </select>
            </div>
            <button
              className="clear-btn"
              onClick={clearCurrentSession}
            >
              🗑️ 清空对话
            </button>
          </div>
        </div>

        {/* Message container */}
        <div className="message-container" ref={messageContainerRef}>
          {(!currentMessages || currentMessages.length === 0) ? (
            <div className="welcome-message">
              <div className="welcome-icon">🤖</div>
              <h3>欢迎使用 AI 聊天助手</h3>
              <p>我是您的智能助手，可以帮助您解答问题、提供建议或进行对话交流。</p>
              <p>请选择一个AI模型，然后在下方输入您的问题开始对话吧！</p>
              
              {selectedModel && (
                <div className="model-card">
                  <div className="model-card-header">
                    <span className="icon">🤖</span>
                    <span>当前模型: {selectedModel.resourceName}</span>
                  </div>
                  <div className="model-card-body">
                    <p><strong>类型:</strong> {selectedModel.resourceType}</p>
                    <p><strong>默认模型:</strong> {selectedModel.defaultModel}</p>
                    {selectedModel.remark && (
                      <p><strong>说明:</strong> {selectedModel.remark}</p>
                    )}
                  </div>
                </div>
              )}
            </div>
          ) : (
            currentMessages.map(message => (
              <div
                key={message.messageId}
                className={`message-item ${message.messageType}`}
              >
                <div className="message-avatar">
                  <div className={message.messageType === 'user' ? 'user-avatar' : 'ai-avatar'}>
                    {message.messageType === 'user' ? '👤' : '🤖'}
                  </div>
                </div>
                
                <div className="message-content">
                  <div 
                    className="message-text" 
                    dangerouslySetInnerHTML={{ __html: formatMessage(message.content) }}
                  />
                  <div className="message-meta">
                    <span className="message-time">{formatTime(message.createTime)}</span>
                    {message.model && (
                      <span className="message-model">{message.model}</span>
                    )}
                    {message.tokenUsage && (
                      <span className="token-usage">{message.tokenUsage} tokens</span>
                    )}
                  </div>
                  {message.error && (
                    <div className="message-error">
                      错误: {message.error}
                    </div>
                  )}
                </div>
              </div>
            ))
          )}

          {/* Typing indicator */}
          {isTyping && (
            <div className="message-item assistant typing">
              <div className="message-avatar">
                <div className="ai-avatar">🤖</div>
              </div>
              <div className="message-content">
                <div className="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
                <div className="typing-text">
                  {selectedModel ? selectedModel.resourceName : 'AI'} 正在思考中...
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Input area */}
        <div className="input-area">
          <div className="input-container">
            <textarea
              value={inputMessage}
              onChange={(e) => setInputMessage(e.target.value)}
              onKeyDown={handleKeyPress}
              placeholder="请输入您的问题..."
              disabled={isTyping}
              className="message-input"
              rows="2"
            />
            <div className="input-actions">
              <div className="input-info">
                <span className="input-tip">按 Enter 换行，Ctrl+Enter 发送</span>
                {selectedModel && (
                  <span className="model-badge">
                    <span className="icon">🤖</span>
                    {selectedModel.resourceName}
                  </span>
                )}
              </div>
              <button
                className="send-btn"
                onClick={handleSend}
                disabled={!inputMessage.trim() || isTyping || !selectedModelId}
              >
                {isTyping ? '发送中...' : '📤 发送'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AiChatReact;