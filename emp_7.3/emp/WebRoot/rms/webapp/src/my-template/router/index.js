import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
const MyTemplateList = () => import(/* webpackChunkName: "rx-template-list" */ '../views/MyTemplateList')
const Access = () => import(/* webpackChunkName: "rx-access" */ '../views/Access')
const AccessList = () => import(/* webpackChunkName: "rx-access-list" */ '../views/AccessList')
const Err404 = () => import(/* webpackChunkName: "404" */ '../../libs/views/404')
const AccessForm = () => import(/* webpackChunkName: "rx-access-form" */ '../components/AccessForm')
const Personalise = () => import(/* webpackChunkName: "rx-personalise" */ '../components/Personalise')
const Outcome = () => import(/* webpackChunkName: "rx-outcome" */ '../components/Outcome')

export default new Router({
  routes: [
    {path: '/404', component: Err404, hidden: true},
    {
      path: '/',
      component: MyTemplateList,
      name: 'index',
      meta: { path: '列表' }
    },
    {
      path: '/access',
      component: Access,
      name: 'access',
      meta: { path: '资源获取' },
      children: [
        {
          path: '/access',
          component: AccessForm,
          name: 'access-form',
          meta: { path: '获取相同资源' }
        },
        {
          path: '/personalise',
          component: Personalise,
          name: 'personalise',
          meta: { path: '获取个性化资源' }
        }
      ]
    },
    {
      path: '/outcome',
      component: Outcome,
      name: 'outcome',
      meta: { path: '获取资源' }
    },
    {
      path: '/access-list',
      component: AccessList,
      name: 'access-list',
      meta: { path: '获取记录' }
    },
    {path: '*', redirect: '/404'}
  ]
})
