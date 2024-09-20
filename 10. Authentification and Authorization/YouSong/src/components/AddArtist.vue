<template>
  <div class="crud-artist">
    <h1>Add New Artist</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Artist Name:</label>
        <input type="text" v-model="artist.name" id="name" class="spotify-input" />
        <span v-for="error in v$.name.$errors" :key="error.$uid" style="color: red">{{ error.$message }}</span>
        <p v-if="errors.name" class="error-message">{{ errors.name }}</p>
      </div>
      <div class="button-group">
        <button type="submit" class="btn-primary">Save</button>
        <button type="button" class="btn-secondary" @click="goBack">Go Back</button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import useVuelidate from '@vuelidate/core';
import { required } from '@vuelidate/validators';

export default {
  setup() {
    const router = useRouter();
    const artist = ref({
      name: ''
    });
    const errors = ref({
      name: ''
    });

    const rules = {
      name: { required },
    };

    const v$ = useVuelidate(rules, artist);

    const submitForm = async () => {
      const result = await v$.value.$validate();
      if (result) {
        try {
          const response = await axios.post('http://localhost:8080/api/artists', {
            name: artist.value.name
          });
          console.log('Artist added:', response.data);
          router.push({ path: '/artists', query: { message: 'Artist added successfully!' } });
        } catch (error) {
          if (error.response) {
            errors.value.name = error.response.data.name || '';
            console.error('Error adding artist:', error.response.data);
          } else {
            console.error('Error adding artist:', error.message);
          }
        }
      }
    };

    const goBack = () => {
      router.push('/artists');
    };

    return {
      artist,
      errors,
      submitForm,
      goBack,
      v$
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistCRUD.css';
</style>
