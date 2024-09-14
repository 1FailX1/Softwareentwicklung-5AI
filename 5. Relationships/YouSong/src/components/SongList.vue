<template>
  <div class="song-list">
    <h1>Song List</h1>
    <div class="search-container">
      <input
          type="text"
          v-model="searchTerm"
          @input="searchSongs"
          class="spotify-search-bar"
          placeholder="Search for a song..."
      />
      <button @click="searchSongs" class="spotify-search-button">Search</button>
      <button @click="addSong" class="spotify-search-button">Add New Song</button>
      <router-link to="/artists" class="spotify-search-button">View Artists</router-link>
    </div>
    <div v-if="confirmationMessage" class="confirmation-message">
      {{ confirmationMessage }}
    </div>
    <ul v-if="songs.length > 0">
      <li v-for="song in songs" :key="song.id">
        <h2>{{ song.name }}</h2>
        <p><strong>Artist:</strong> {{ song.artist.name }}</p>
        <p><strong>Genre:</strong> {{ song.genre }}</p>
        <p><strong>Length:</strong> {{ song.length }} seconds</p>
        <div class="button-group">
          <button @click="editSong(song.id)">Edit</button>
          <button @click="deleteSong(song.id)">Delete</button>
        </div>
      </li>
    </ul>
    <p v-else>No songs can be found. Please adjust your search.</p>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

export default {
  setup() {
    const songs = ref([]);
    const searchTerm = ref('');
    const router = useRouter();
    const route = useRoute();
    const confirmationMessage = ref('');

    const fetchSongs = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/songs');
        songs.value = response.data;
        console.log(songs.value);
      } catch (error) {
        console.error('Error fetching songs:', error);
      }
    };

    const searchSongs = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/songs/search?searchTerm=${searchTerm.value}`);
        songs.value = response.data;
      } catch (error) {
        console.error('Error searching songs:', error);
      }
    };

    const addSong = () => {
      router.push('/add-song');
    };
    const editSong = (id) => {
      router.push(`/edit-song/${id}`);
    };
    const deleteSong = async (id) => {
      try {
        await axios.delete(`http://localhost:8080/api/songs/${id}`);
        songs.value = songs.value.filter(song => song.id !== id);
        confirmationMessage.value = 'Song deleted successfully!';
      } catch (error) {
        console.error('Error deleting song:', error);
      }
    };

    onMounted(() => {
      fetchSongs();
      if (route.query.message) {
        confirmationMessage.value = route.query.message;
      }
    });

    return {
      songs,
      addSong,
      searchTerm,
      searchSongs,
      editSong,
      deleteSong,
      confirmationMessage
    };
  }
};
</script>

<style scoped>
@import '../assets/SongList.css';
</style>