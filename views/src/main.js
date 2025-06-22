import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import store from "./store/store.js";

const app = createApp(App)

app.use(router)
app.use(ElementPlus,{
    locale:zhCn
});
app.mount('#app');
app.use(store);
