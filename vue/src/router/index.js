import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login',},
    { path: '/login', name: 'login',meta:{title:"登录"},component: () => import('../views/Login.vue')},
    { path: '/register', name: 'register',meta:{title:"注册"},component: () => import('../views/Register.vue')},
    { path: '/modify', name: 'modify', meta: { title: "修改密码" }, component: () => import('../views/modify.vue')},
    { path: '/home', name: 'home',meta:{title:"主页"},component: () => import('../views/Home.vue'),children:[
        {path: 'swiper', name: '',meta:{title:"轮播图管理"}, component: () => import('../views/swiper.vue') },
        {path: 'data', name: '',meta:{title:"数据分析"}, component: () => import('../views/data.vue') },
        {path: 'user', name: 'data', meta:{title:"员工界面"},component: () => import('../views/user.vue') },
        {path: 'song', name: '', meta:{title:'歌曲管理'},component: () => import('../views/song.vue')},
        {path: 'singer', name: '', meta:{title:'歌手管理'},component: () => import('../views/Singer.vue')},
        {path: 'songlist', name: '', meta:{title:'歌单管理'},component: () => import('../views/SongList.vue')},
        {path: 'message', name: '', meta:{title:'消息管理'},component: () => import('../views/Message.vue')},
        {path: 'consumer', name: 'consumerdata', meta:{title:"用户界面"},component: () => import('../views/consumer.vue') },
        {path: 'modify', name: 'modify', meta:{title:"修改密码"},component: () => import('../views/modify.vue')},
        {path: 'person', name: 'person', meta:{title:'修改个人信息'},component: () => import('../views/person.vue')},
      ]
    },

  ],
})

export default router
