<template>
  <div class="login-page">
    <div class="login-box">
      <h2>Login</h2>
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" v-model="username" id="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="password" id="password" required />
        </div>
        <div class="button-group">
          <button type="submit" class="spotify-login-button">Login</button>
          <button type="button" class="spotify-home-button" @click="goBack">Home</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import router from "@/router/index.js";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

export default {
  setup() {
    const username = ref('');
    const password = ref('');
    const isAuthenticated = ref(false);
    const song = ref({
      title: '',
      artist: ''
    });

    const router = useRouter();

    const login = async () => {
      try {
        const response = await axios.post('http://localhost:8080/login', `username=${username.value}&password=${password.value}`, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          withCredentials: true // Ensure cookies are included in the request
        });
        if (response.status === 200 || response.status === 302) {
          isAuthenticated.value = true;
          sessionStorage.setItem('isAuthenticated', 'true');
          await router.push('/');
        }
      } catch (error) {
        console.error('Login failed:', error);
      }
    };

    onMounted(() => {
      isAuthenticated.value = sessionStorage.getItem('isAuthenticated') === 'true';
    });

    const goBack = () => {
      router.push('/');
    };

    return {
      username,
      password,
      isAuthenticated,
      song,
      login,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/LoginPage.css';
</style>