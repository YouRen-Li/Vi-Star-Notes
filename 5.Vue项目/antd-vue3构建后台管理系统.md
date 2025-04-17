# Antd-Vue3 构建后台管理系统

## 前言

随着前端 Vue3 技术发展和生态的完善，越来越多企业使用 Vue3 技术栈搭建后台管理系统，本课程采用 Vue3 生态的 antd-vue 热门组件库，帮助开发者快速构建后台管理系统模板。  

### 课程收获

1. 最新 Vue3 前端技术栈应用
2. 详解 ESLint + Prettier 团队编码规范
3. Antd-Vue 组件库主题定制&布局搭建
4. 动态路由菜单和权限控制
5. 等...

## Vue3 项目初始化

Vue3 官网：https://cn.vuejs.org/

### 初始化 Vue3

初始化安装

```sh
npm create vue@latest
```

选择模板功能

![1700133049909](assets/1700133049909.png)

### VS Code 扩展安装

推荐安装扩展：`.vscode/extensions.json`

- 输入 `@recommended` 可一键安装推荐拓展

![1700122731585](assets/1700122731585.png)

- "Vue.volar",
  - Vue.js 语言插件，提供 Vue 文件代码提示等功能。
- "Vue.vscode-typescript-vue-plugin",
  - Vue.js 文件中 TypeScript 的增强支持。
- "dbaeumer.vscode-eslint",
  - ESLint 插件，用于在编写 JavaScript 和 TypeScript 时检查和修复代码质量问题。
- "esbenp.prettier-vscode"
  - Prettier 是一个代码格式化工具，保持一致的代码风格。

### 项目文件清理

1. 删除 src 所有文件
2. 新建 App.vue 和 main.js

### 准备基础代码

`App.vue`

```vue
<script setup>
//
</script>

<template>
  <h1>Hello vue3👍</h1>
</template>
```

`main.js`

```js
import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App)

app.mount('#app')
```

`vite.config.js`

 开发服务器启动时，自动在浏览器中打开应用程序。

```diff
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
+  server: {
+    open: true, // 自动打开浏览器
+  },
})
```

### 团队编码规范

eslint + prettier 配置参考，可根据项目情况定制。

#### 组件文件名规范

新建文件 `views/Login/index.vue`，结果文件名报错，配置 ESlint 规则为允许 `index.vue` 命名。

同时配置 jsx 语法支持，用于动态生成侧栏菜单。

`.eslintrc.cjs`

```diff
/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-prettier/skip-formatting'
  ],
  parserOptions: {
    ecmaVersion: 'latest',
+    // jsx 支持
+    ecmaFeatures: {
+      jsx: true,
+      tsx: true,
+    },
  },
+  rules: {
+    'vue/multi-word-component-names': [
+      'warn',
+      {
+        ignores: ['index']
+      }
+    ]
+  }
}
```

#### 统一添加末尾分号(可选)

`.prettierrc.json`

```diff
{
  "$schema": "https://json.schemastore.org/prettierrc",
  "semi": false,
  "tabWidth": 2,
  "singleQuote": true,
  "printWidth": 100,
-  "trailingComma": "none",
+  "trailingComma": "all",
+  "endOfLine": "auto"
}
```

`.eslintrc.cjs`

温馨提示：prettierrc 的配置复制一份到 eslintrc 中，用于避免插件冲突。

```diff
/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-prettier/skip-formatting',
  ],
  parserOptions: {
    ecmaVersion: 'latest',
    // jsx 支持
    ecmaFeatures: {
      jsx: true,
      tsx: true,
    },
  },
  rules: {
    'vue/multi-word-component-names': [
      'warn',
      {
        ignores: ['index'],
      },
    ],
+    'prettier/prettier': [
+      'warn',
+      {
+        semi: false,
+        tabWidth: 2,
+        singleQuote: true,
+        printWidth: 100,
+        trailingComma: 'all',
+        endOfLine: 'auto',
+      },
+    ],
  },
}

```

#### VS Code工作区设置

新建文件 `.vscode/setting.json`，保存时自动运行 eslint 

