import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

const MediaNavBar = () => import(/* webpackChunkName: "media-navbar" */ '../views/MediaNavBar')
const MediaEditor = () => import(/* webpackChunkName: "media-editor" */ '../views/MediaEditor')
const MsgNavBar = () => import(/* webpackChunkName: "msg-navbar" */ '../views/MsgNavBar')
const MsgEditor = () => import(/* webpackChunkName: "msg-editor" */ '../views/MsgEditor')
const Err404 = () => import(/* webpackChunkName: "404" */ '../../libs/views/404')

export default new Router({
  routes: [
    {path: '/404', component: Err404, hidden: true},
    {
      path: '/',
      components: {
        navBar: MediaNavBar,
        editor: MediaEditor
      },
      name: 'editor'
    },
    {
      path: '/msg',
      components: {
        navBar: MsgNavBar,
        editor: MsgEditor
      },
      name: 'msg'
    },
    {path: '*', redirect: '/'}
  ]
})
