import request from '@/utils/request'

// 查询AI模型资源列表
export function listModelResource(query) {
  return request({
    url: '/ai/modelResource/list',
    method: 'get',
    params: query
  })
}

// 查询AI模型资源详细
export function getModelResource(id) {
  return request({
    url: '/ai/modelResource/' + id,
    method: 'get'
  })
}

// 新增AI模型资源
export function addModelResource(data) {
  return request({
    url: '/ai/modelResource',
    method: 'post',
    data: data
  })
}

// 修改AI模型资源
export function updateModelResource(data) {
  return request({
    url: '/ai/modelResource',
    method: 'put',
    data: data
  })
}

// 删除AI模型资源
export function delModelResource(id) {
  return request({
    url: '/ai/modelResource/' + id,
    method: 'delete'
  })
}

// 测试AI模型连接
export function testModelResource(id) {
  return request({
    url: '/ai/modelResource/test/' + id,
    method: 'post'
  })
}