```json
{
  // 启用 eslint  
  "eslint.enable": true,
  // 保存时为编辑器运行
  "editor.codeActionsOnSave": {
    // 保存时运行 eslint
    "source.fixAll.eslint": true
  },
  // 处理以下后缀名文件
  "eslint.options": {
    "extensions": [
      ".js",
      ".vue",
      ".jsx",
      ".tsx"
    ]
  },
}
```

## antd-vue 组件库

官方文档：https://www.antdv.com/docs/vue/introduce-cn

### antdv 组件库

#### 安装依赖

安装组件库

```sh
npm install ant-design-vue@4.x --save
```

基本使用

```vue
<template>
  <h1>antd 组件库</h1>
  <a-button type="primary">按钮</a-button>
</template>
```

注意：此时发现组件库按钮不生效，若全量导入组件库体积太大，建议配置按需引入组件。

#### 自动按需引入组件

安装依赖

```sh
npm install unplugin-vue-components -D
```

vite.config.js

```diff
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
+ import Components from 'unplugin-vue-components/vite'
+ import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
+    Components({
+      resolvers: [
+        // 自动按需引入 antd 组件
+        AntDesignVueResolver({
+          importStyle: false, // css in js
+        }),
+      ],
+    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    open: true,
  },
})
```

### antdv 图标库

#### 安装和基本使用

安装依赖

```sh
npm install @ant-design/icons-vue --save
```

使用图标

```vue
<script setup>
import { StepForwardOutlined } from '@ant-design/icons-vue'
import { h } from 'vue'
</script>

<template>
  <h1>antd 图标演示</h1>
  <!-- 图标基础用法 -->
  <StepForwardOutlined />
  <!-- 带图标的按钮 icon 属性 -->
  <a-button type="primary" :icon="h(StepForwardOutlined)">按钮图标</a-button>
  <!-- 带图标的按钮 icon 插槽 -->
  <a-button type="primary">
    <template #icon>
      <StepForwardOutlined />
    </template>
    按钮图标
  </a-button>
</template>
```

#### 图标全局按需导入

`components/Icons/index.js`

```js
import {
  HomeOutlined,
  PartitionOutlined,
  SettingOutlined,
} from '@ant-design/icons-vue'

// 以上图标都需要全局注册
const icons = [
  HomeOutlined,
  PartitionOutlined,
  SettingOutlined,
]

export default {
  install(app) {
    // 全局注册引入的所有图标，需在 main.js 中使用 app.use(icons) 注册
    icons.forEach((item) => {
      app.component(item.displayName, item)
    })
  },
}
```

`main.js`

```diff
import { createApp } from 'vue'
import App from './App.vue'
+ import Icons from './components/Icons'

const app = createApp(App)

+ app.use(Icons)
app.mount('#app')
```

### 组件库代码提示配置和 @ 别名映射

配置后使用 antd 组件库有代码提示，@ 导入的文件也

`jsconfig.json`

```json
{
  "compilerOptions": {
    "types": [
      // 添加 antdv 的类型声明文件，方便代码提示
      "ant-design-vue/typings/global.d.ts"
    ],
    // 配置路径别名映射，识别类型，方便代码提示
    "baseUrl": "./",
    "paths": {
      "@/*": [
        "src/*"
      ]
    }
  }
}
```

### antdv 定制主题和国际化

antdv 默认主题色时蓝色，默认语言为英文。

- 国际化： https://www.antdv.com/docs/vue/i18n-cn

- 定制主题： https://www.antdv.com/docs/vue/customize-theme-cn
- ConfigProvider 全局化配置: https://www.antdv.com/components/config-provider-cn

`App.vue`

```diff
<script setup>
+ import zhCN from 'ant-design-vue/es/locale/zh_CN'

+ const theme = {
+   token: {
+     colorPrimary: '#e15536',
+   },
+ }
</script>

<template>
+   <a-config-provider :theme="theme" :locale="zhCN">
      <h1>定制主题和国际化</h1>
      <!-- 主色按钮 -->
      <a-button type="primary">主色按钮</a-button>
      <!-- 分页器 -->
      <a-pagination :total="50" show-size-changer />
+   </a-config-provider>
</template>
```

## CSS 预处理器和全局样式

### 安装依赖

 考虑到不同的团队习惯把 sass 和 less 都安装上，按需使用。

```sh
npm install sass less -D
```

