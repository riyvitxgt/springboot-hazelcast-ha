<template>
  <v-container>
    <v-row>
      <v-col>
        <h1 class="text-h4">欢迎进入管理系统</h1>
        <p class="mt-2">您已成功登录！</p>
        <v-btn color="primary" @click="handleLogout" class="mt-4">退出登录</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
const { signOut } = useAuth();

const handleLogout = async () => {
  await signOut({ callbackUrl: '/login' });
};

definePageMeta({
  layout: 'default', // 使用 default 布局
  middleware: ['sidebase-auth'],
});
</script>