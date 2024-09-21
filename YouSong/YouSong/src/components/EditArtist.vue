<template>
  <div class="crud-artist">
    <div class="crud-artist-box">
      <h1>Add New Artist</h1>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="name">Artist Name:</label>
          <input type="text" v-model="artist.name" id="name" class="spotify-input"/>
          <span v-for="error in v$.name.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
          <p v-if="errors.name" class="error-message">{{ errors.name }}</p>
        </div>
        <div class="button-group">
          <button type="submit" class="spotify-button btn-primary">Save</button>
          <button type="button" class="spotify-button btn-secondary" @click="goBack">Go Back</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {useRouter, useRoute} from 'vue-router';
import axios from 'axios';
import useVuelidate from '@vuelidate/core';
import {required} from '@vuelidate/validators';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    const artist = ref({name: ''});
    const errors = ref({
      name: ''
    });
    const rules = {
      name: {required},
    };

    const v$ = useVuelidate(rules, artist);

    const fetchArtist = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/artists/${route.params.id}`);
        artist.value = response.data;
      } catch (error) {
        console.error('Error fetching artist:', error);
      }
    };

    const submitForm = async () => {
      const result = await v$.value.$validate();
      if (result) {
        try {
          await axios.put(`http://localhost:8080/api/artists/${route.params.id}`, artist.value);
          router.push({path: '/artists', query: {message: 'Artist updated successfully!'}});
        } catch (error) {
          //backend validation
          if (error.response) {
            errors.value.name = error.response.data.name || '';
            console.error('Error adding song:', error.response.data);
          } else {
            console.error('Error adding song:', error.message);
          }
        }
      }
    };

    const goBack = () => {
      router.push('/artists');
    };

    onMounted(() => {
      fetchArtist();
    });

    return {
      artist,
      submitForm,
      goBack,
      errors,
      v$
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistCRUD.css';
</style>