### 样式全局变量

styles/var.less

```less
:root {
  --color-primary: #e15536;
}
```

styles/main.less

```less
@import './var.less';

// 全局样式
body {
  font-size: 14px;
  color: #333;
  margin: 0;
}

a {
  color: inherit;
  text-decoration: none;
  &:hover {
    color: var(--color-primary);
  }
}

h1,
h2,
h3,
h4,
h5,
h6,
p,
ul,
ol {
  margin: 0;
  padding: 0;
}

// 用于修改 nprogress 进度条颜色
#nprogress {
  .bar {
    background-color: var(--color-primary) !important;
  }
  .peg {
    box-shadow:
      0 0 10px var(--color-primary),
      0 0 5px var(--color-primary) !important;
  }
}
```

`main.js`

```diff
import { createApp } from 'vue'
import App from './App.vue'
import Icons from './components/Icons'
+ import '@/styles/main.less'

const app = createApp(App)

app.use(Icons)
app.mount('#app')
```

`App.vue`

```diff
<script setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'

const theme = {
  token: {
    colorPrimary: '#e15536',
  },
}
</script>

<template>
  <a-config-provider :theme="theme" :locale="zhCN">
    <h1>定制主题和国际化</h1>
    <!-- 主色按钮 -->
    <a-button type="primary">主色按钮</a-button>
    <!-- 分页器 -->
    <a-pagination :total="50" show-size-changer />
+   <a href="#">超链接悬停时为主色</a>
  </a-config-provider>
</template>

```

## antdv 布局和项目路由

### antd 布局

`src\views\Layout\index.vue`

```vue
<template>
  <a-layout has-sider class="layout">
    <!-- 侧边栏 -->
    <div>侧边栏</div>
    <a-layout class="main">
      <!-- 页头 -->
      <div>页头</div>
      <!-- 主体 -->
      <a-layout-content class="content">
        <div>内容</div>
        <div>内容</div>
        <div>内容</div>
        <div>内容</div>
        <div>内容</div>
        <div>内容</div>
      </a-layout-content>
      <!-- 页脚 -->
      <div>页脚</div>
    </a-layout>
  </a-layout>
</template>

<style scoped lang="scss">
.layout {
  min-height: 100vh;
  background-color: #ccc;
}

.main {
  margin-left: 200px;
  background-color: #ddd;
}

.content {
  background-color: #f4f4f4;
  padding: 20px;
  overflow-y: auto;
  margin-top: 60px;
}
</style>
```

### vue-router 路由

`src\router\index.js`

```js
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      meta: { title: '主页', icon: 'HomeOutlined' },
      component: () => import('@/views/Layout/index.vue'),
    },
    {
      path: '/login',
      name: 'login',
      meta: { title: '登录页' },
      component: () => import('@/views/Login/index.vue'),
    },

  ],
})

export default router
```

`App.vue`

```diff
<script setup>
import zhCN from 'ant-design-vue/es/locale/zh_CN'

const theme = {
  token: {
    colorPrimary: '#e15536',
  },
}
</script>

<template>
  <a-config-provider :theme="theme" :locale="zhCN">
+    <router-view />
  </a-config-provider>
</template>
```

### 加载进度条和标题设置

安装依赖

```vue
npm install nprogress
```

应用

```diff
import { createRouter, createWebHistory } from 'vue-router'
+ import NProgress from 'nprogress'
+ import 'nprogress/nprogress.css'

+ NProgress.configure({
+   showSpinner: false,
+ })

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      meta: {
        title: '主页',
      },
      component: () => import('@/views/Layout/index.vue'),
    },
    {
      path: '/login',
      name: 'login',
      meta: {
        title: '登录页',
      },
      component: () => import('@/views/Login/index.vue'),
    },
  ],
})

+ router.beforeEach(() => {
+   // 进度条开始
+   NProgress.start()
+ })

+ // 全局的后置导航
+ router.afterEach((to) => {
+   // 进度条结束
+   NProgress.done()
+   // 动态设置标题
+   document.title = `${to.meta.title || import.meta.env.VITE_APP_TITLE}`
+ })

export default router
```

### 环境变量

- `.env.development`

