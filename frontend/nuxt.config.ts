export default defineNuxtConfig({
  compatibilityDate: '2025-06-23',
  devtools: { enabled: true },
  modules: [
    'vuetify-nuxt-module',
    '@sidebase/nuxt-auth',
  ],
  vuetify: {
    vuetifyOptions: {
      theme: {
        defaultTheme: 'light',
      },
    },
    moduleOptions: {
      treeshaking: true,
      styles: 'sass',
    },
  },
  auth: {
    baseURL: 'http://localhost:8080', // 后端 API 基础地址
    provider: {
      type: 'local',
      endpoints: {
        signIn: { path: '/api/auth/login', method: 'post' },
        getSession: { path: '/api/auth/user', method: 'get' },
        signOut: { path: '/api/auth/logout', method: 'post' },
      },
      token: {
        signInResponseTokenPointer: '/data/token',
        headerName: 'Authorization',
        type: 'Bearer',
      },
      pages: {
        login: '/login', // 明确指定登录页面路径
      },
    },
    globalAppMiddleware: {
      isEnabled: false, // 禁用全局中间件
    },
  },
  css: ['vuetify/styles', '~/assets/css/main.css'],
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080', // 一致的后端 API 地址
    },
  },
});