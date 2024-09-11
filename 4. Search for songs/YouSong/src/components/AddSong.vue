<template>
  <div class="crud-song">
    <h1>Add New Song</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Song Name:</label>
        <input type="text" v-model="song.name" id="name" required />
      </div>
      <div class="form-group">
        <label for="artist">Artist:</label>
        <input type="text" v-model="song.artist" id="artist" required />
      </div>
      <div class="form-group">
        <label for="genre">Genre:</label>
        <input type="text" v-model="song.genre" id="genre" required />
      </div>
      <div class="form-group">
        <label for="length">Length (seconds):</label>
        <input type="number" v-model="song.length" id="length" required />
      </div>
      <div class="button-group">
        <button type="submit" class="btn btn-primary">Add</button>
        <button type="button" class="btn btn-secondary" @click="goBack">Go Back</button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const router = useRouter();
    const song = ref({
      name: '',
      artist: '',
      genre: '',
      length: ''
    });

    const submitForm = async () => {
      try {
        const response = await axios.post('http://localhost:8080/api/songs', {
          name: song.value.name,
          artist: song.value.artist,
          genre: song.value.genre,
          length: song.value.length
        });
        console.log('Song added:', response.data);
        await router.push({path: '/', query: {message: 'Song added successfully!'}});
      } catch (error) {
        console.error('Error adding song:', error);
      }
    };

    const goBack = () => {
      router.push('/');
    };

    return {
      song,
      submitForm,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/SongCRUD.css';
</style>