```sh
VITE_APP_TITLE = 后台管理系统 - dev
VITE_APP_BASE_URL = https://slwl-api.itheima.net/manager
```

- `.env.production`

```
VITE_APP_TITLE = 后台管理系统
VITE_APP_BASE_URL = https://slwl-api.itheima.net/manager
```

## 基于路由生成菜单

### JSX 版侧栏菜单-静态结构

侧栏菜单：`src\views\Layout\components\AppSideBar.vue`

```vue
<script lang="jsx">
import { defineComponent, ref, h, resolveComponent } from 'vue'

export default defineComponent({
  name: 'SideBarItem',
  setup() {
    const openKeys = ref(['1']) // 展开的一级菜单 key
    const selectedKeys = ref(['11']) // 高亮的二级菜单 key

    return () => (
      <a-layout-sider theme="light" width="200" class="sidebar">
        {/* logo */}
        <h1 class="logo">
          <RouterLink to="/"> Logo </RouterLink>
        </h1>
        {/* 菜单 */}
        <a-menu
          v-model:openKeys={openKeys.value}
          v-model:selectedKeys={selectedKeys.value}
          theme="light"
          mode="inline"
        >
          <a-sub-menu title={'基础数据管理'} key="1" icon={h(resolveComponent('HomeOutlined'))}>
            <a-menu-item key="11">机构管理</a-menu-item>
            <a-menu-item key="12">机构作业范围</a-menu-item>
            <a-menu-item key="13">运费管理</a-menu-item>
          </a-sub-menu>
          <a-sub-menu title={'车辆管理'} key="2" icon={h(resolveComponent('PartitionOutlined'))}>
            <a-menu-item key="21">车型管理</a-menu-item>
            <a-menu-item key="32">车辆列表</a-menu-item>
            <a-menu-item key="33">回车管理</a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
    )
  },
})
</script>

<style scoped lang="scss">
.sidebar {
  // 侧栏菜单固定定位
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 99;
  height: 100vh;
  overflow-y: auto;
}
.logo {
  height: 150px;
  display: flex;
  justify-content: center;
  align-items: center;

  &-img {
    width: 152px;
    height: 113px;
  }
}
</style>
```

### 项目路由参考

#### 新建静态路由

 `src\router\constantRoutes.js`

```js
export const constantRoutes = [
  {
    component: () => import('@/views/Login/index.vue'),
    name: 'login',
    path: '/login',
    meta: { title: '登录' },
    hidden: true, // 侧边栏隐藏该路由
  },
  {
    component: () => import('@/views/Layout/index.vue'),
    name: 'dashboard',
    path: '/',
    redirect: '/dashboard',
    meta: { title: '工作台', icon: 'HomeOutlined', order: 0 },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard/index.vue'),
        meta: { title: '数据面板', parent: 'dashboard' },
      },
    ],
  },
  // 捕获所有路由或 404 Not found 路由
  {
    path: '/:pathMatch(.*)',
    component: () => import('@/views/NotFound/index.vue'),
    meta: { title: '页面不存在' },
    hidden: true, // 侧边栏隐藏该路由
  },
]
```

#### 新建动态路由

`src\router\asyncRoutes.js` ，基于模块生成路由树。

```js
// 官方文档：https://cn.vitejs.dev/guide/features.html#glob-import
const modules = import.meta.glob('./modules/*.js', { eager: true })

// 格式化模块
function formatModules(_modules, result) {
  // 遍历模块
  Object.keys(_modules).forEach((key) => {
    // 获取模块
    const defaultModule = _modules[key].default
    // 模块存在
    if (defaultModule) {
      // 把模块放入结果数组
      result.push(defaultModule)
    }
  })
  // 返回结果数组
  return result.sort((a, b) => a.meta?.order - b.meta?.order)
}

// 根据文件生成路由树
export const asyncRoutes = formatModules(modules, [])
```

#### 新建路由模块

- 新建多个路由模块1：`src\router\modules\base.js`

