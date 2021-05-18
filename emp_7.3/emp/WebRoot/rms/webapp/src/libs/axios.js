import axios from 'axios'

import config from './config'

// axios全局设置
axios.defaults.timeout = 20000
axios.defaults.baseURL = ''
axios.defaults.headers.common['x-requested-with'] = 'XMLHttpRequest'
axios.defaults.headers.common['meditor'] = 'true'
axios.defaults.headers.common['version'] = config.VERSION

// http请求拦截器
axios.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
})

/**
 * http响应拦截器
 * @param store
 */
axios.setResponse = store => {
  axios.interceptors.response.use(result => {
    const data = result.data
    if (data.code === 200 || data.state === '0') {
      return result
    } else if (data.code === 403) {
      window.location.reload()
    } else {
      store.commit('error', data)
      return Promise.reject(data)
    }
  }, error => {
    store.commit('loaded')
    store.commit('error', {msg: error.toString() || '加载失败'})
    return Promise.reject(error)
  })
}

/**
 * 封装$post请求
 * @param url
 * @param data
 * @returns {Promise}
 */
axios.$post = (url, data = {}) => {
  return new Promise((resolve, reject) => {
    axios.post(url, data).then(
      response => {
        resolve(response.data)
      },
      err => {
        reject(err)
      }
    )
  })
}

export default axios
