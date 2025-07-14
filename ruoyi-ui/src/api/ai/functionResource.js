import request from '@/utils/request'

// 查询Function资源列表
export function listFunctionResource(query) {
  return request({
    url: '/ai/functionResource/list',
    method: 'get',
    params: query
  })
}

// 查询Function资源详细
export function getFunctionResource(id) {
  return request({
    url: '/ai/functionResource/' + id,
    method: 'get'
  })
}

// 新增Function资源
export function addFunctionResource(data) {
  return request({
    url: '/ai/functionResource',
    method: 'post',
    data: data
  })
}

// 修改Function资源
export function updateFunctionResource(data) {
  return request({
    url: '/ai/functionResource',
    method: 'put',
    data: data
  })
}

// 删除Function资源
export function delFunctionResource(id) {
  return request({
    url: '/ai/functionResource/' + id,
    method: 'delete'
  })
}

// 导出Function资源
export function exportFunctionResource(query) {
  return request({
    url: '/ai/functionResource/export',
    method: 'post',
    params: query
  })
}

// Function资源状态修改
export function changeFunctionResourceStatus(id, isEnabled) {
  const data = {
    id,
    isEnabled
  }
  return request({
    url: '/ai/functionResource/changeStatus',
    method: 'put',
    data: data
  })
}

// 根据编号获取Function资源详细信息
export function getFunctionResourceByCode(functionCode) {
  return request({
    url: '/ai/functionResource/getByCode/' + functionCode,
    method: 'get'
  })
}

// 检查Function编号是否唯一
export function checkFunctionCodeUnique(functionCode) {
  return request({
    url: '/ai/functionResource/checkFunctionCodeUnique',
    method: 'get',
    params: { functionCode }
  })
}