```js
export default {
  name: 'base',
  path: '/base',
  component: () => import('@/views/Layout/index.vue'),
  redirect: '/base/department',
  meta: { title: '基础数据管理', icon: 'BarChartOutlined', order: 1 },
  children: [
    {
      name: 'base-department',
      path: '/base/department',
      meta: { title: '机构管理', parent: 'base' },
      component: () => import('@/views/Base/Department/index.vue'),
    },
    {
      name: 'base-departwork',
      path: '/base/departwork',
      meta: { title: '机构作业范围', parent: 'base' },
      component: () => import('@/views/Base/DepartWork/index.vue'),
    },
    {
      name: 'base-freight',
      path: '/base/freight',
      meta: { title: '运费管理', parent: 'base' },
      component: () => import('@/views/Base/Freight/index.vue'),
    },
  ],
}
```

- 新建多个路由模块2：`src\router\modules\base.js`

```js
export default {
  name: 'business',
  component: () => import('@/views/Layout/index.vue'),
  path: '/business',
  redirect: '/business/orderlist',
  meta: { title: '业务管理', icon: 'ScheduleOutlined', order: 4 },
  children: [
    {
      name: 'business-orderlist',
      path: '/business/orderlist',
      meta: { title: '运单管理', parent: 'business' },
      component: () => import('@/views/Business/WayBill/index.vue'),
    },
    {
      name: 'business-businesslist',
      path: '/business/businesslist',
      meta: { title: '订单管理', parent: 'business' },
      component: () => import('@/views/Business/Order/index.vue'),
    },
  ],
}
```

#### 导入并应用路由

删除掉旧路由，替换成新的路由。

```diff
+ import { constantRoutes } from './constantRoutes'  // 导入静态路由
+ import { asyncRoutes } from './asyncRoutes'        // 导入动态路由

const router = createRouter({
   history: createWebHashHistory(),
   routes: [
+     ...constantRoutes,    // 静态路由
+     ...asyncRoutes,       // 动态路由
   ],
})
```

- 注意：侧栏菜单需要用到图标，记得在 `src\components\Icons\index.js` 全局导入。

### 基于路由生成菜单

```diff
<script lang="jsx">
+ import { defineComponent, h, resolveComponent, computed, ref } from 'vue'
+ import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'SideBarItem',
  setup() {
    const openKeys = ref([]) // 展开的一级菜单 key
    const selectedKeys = ref([]) // 高亮的二级菜单 key

+    const router = useRouter() // 获取路由实例
+    // 获取路由表
+    const routes = computed(() => {
+      // 隐藏 hidden: true 的路由
+      return router.options.routes.filter((v) => !v.hidden)
+    })

+    // 渲染侧栏菜单的函数
+    const renderSubMenu = () => {
+      // 递归渲染侧栏菜单
+      function travel(_route, nodes = []) {
+        // _route 是一个数组，里面是路由对象
+        if (_route) {
+          // 遍历路由对象
+          _route.forEach((element) => {
+            const { icon, title } = element.meta
+
+            const node =
+              element.children && element.children.length > 0 ? (
+                // 一级菜单：渲染 标题 和 图标
+                <a-sub-menu title={title} key={element.name} icon={h(resolveComponent(icon))}>
+                  {/* 如果有子路由，递归渲染 */}
+                  {travel(element.children)}
+                </a-sub-menu>
+              ) : (
+                // 二级菜单：渲染 路由链接 和 标题
+                <a-menu-item key={element.path}>
+                  <router-link to={element.path}>{title}</router-link>
+                </a-menu-item>
+              )
+            nodes.push(node)
+          })
+        }
+        return nodes
+      }
+      return travel(routes.value)
+    }

    return () => (
      <a-layout-sider theme="light" width="200" class="sidebar">
        {/* logo */}
        <h1 class="logo">
          <RouterLink to="/"> Logo </RouterLink>
        </h1>
        {/* 菜单 */}
        <a-menu
          v-model:openKeys={openKeys.value}
          v-model:selectedKeys={selectedKeys.value}
          theme="light"
          mode="inline"
        >
+          {renderSubMenu()}
        </a-menu>
      </a-layout-sider>
    )
  },
})
</script>
```

### 高亮侧栏菜单

监听路由切换，展开并高亮对应的菜单项

