import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const CardNavBar = () => import(/* webpackChunkName: "card-navbar" */ '../views/CardNavBar')
const CardEditor = () => import(/* webpackChunkName: "card-editor" */ '../views/CardEditor')
const MediaNavBar = () => import(/* webpackChunkName: "media-navbar" */ '../../media/views/MediaNavBar')
const MediaEditor = () => import(/* webpackChunkName: "media-editor" */ '../../media/views/MediaEditor')
const MsgNavBar = () => import(/* webpackChunkName: "msg-navbar" */ '../../media/views/MsgNavBar')
const MsgEditor = () => import(/* webpackChunkName: "msg-editor" */ '../../media/views/MsgEditor')
const Err404 = () => import(/* webpackChunkName: "404" */ '../../libs/views/404')

export default new Router({
  routes: [
    {path: '/404', component: Err404, hidden: true},
    {
      path: '/',
      components: {
        navBar: CardNavBar,
        editor: CardEditor
      },
      name: 'editor'
    },
    {
      path: '/media',
      components: {
        navBar: MediaNavBar,
        editor: MediaEditor
      },
      name: 'media'
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
