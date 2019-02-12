/**
 * 判断字符串是否为空
 * @param  data 
 */
export function isNullStr(data){
    if(null == data || '' == data){
        return true;
    }

    return false;
}

export function isEmptyArr(data){
    if(null == data || typeof(data) == 'undefined' || data.length <= 0){
        return true;
    }

    return false;
}

export default isNullStr