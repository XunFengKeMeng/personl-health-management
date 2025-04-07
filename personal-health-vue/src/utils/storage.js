/**
 * 用于管理sessionStorage的工具模块
 */
const TOKEN_KEY = "token"
const INFO_KEY = "health-info";
const ACTIVE_PATH_KEY = "active_key"
/**
 * 获取token
 * 
 * @returns 返回token
 */
export function getToken () {
    return sessionStorage.getItem(TOKEN_KEY);
}

/**
 * 设置token
 */
export function setToken (token) {
    if (!token) {
        console.warn("Token is empty or undefined.");
        return;
    }
    sessionStorage.setItem(TOKEN_KEY, token);
}

/**
 * 获取健康信息
 * 
 * @returns 用户的健康信息
 */
export function getHealthInfo () {
    return sessionStorage.getItem(INFO_KEY);
}

/**
 * 设置健康信息
 */
export function setHealthInfo (info) {
    if (!info) {
        console.warn("Health info is empty or undefined.");
        return;
    }
    sessionStorage.setItem(INFO_KEY, info);
}

/**
 * 清除 token 和所有 sessionStorage 数据
 */
export function clearToken () {
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(INFO_KEY);
    sessionStorage.removeItem(ACTIVE_PATH_KEY);
}

/**
 * 获取路径
 * 
 * @returns 
 */
export function getActivePath () {
    return sessionStorage.getItem(ACTIVE_PATH_KEY);
}

/**
 * 设置路径
 */
export function setActivePath (path) {
    if (!path) {
        console.warn("Active path is empty or undefined.");
        return;
    }
    sessionStorage.setItem(ACTIVE_PATH_KEY, path);
}