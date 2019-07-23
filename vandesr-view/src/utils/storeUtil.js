export function addSessionData(key, data) {
  window.sessionStorage.setItem(key, data);
}
export function removeSesssionData(key) {
  window.sessionStorage.removeItem(key)
}

export function getSessionData(key) {
  let data = window.sessionStorage.getItem(key);

  if (data && isJsonString(data)) {
    // 判断是不是json
    data = JSON.parse(data)
  }

  return data;
}

export function addStorageData(key, data) {
  window.localStorage.setItem(key, JSON.stringify(data));
}
export function removeStorageData(key) {
  window.localStorage.removeItem(key)
}
export function getStorageData(key) {
  let data = window.sessionStorage.getItem(key);
  if (data && isJsonString(data)) {
    data = JSON.parse(data)
  }

  return data;
}

function isJsonString(str) {
  try {
      if (typeof JSON.parse(str) == "object") {
          return true;
      }
  } catch(e) {
  }
  return false;
}