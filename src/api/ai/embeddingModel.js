import request from '@/utils/request'

// 查询嵌入模型资源列表
export function listEmbeddingModel(query) {
  return request({
    url: '/ai/embeddingModel/list',
    method: 'get',
    params: query
  })
}

// 查询嵌入模型资源详细
export function getEmbeddingModel(id) {
  return request({
    url: '/ai/embeddingModel/' + id,
    method: 'get'
  })
}

// 新增嵌入模型资源
export function addEmbeddingModel(data) {
  return request({
    url: '/ai/embeddingModel',
    method: 'post',
    data: data
  })
}

// 修改嵌入模型资源
export function updateEmbeddingModel(data) {
  return request({
    url: '/ai/embeddingModel',
    method: 'put',
    data: data
  })
}

// 删除嵌入模型资源
export function delEmbeddingModel(id) {
  return request({
    url: '/ai/embeddingModel/' + id,
    method: 'delete'
  })
}

// 测试嵌入模型连接
export function testEmbeddingModel(id, data) {
  return request({
    url: '/ai/embeddingModel/test/' + id,
    method: 'post',
    data: data
  })
}
