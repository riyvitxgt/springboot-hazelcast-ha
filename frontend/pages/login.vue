<template>
  <LoginForm />
</template>

<script setup lang="ts">
definePageMeta({
  layout: 'login', // 使用 login 布局
  middleware: ['sidebase-auth'],
});
</script>