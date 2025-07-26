import request from '@/utils/request'

// AI聊天相关API
export const chatApi = {
  // 测试API连接
  test() {
    return request({
      url: '/ai/chat/test',
      method: 'get'
    })
  },

  // 健康检查
  health() {
    return request({
      url: '/ai/chat/health',
      method: 'get'
    })
  },

  // 获取可用的AI模型列表
  getAvailableModels() {
    return request({
      url: '/ai/modelResource/available',
      method: 'get'
    })
  },

  // 发送聊天消息
  sendMessage(data) {
    return request({
      url: '/ai/chat/send',
      method: 'post',
      data: data
    })
  },

  // 获取用户会话列表
  getSessions(params) {
    return request({
      url: '/ai/chat/sessions',
      method: 'get',
      params: params
    })
  },

  // 获取会话详情和消息历史
  getSession(sessionId) {
    return request({
      url: `/ai/chat/sessions/${sessionId}`,
      method: 'get'
    })
  },

  // 创建新会话
  createSession(data) {
    return request({
      url: '/ai/chat/sessions',
      method: 'post',
      data: data
    })
  },

  // 删除会话
  deleteSession(sessionId) {
    return request({
      url: `/ai/chat/sessions/${sessionId}`,
      method: 'delete'
    })
  },

  // 清空会话历史
  clearSession(sessionId) {
    return request({
      url: `/ai/chat/sessions/${sessionId}/clear`,
      method: 'post'
    })
  },

  // 更新会话标题
  updateSessionTitle(sessionId, title) {
    return request({
      url: `/ai/chat/sessions/${sessionId}/title`,
      method: 'put',
      data: { title }
    })
  }
}

export default chatApi