```diff
<script lang="jsx">
import { useRouter } from 'vue-router'
+ import { watch } from 'vue'

export default defineComponent({
  name: 'SideBarItem',
  setup() {
    // ...省略
    const router = useRouter() // 获取路由实例
    
+   // 监听路由变化，更新选中的菜单
+   watch(
+     () => router.currentRoute.value,
+     (route) => {
+       // 设置一级菜单高亮
+       openKeys.value = [route.meta?.parent]
+       // 设置二级菜单高亮
+       selectedKeys.value = [route.path]
+     },
+     // 立即执行
+     { immediate: true },
+   )

   // ...省略
  },
})
</script>
```

## Pinia 状态管理和持久化

Vue3 推荐的 Store 状态管理是 pinia (Vuex5)，项目中一般会按需配置 Store 的持久化。

Pinia官方：https://pinia.vuejs.org/zh/

持久化存储：https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/config.html#paths

### 安装依赖

```sh
npm install pinia-plugin-persistedstate
```

### 新建用户模块

`src\store\modules\account.js`

```js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAccountStore = defineStore(
  'account', // store id
  () => {
    // store state
    const role = ref('admin')
    const token = ref('token-string')

    // store actions
    const changeRole = (payload) => {
      role.value = payload
      location.reload()
    }

    // return store state and actions
    return {
      role,
      token,
      changeRole,
    }
  },
  {
    // 持久化存储 role 和 token
    persist: {
      paths: ['role', 'token'],
    },
  },
)
```

### 配置持久化存储

`src\store\index.js`

```js
// Vue3 推荐状态管理是 pinia (Vuex5)
import { createPinia } from 'pinia'
// 导入持久化存储插件
import persist from 'pinia-plugin-persistedstate'

// 创建 store 实例
const store = createPinia()
// 使用持久化插件
store.use(persist)

// 导出 store 实例
export default store

// 导出所有模块
export * from './modules/account'
```

### 全局应用 Store

`src\main.js`

```diff
import { createApp } from 'vue'
import App from './App.vue'
import './styles/main.less'

import Icons from './components/Icons'
import router from './router'
+ import store from './store'

const app = createApp(App)

app.use(Icons)
app.use(router)
+ app.use(store)
app.mount('#app')
```

### 测试 Store 数据

```vue
<script setup>
import { useAccountStore } from '@/store'

// 获取用户 Store
const accountStore = useAccountStore()
</script>

<template>
  <h3>Store 角色: {{ accountStore.role }}</h3>
  <button @click="accountStore.changeRole('admin')">切换角色 admin</button>
  <button @click="accountStore.changeRole('user')">切换角色 user</button>
</template>
```


## Mock 模拟数据

### 安装依赖

```sh
npm install mockjs vite-plugin-mock -D
```

### 项目配置

- 配置 mock 服务： `vite.config.js`

```diff
import { defineConfig } from 'vite'
// ...省略
+ import { viteMockServe } from 'vite-plugin-mock'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    // ...省略
+    viteMockServe({
+      mockPath: './src/mock',
+      enable: true,
+      watchFiles: false,
+    }),
  ],
  // ...省略
})
```

### 参考例子

- 新建 mock 数据文件：`src\mock\user.js`

```js
export default [
  // 模拟接口1
  {
    url: '/api/user/info', // 请求地址
    method: 'get', // 请求方法
    response: () => {
      // 返回数据
      return {
        code: 200,
        msg: 'ok',
        data: {
          // MockJS 数据占位符定义：http://mockjs.com/examples.html#DPD
          id: '@id', // 随机 id
          name: '黑马程序员', // 普通信息
        },
      }
    },
  },
  // ...省略
]
```

- 在 vue 文件中使用，先使用原生 fetch 获取数据，可根据项目需要换成 axios 。

```vue
<script setup>
import { ref } from 'vue'

const userInfo = ref()
const getUserInfo = async () => {
  // 通过 fetch 获取用户信息(mock)
  const response = await fetch('/api/user/info')
  // 获取响应数据
  const res = await response.json()
  // 保存用户信息
  userInfo.value = res.data
}
</script>

<template>
  <button @click="getUserInfo()">获取 mock 用户信息</button>
  <a-divider />
  <div>用户信息：{{ userInfo }}</div>
</template>
```

注意事项：mock 数据更新后不生效，需要重启服务 `npm run dev`。

## request 封装

axios官网：https://www.axios-http.cn/docs/intro

### 安装依赖

```js
npm install axios
```

