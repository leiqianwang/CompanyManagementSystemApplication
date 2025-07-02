import request from '@/utils/request'

// 查询向量数据库资源列表
export function listVectorDb(query) {
  return request({
    url: '/ai/vector-database/list',
    method: 'get',
    params: query
  })
}

// 查询向量数据库资源详细
export function getVectorDb(id) {
  return request({
    url: '/ai/vector-database/' + id,
    method: 'get'
  })
}

// 新增向量数据库资源
export function addVectorDb(data) {
  return request({
    url: '/ai/vector-database',
    method: 'post',
    data: data
  })
}

// 修改向量数据库资源
export function updateVectorDb(data) {
  return request({
    url: '/ai/vector-database',
    method: 'put',
    data: data
  })
}

// 删除向量数据库资源
export function delVectorDb(id) {
  return request({
    url: '/ai/vector-database/' + id,
    method: 'delete'
  })
}

// 测试向量数据库连接
export function testVectorDb(id) {
  return request({
    url: '/ai/vector-database/test/' + id,
    method: 'post'
  })
}

// 导出向量数据库资源
export function exportVectorDb(query) {
  return request({
    url: '/ai/vector-database/export',
    method: 'post',
    params: query
  })
}
