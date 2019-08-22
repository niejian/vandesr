import request from '@/utils/request'
export function login(data) {
  // console.log(request)
  //return post('http://localhost:8088/user/login', data)
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

/**
 * 获取角色信息
 * @param {} data 
 */
export function getRoleList(data) {
  return request({
    url: '/user/role/list',
    method: 'post',
    data
  })
}

/**
 * 编辑角色信息
 * @param {角色信息} data 
 */
export function updateRole(data) {
  return request({
    url: '/user/role/update',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息
 * @param {查询条件} data 
 */
export function getUsers(data) {
  return request({
    url: '/user/getUsers',
    method: 'post',
    data
  })
}

/**
 * 更新用户状态
 * @param {} data 
 */
export function deleteUser(data) {
  return request({
    url: '/user/deleteUser',
    method: 'post',
    data
  })  
}

/**
 * 获取用户角色信息
 * @param {userId} data 
 */
export function getUserRoleInfo(data) {
  return request({
    url: '/userRole/list',
    method: 'post',
    data
  })
}