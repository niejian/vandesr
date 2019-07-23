export function getToken() {
  return window.localStorage.getItem('user_token');
}

export function getMenus() {
  return window.localStorage.getItem('user_menus');
}