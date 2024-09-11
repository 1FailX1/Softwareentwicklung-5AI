<template>
  <div class="song-list">
    <h1>Song List</h1>
    <div v-if="confirmationMessage" class="confirmation-message">
      {{ confirmationMessage }}
    </div>
    <ul>
      <li v-for="song in songs" :key="song.id">
        <h2>{{ song.name }}</h2>
        <p><strong>Artist:</strong> {{ song.artist }}</p>
        <p><strong>Genre:</strong> {{ song.genre }}</p>
        <p><strong>Length:</strong> {{ song.length }} seconds</p>
        <div class="button-group">
          <button @click="editSong(song.id)">Edit</button>
          <button @click="deleteSong(song.id)">Delete</button>
        </div>
      </li>
    </ul>
    <button @click="addSong">Add New Song</button>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {useRoute, useRouter} from 'vue-router';
import editSong from "@/components/EditSong.vue";

export default {
  setup() {
    const songs = ref([]);
    const router = useRouter();
    const route = useRoute();
    const confirmationMessage = ref('');

    const fetchSongs = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/songs');
        console.log('API Response:', response.data);
        songs.value = response.data;
      } catch (error) {
        console.error('Error fetching songs:', error);
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