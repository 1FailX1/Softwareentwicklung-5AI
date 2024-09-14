<template>
  <div class="crud-artist">
    <h1>Edit Artist</h1>
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
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    const artist = ref({ name: '' });

    const fetchArtist = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/artists/${route.params.id}`);
        artist.value = response.data;
      } catch (error) {
        console.error('Error fetching artist:', error);
      }
    };

    const submitForm = async () => {
      try {
        await axios.put(`http://localhost:8080/api/artists/${route.params.id}`, artist.value);
        router.push({ path: '/artists', query: { message: 'Artist updated successfully!' } });
      } catch (error) {
        console.error('Error updating artist:', error);
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
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistCRUD.css';
</style>