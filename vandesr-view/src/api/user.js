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