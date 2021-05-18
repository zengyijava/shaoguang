import axios from 'axios'
import store from './mediaStore'
// http请求拦截器
axios.interceptors.request.use(config => {
  return config
}, error => {
  return Promise.reject(error)
})
// http响应拦截器
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
  store.commit('loaded', null, {root: true})
  store.commit('error', {msg: error.toString() || '加载失败'})
  return Promise.reject(error)
})

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */
export function fetch (url, params = {}) {
  return new Promise((resolve, reject) => {
    axios
      .get(url, {
        params: params
      })
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */
export function post (url, data = {}) {
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
