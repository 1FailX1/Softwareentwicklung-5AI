<template>
  <div class="song-list">
    <h1>Song List</h1>
    <ul>
      <li v-for="song in songs" :key="song.id">
        <h2>{{ song.name }}</h2>
        <p><strong>Artist:</strong> {{ song.artist }}</p>
        <p><strong>Genre:</strong> {{ song.genre }}</p>
        <p><strong>Length:</strong> {{ song.length }} seconds</p>
      </li>
    </ul>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
  setup() {
    const songs = ref([]);

    const fetchSongs = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/songs');
        console.log('API Response:', response.data); // Log the API response
        songs.value = response.data;
      } catch (error) {
        console.error('Error fetching songs:', error);
      }
    };

    onMounted(fetchSongs);

    return {
      songs
    };
  }
};
</script>

<style scoped>
@import '../assets/SongList.css';
</style>