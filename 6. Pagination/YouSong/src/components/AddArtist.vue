<template>
  <div class="crud-artist">
    <h1>Add New Artist</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Artist Name:</label>
        <input type="text" v-model="artist.name" id="name" required class="spotify-input" />
      </div>
      <div class="button-group">
        <button type="submit" class="spotify-button btn-primary">Save</button>
        <button type="button" class="spotify-button btn-secondary" @click="goBack">Go Back</button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from "axios";

export default {
  setup() {
    const router = useRouter();
    const artist = ref({
      name: ''
    });

    const submitForm = async () => {
      try {
        const response = await axios.post('http://localhost:8080/api/artists', {
          name: artist.value.name
        });
        console.log('Artist added:', artist.value);
        router.push({path: '/artists', query: {message: 'Artist added successfully!'}});
      } catch (error) {
        console.error('Error adding artist:', error);
      }
    };

    const goBack = () => {
      router.push('/');
    };

    return {
      artist,
      submitForm,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistCRUD.css';
</style>
