import request from '@/utils/request'

// 查询向量数据库列表
export function listVectorDb(query) {
  return request({
    url: '/system/vectorDb/list',
    method: 'get',
    params: query
  })
}

// 查询向量数据库详细
export function getVectorDb(id) {
  return request({
    url: '/system/vectorDb/' + id,
    method: 'get'
  })
}

// 新增向量数据库
export function addVectorDb(data) {
  return request({
    url: '/system/vectorDb',
    method: 'post',
    data: data
  })
}

// 修改向量数据库
export function updateVectorDb(data) {
  return request({
    url: '/system/vectorDb',
    method: 'put',
    data: data
  })
}

// 删除向量数据库
export function delVectorDb(id) {
  return request({
    url: '/system/vectorDb/' + id,
    method: 'delete'
  })
}

// 测试向量数据库连接
export function testVectorDb(id) {
  return request({
    url: '/system/vectorDb/test/' + id,
    method: 'post'
  })
}
