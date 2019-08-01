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