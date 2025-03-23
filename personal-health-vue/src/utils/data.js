/**
 * 计算给定时间与当前时间的相对时间差
 * 
 * @param {*} dateString 指定时间
 * @returns 一个描述时间差的字符串
 */
export function timeAgo (dateString) {
    const now = new Date();
    const date = new Date(dateString);
    const secondsPast = (now.getTime() - date.getTime()) / 1000;
    if (secondsPast < 60) {
        return `${Math.floor(secondsPast)} 秒前`;
    } else if (secondsPast < 3600) {
        return `${Math.floor(secondsPast / 60)} 分钟前`;
    } else if (secondsPast <= 86400) {
        return `${Math.floor(secondsPast / 3600)} 小时前`;
    } else {
        const daysPast = Math.floor(secondsPast / 86400);
        if (daysPast === 1) {
            return '1 天前';
        } else {
            return `${daysPast} 天前`;
        }
    }
}