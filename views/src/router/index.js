import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/home',},
    {path: '/home', name: 'home', component: () => import('../views/Home.vue'),children:[
        {path: 'search', name: 'Search',meta:{title:"发现"}, component: () => import('../views/search.vue'),children:[
            {path: 'searchsongs', name: 'searchsongs',meta:{title:"单曲"}, component: () => import('../views/Searchsongs.vue')},
            {path: 'searchsinger', name: 'searchsinger',meta:{title:"歌手"}, component: () => import('../views/searchsinger.vue')},
            {path: 'searchsonglist', name: 'searchsonglist',meta:{title:"歌单"}, component: () => import('../views/Searchsonglist.vue')},
        ]},
        {path: 'message', name: 'message',meta:{title:"信息"}, component: () => import('../views/Message.vue'),children:[
                {path: 'content', name: 'content',meta:{title:"信息内容"}, component: () => import('../views/Content.vue')},
                ]},
        {path: 'singer', name: 'singer',meta:{title:"音乐人"}, component: () => import('../views/Singer.vue')},
        {path: 'space', name: 'space',meta:{title:"我的"}, component: () => import('../views/space.vue')},
        {path: 'collect', name: 'collect',meta:{title:"收藏单曲"}, component: () => import('../views/collect.vue'),children:[
                {path: 'collectsong', name: 'collectsong',meta:{title:"收藏单曲列表"}, component: () => import('../views/CollectSong.vue')}]},
        {path: 'rank', name: 'rank',meta:{title:"排行榜"}, component: () => import('../views/Ranking.vue')},
        {path: 'homeview', name: 'homeview',meta:{title:"首页展示"}, component: () => import('../views/Homeview.vue')},
        {path: 'songlists', name: 'songlists',meta:{title:"歌单列表"}, component: () => import('../views/SongLists.vue')},
        {path: 'songlist', name: 'songlist',meta:{title:"当前歌单"}, component: () => import('../views/SongList.vue'),children:[
                {path: 'songlistselect', name: 'songlistselect',meta:{title:"歌曲列表"}, component: () => import('../views/Songlistselect.vue')},
                {path: 'songlistcontent', name: 'songlistcontent',meta:{title:"歌单评论"}, component: () => import('../views/Songlistcontent.vue')},
            ]},
            {
                path: '/chat/:id',
                name: 'ChatPage',
                meta: { title: '聊天页面' },
                component: () => import('../views/ChatPage.vue')
            }
      ]
    },
  ],
})
export default router
