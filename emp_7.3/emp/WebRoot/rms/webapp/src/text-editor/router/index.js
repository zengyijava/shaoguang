import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

const Editor = resolve => require(['../views/Editor'], resolve)
const Err404 = resolve => require(['../../libs/views/404'], resolve)

export default new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: [
    { path: '/404', component: Err404, hidden: true },
    {
      path: '/',
      component: Editor,
      name: 'editor'
    },
    { path: '*', redirect: '/404' }
  ]
})
