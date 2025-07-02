import request from '@/utils/request'

// 查询函数资源列表
export function listFunctionResource(query) {
  return request({
    url: '/system/functionResource/list',
    method: 'get',
    params: query
  })
}

// 查询函数资源详细
export function getFunctionResource(id) {
  return request({
    url: '/system/functionResource/' + id,
    method: 'get'
  })
}

// 新增函数资源
export function addFunctionResource(data) {
  return request({
    url: '/system/functionResource',
    method: 'post',
    data: data
  })
}

// 修改函数资源
export function updateFunctionResource(data) {
  return request({
    url: '/system/functionResource',
    method: 'put',
    data: data
  })
}

// 删除函数资源
export function delFunctionResource(id) {
  return request({
    url: '/system/functionResource/' + id,
    method: 'delete'
  })
}

// 测试函数资源
export function testFunctionResource(data) {
  return request({
    url: '/system/functionResource/test',
    method: 'post',
    data: data
  })
}
