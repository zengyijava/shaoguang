export default {
  /**
   * 格式化年月日
   * @param date
   * @param fmt
   * @returns {*}
   */
  formatDate: (date, fmt = 'yyyy-MM-dd hh:mm:ss') => {
    /**
     * 左边补零
     * @param str
     * @returns {string}
     */
    function padLeftZero (str) {
      return ('00' + str).substr(str.length)
    }
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(
        RegExp.$1,
        (date.getFullYear() + '').substr(4 - RegExp.$1.length)
      )
    }
    let o = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'h+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds()
    }
    for (let k in o) {
      if (new RegExp(`(${k})`).test(fmt)) {
        let str = o[k] + ''
        fmt = fmt.replace(
          RegExp.$1,
          RegExp.$1.length === 1 ? str : padLeftZero(str)
        )
      }
    }
    return fmt
  },
  /**
   * 将时间转时分秒并补0
   * @param value
   * @param isMS
   * @returns {string}
   */
  formatTime: (value, isMS = false) => {
    /**
     * 补零
     * @param str
     * @returns {string}
     * @constructor
     */
    function padZero (str) {
      return new RegExp(/^\d$/g).test(str) ? `0${str}` : str
    }

    if (!value) return '00:00'
    let ms = parseInt(value)
    if (isMS) {
      ms = parseInt(value) / 1000
    }
    let hours, mins, seconds
    let result = ''
    seconds = parseInt(ms % 60)
    mins = parseInt((ms % 3600) / 60)
    hours = parseInt(ms / 3600)

    if (hours) {
      result = `${padZero(hours)}:${padZero(mins)}:${padZero(seconds)}`
    } else {
      result = `${padZero(mins)}:${padZero(seconds)}`
    }
    return result
  }
}
