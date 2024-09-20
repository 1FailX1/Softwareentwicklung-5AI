<template>
  <div class="song-list">
    <h1>Song List</h1>
    <div class="search-container">
      <input
          type="text"
          v-model="searchTerm"
          @input="() => searchSongs(0)"
          class="spotify-search-bar"
          placeholder="Search for a song..."
      />
      <button @click="() => searchSongs(0)" class="spotify-search-button">Search</button>
      <button @click="addSong" class="spotify-search-button">Add New Song</button>
      <router-link to="/artists" class="spotify-search-button">View Artists</router-link>
    </div>
    <div class="genre-buttons">
      <button
          v-for="genre in genreOptions"
          :key="genre"
          type="button"
          :class="{'selected': selectedGenres.includes(genre)}"
          @click="toggleGenre(genre)"
      >
        {{ genre }}
      </button>
    </div>
    <div v-if="confirmationMessage" class="confirmation-message">
      {{ confirmationMessage }}
    </div>
    <ul v-if="songs.length > 0">
      <li v-for="song in songs" :key="song.id">
        <h2>{{ song.name }}</h2>
        <p><strong>Artist:</strong> {{ song.artist.name }}</p>
        <p><strong>Genre:</strong> {{ song.genres.join(', ') }}</p>
        <p><strong>Length:</strong> {{ song.length }} seconds</p>
        <div class="button-group">
          <button @click="editSong(song.id)">Edit</button>
          <button @click="deleteSong(song.id)">Delete</button>
          <button @click="playSong(song)">Play</button>
        </div>
      </li>
    </ul>
    <p v-else>No songs can be found. Please adjust your search.</p>
    <div class="pagination">
      <button @click="fetchSongs(0)" :disabled="currentPage === 0">First</button>
      <button @click="fetchSongs(currentPage - 1)" :disabled="currentPage === 0">Previous</button>
      <button @click="fetchSongs(currentPage + 1)" :disabled="currentPage === totalPages - 1">Next</button>
      <button @click="fetchSongs(totalPages - 1)" :disabled="currentPage === totalPages - 1">Last</button>
      <p class="pagination-text">Page {{ currentPage+1 }} of {{ totalPages }}</p>
    </div>
    <div v-if="currentSong" class="music-player">
      <div class="music-player-content">
        <h2>Now Playing: {{ currentSong.name }}</h2>
        <audio ref="audioPlayer" :src="currentSong.data_url" controls></audio>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import { genreOptions } from '../assets/genres';

export default {
  setup() {
    const songs = ref([]);
    const searchTerm = ref('');
    const selectedGenres = ref([]);
    const currentPage = ref(0);
    const totalPages = ref(0);
    const currentSong = ref(null);
    const currentSongETag = ref('');
    const router = useRouter();
    const route = useRoute();
    const confirmationMessage = ref('');

    const fetchSongs = async (page = 0) => {
      try {
        const response = await axios.get(`http://localhost:8080/api/songs/paginated`, {
          params: {
            page: page
          }
        });
        songs.value = response.data.content;
        currentPage.value = response.data.page.number;
        totalPages.value = response.data.page.totalPages;
      } catch (error) {
        console.error('Error fetching songs:', error);
      }
    };

    const searchSongs = async (page = 0) => {
      try {
        const response = await axios.get(`http://localhost:8080/api/songs/search/genres-and-term`, {
          params: {
            searchTerm: searchTerm.value,
            genres: selectedGenres.value.join(','),
            page: page,
            size: 5
          }
        });
        songs.value = response.data.content;
        currentPage.value = response.data.page.number;
        totalPages.value = response.data.page.totalPages;
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
        fetchSongs(currentPage.value);
        confirmationMessage.value = 'Song deleted successfully!';
      } catch (error) {
        console.error('Error deleting song:', error);
      }
    };

    const playSong = async (song) => {
      try {
        const response = await axios.get(`http://localhost:8080/api/songs/${song.id}`);
        currentSong.value = response.data;
        currentSongETag.value = response.headers['etag'];

        const audioPlayer = document.querySelector('audio');

        if (audioPlayer) {
          audioPlayer.pause();
          audioPlayer.currentTime = 0;
          audioPlayer.src = currentSong.value.data_url;

          audioPlayer.addEventListener('canplaythrough', () => {
            audioPlayer.play();
          }, { once: true });

          audioPlayer.load();
        }
      } catch (error) {
        console.error('Error fetching song:', error);
      }
    };

    const toggleGenre = (genre) => {
      const index = selectedGenres.value.indexOf(genre);
      if (index === -1) {
        selectedGenres.value.push(genre);
      } else {
        selectedGenres.value.splice(index, 1);
      }
      searchSongs(0);
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
      confirmationMessage,
      currentPage,
      totalPages,
      fetchSongs,
      currentSong,
      playSong,
      toggleGenre,
      selectedGenres,
      genreOptions
    };
  }
};
</script>

<style scoped>
@import '../assets/SongList.css';
</style>