### 封装 axios 工具

新建文件：`src\utils\request.js`

```js
import axios from 'axios'
import { message } from 'ant-design-vue'
import { useAccountStore } from '@/store'

// 导入路由
import router from '@/router'

// 创建 axios 实例
export const http = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  timeout: 10000, // timeout
})

// axios 请求拦截器
// https://axios-http.com/zh/docs/interceptors
http.interceptors.request.use(
  (config) => {
    const { token } = useAccountStore()
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)

// axios 响应拦截器
http.interceptors.response.use(
  (response) => {
    // 提取响应数据
    const data = response.data
    // 如果是下载文件(图片等)，直接返回数据
    if (data instanceof ArrayBuffer) {
      return data
    }
    // code 为非 200 是抛错，可结合自己业务进行修改
    const { code, msg } = data
    if (code !== 200) {
      message.error(msg)
      return Promise.reject(msg)
    }
    // 响应数据
    return data
  },
  (error) => {
    const response = error.response
    const status = response && response.status
    // 和后端约定的3种状态码会跳转登录，可结合自己业务进行修改
    if ([400, 401, 403].includes(status)) {
      if (status === 400) {
        message.warning('权限不足')
      } else if (status === 401) {
        message.warning('登录状态过期')
      }
      // 清理用户信息 token，重置权限路由等，可结合自己业务进行修改
      // TODO...
      // 跳转登录页
      router.push('/login')
      return Promise.reject(error)
    } else {
      return Promise.reject(error)
    }
  },
)
```

### 参考例子

```vue
<script setup>
import { ref } from 'vue'
import { http } from '@/utils/request'

const userInfo = ref()
const getUserInfo = async () => {
  // 通过 axios 获取用户信息(注意：请求 mock 需拼接成 http 开头的路径)
  const res = await http.get(`${location.origin}/api/user/info`)
  userInfo.value = res.data
}
</script>

<template>
  <button @click="getUserInfo()">获取 mock 用户信息</button>
  <a-divider />
  <div>用户信息：{{ userInfo }}</div>
</template>
```

## 权限控制

权限控制常见有两种业务需求：权限指令、权限路由(菜单)。

### 权限指令

基于权限控制按需展示某些功能模块，相当于结合了权限控制的 `v-if` 指令。

#### 权限指令封装

`src\directive\modules\permission.js`

```js
import { useAccountStore } from '@/store'

// 权限校验方法
function checkPermission(el, { value }) {
  // 获取用户 Store
  const accountStore = useAccountStore()
  // 获取用户 Store 的角色，可根据业务情况进行调整
  const currentRole = accountStore.role

  // 传入的权限值要求是一个数组
  if (Array.isArray(value) && value.length > 0) {
    // 判断用户角色是否有权限
    const hasPermission = value.includes(currentRole)
    // 没有权限则删除当前dom
    if (!hasPermission) el.remove()
  } else {
    throw new Error(`格式错误，正确用法 v-permission="['admin','employee']"`)
  }
}

export default {
  mounted(el, binding) {
    checkPermission(el, binding)
  },
  updated(el, binding) {
    checkPermission(el, binding)
  },
}
```

#### 指令入口管理

`src\directive\index.js`

```js
import permission from './modules/permission'

export default {
  install(app) {
    // 注册全局指令
    app.directive('permission', permission)
  },
}

```

#### 全局注册指令

```diff
import { createApp } from 'vue'
import App from './App.vue'
import './styles/main.less'

import Icons from './components/Icons'
import router from './router'
import store from './store'
+ import directive from './directive'


const app = createApp(App)

app.use(Icons)
app.use(router)
app.use(store)
+ app.use(directive)
app.mount('#app')
```

#### 参考例子

```diff
<script setup>
import { useAccountStore } from '@/store'

// 获取用户 Store
const accountStore = useAccountStore()
</script>

<template>
  <h3>Store 角色: {{ accountStore.role }}</h3>
  <button @click="accountStore.changeRole('admin')">切换角色 admin</button>
  <button @click="accountStore.changeRole('user')">切换角色 user</button>
  <a-divider />
+  <a-button v-permission="['admin']" type="primary">admin 权限按钮</a-button>
+  <a-button v-permission="['user']" type="primary" ghost> user 权限按钮</a-button>
</template>
```

