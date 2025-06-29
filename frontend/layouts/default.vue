<template>
  <v-app>
    <v-navigation-drawer v-model="drawer" app>
      <v-list>
        <v-list-item
          prepend-icon="mdi-home"
          title="首页"
          value="home"
          to="/"
        />
        <v-list-item
          prepend-icon="mdi-logout"
          title="退出登录"
          value="logout"
          @click="handleLogout"
        />
      </v-list>
    </v-navigation-drawer>
    <v-app-bar app color="primary" dark>
      <v-app-bar-nav-icon @click="drawer = !drawer" />
      <v-app-bar-title>管理系统</v-app-bar-title>
    </v-app-bar>
    <v-main>
      <v-container>
        <slot />
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const { signOut } = useAuth();

const drawer = ref(true); // 控制导航栏显示/隐藏

const handleLogout = async () => {
  await signOut({ callbackUrl: '/login' });
};
</script>

<style scoped>
</style>