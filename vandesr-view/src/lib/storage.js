const setItem = function(key, val){
    window.sessionStorage.setItem(key, val)
}
const getItem = function(key){
    return window.sessionStorage.getItem(key);
}

export default {setItem, getItem}