### 权限路由(菜单)

业务较为复杂，请参考素材中的源码解读。

权限路由常见业务为：

1. 获取后端返回的用户菜单(权限)
2. 基于返回的菜单(权限)，查找匹配的路由
3. 注册成路由，添加路由导航守卫等
4. 基于新注册的路由，生成后台管理系统的菜单
5. 退出登录，清理用户信息的同时，清理权限路由

## 感言

感谢各位小伙伴能学习到这里，自己动手丰衣足食。

当然 Vue3 生态在国内非常活跃，有很多优秀的后台管理系统模板，作为最后给大家的分享。

### Vue3 生态后台管理系统分享

[GitHub 排名](https://github.com/search?q=vue3+admin&type=repositories&s=stars&o=desc)

| 开源仓库                                                     | 预览地址                                                  | 组件库                                                     | Star 数量                                                    |
| ------------------------------------------------------------ | --------------------------------------------------------- | ---------------------------------------------------------- | ------------------------------------------------------------ |
| [vbenjs/vue-vben-admin](https://github.com/vbenjs/vue-vben-admin) | [预览地址](https://vben.vvbin.cn/)                        | [Ant-Design-Vue](https://antdv.com/docs/vue/introduce-cn/) | <img src="https://img.shields.io/github/stars/vbenjs/vue-vben-admin" /> |
| [flipped-aurora/gin-vue-admin](https://github.com/flipped-aurora/gin-vue-admin) | [预览地址](https://demo.gin-vue-admin.com/)               | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/flipped-aurora/gin-vue-admin" /> |
| [chuzhixin/vue-admin-better](https://github.com/chuzhixin/vue-admin-better) | [预览地址](https://vue-admin-beautiful.com/shop-vite)     | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/chuzhixin/vue-admin-better" /> |
| [pure-admin/vue-pure-admin](https://github.com/pure-admin/vue-pure-admin) | [预览地址](https://yiming_chang.gitee.io/vue-pure-admin/) | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/pure-admin/vue-pure-admin" /> |
| [honghuangdc/soybean-admin](https://github.com/honghuangdc/soybean-admin) | [预览地址](https://admin.soybeanjs.cn/)                   | [Naive UI](https://www.naiveui.com/zh-CN/os-theme)         | <img src="https://img.shields.io/github/stars/honghuangdc/soybean-admin" /> |
| [HalseySpicy/Geeker-Admin](https://github.com/HalseySpicy/Geeker-Admin) | [预览地址](https://admin.spicyboy.cn/#/login)             | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/HalseySpicy/Geeker-Admin" /> |
| [jekip/naive-ui-admin](https://github.com/jekip/naive-ui-admin) | [预览地址](http://naive-ui-admin.vercel.app/)             | [Naive UI](https://www.naiveui.com/zh-CN/os-theme)         | <img src="https://img.shields.io/github/stars/jekip/naive-ui-admin" /> |
| [yangzongzhuan/RuoYi-Vue3](https://github.com/yangzongzhuan/RuoYi-Vue3) | [预览地址](https://vue.ruoyi.vip/)                        | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/yangzongzhuan/RuoYi-Vue3" /> |
| [un-pany/v3-admin-vite](https://github.com/un-pany/v3-admin-vite/) | [预览地址](https://un-pany.github.io/v3-admin-vite/)      | [element-plus](https://element-plus.org/zh-CN/)            | <img src="https://img.shields.io/github/stars/un-pany/v3-admin-vite" /> |
| [buqiyuan/vue3-antdv-admin](https://github.com/buqiyuan/vue3-antdv-admin) | [预览地址](https://buqiyuan.gitee.io/vue3-antdv-admin/)   | [Ant-Design-Vue](https://antdv.com/docs/vue/introduce-cn/) | <img src="https://img.shields.io/github/stars/buqiyuan/vue3-antdv-admin" /> |
| [arco-design/arco-design-pro-vue](https://github.com/arco-design/arco-design-pro-vue) | [预览地址](https://vue-pro.arco.design/)                  | [arco.design-字节跳动](https://arco.design/)               | <img src="https://img.shields.io/github/stars/arco-design/arco-design-pro-vue" /> |

