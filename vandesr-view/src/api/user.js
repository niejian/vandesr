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

/**
 * 编辑用户角色信息
 * @param {*} data 
 */
export function updateUserRole(data) {
  return request({
    url: '/userRole/updateUserRole',
    method: 'post',
    data
  })
}

/**
 * 获取系统菜单信息
 * @param {data} data 
 */
export function getSystemMenus(data) {
  return request({
    url: '/user/menu/getSystemMenus',
    method: 'post',
    data
  })
}

/**
 * 获取指定菜单的详细信息
 * @param {id:''，menuCode: xxx, menuName:xxx} data 
 */
export function getMenuInfo(data) {
  return request({
    url: '/user/menu/getMenuInfo',
    method: 'post',
    data
  })
}

export function addOrUpdateMenuInfo(data) {
  return request({
    url: '/user/menu/handleAddOrUpdate',
    method: 'post',
    data
  })
}

/**
 * 删除菜单信息
 * @param {menuId:xx} data 
 */
export function delMenu(data) {
  return request({
    url: '/user/menu/delMenu',
    method: 'post',
    data
  })
}

/**
 *通过用户角色获取到对应的菜单信息
 * @param {roleId:xx} data 
 */
export function getMenuByRoleId(data) {
  return request({
    url: '/user/roleMenu/getMenuByRoleId',
    method: 'post',
    data
  })
}

/**
 *
 * 绑定菜单信息
 * @param {roleId:xxx, data:[{menuId,menuCode, menuName}]}
 */
export function saveRoleMenu(data) {
  return request({
    url: '/user/roleMenu/saveRoleMenu',
    method: 'post',
    data
  })
}