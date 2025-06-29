<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card elevation="4" class="pa-4">
          <v-card-title class="text-h5 text-center">登录</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="handleLogin" v-model="valid">
              <v-text-field
                v-model="username"
                label="用户名"
                prepend-icon="mdi-account"
                :rules="[v => !!v || '请输入用户名']"
                required
              />
              <v-text-field
                v-model="password"
                label="密码"
                prepend-icon="mdi-lock"
                type="password"
                :rules="[v => !!v || '请输入密码']"
                required
              />
              <v-btn
                color="primary"
                type="submit"
                block
                :loading="loading"
                :disabled="!valid"
                class="mt-4"
              >
                登录
              </v-btn>
            </v-form>
            <v-alert
              v-if="error"
              type="error"
              dense
              class="mt-4"
            >
              {{ error }}
            </v-alert>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
const { signIn } = useAuth();

const username = ref('');
const password = ref('');
const error = ref('');
const loading = ref(false);
const valid = ref(false);

const handleLogin = async () => {
  loading.value = true;
  error.value = '';
  try {
    const response = await signIn({
      username: username.value,
      password: password.value,
    }, {callbackUrl: '/'});
  } catch (err) {
    error.value = `登录失败：${err.message || '请稍后重试'}`;
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.v-card {
  border-radius: 8px;
}
</style>