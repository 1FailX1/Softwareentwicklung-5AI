<template>
  <div class="crud-song">
    <h1>Edit Song</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Song Name:</label>
        <input type="text" v-model="song.name" id="name" required />
      </div>
      <div class="form-group">
        <label for="artist">Artist:</label>
        <select v-model="song.artistId" id="artist" class="spotify-select" required>
          <option v-for="artist in artists" :key="artist.id" :value="artist.id">{{ artist.name }}</option>
        </select>
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
        <button type="submit" class="btn btn-primary">Save</button>
        <button type="button" class="btn btn-secondary" @click="goBack">Go Back</button>
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
    const song = ref({
      name: '',
      artistId: '',
      genre: '',
      length: ''
    });
    const artists = ref([]);

    const fetchArtists = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/artists');
        artists.value = response.data;
      } catch (error) {
        console.error('Error fetching artists:', error);
      }
    };

    const fetchSong = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/songs/${route.params.id}`);
        const fetchedSong = response.data;
        console.log(fetchedSong);
        song.value = {
          name: fetchedSong.name,
          artistId: fetchedSong.artist.id,
          genre: fetchedSong.genre,
          length: fetchedSong.length
        };
      } catch (error) {
        console.error('Error fetching song:', error);
      }
    };

    const submitForm = async () => {
      try {
        await axios.put(`http://localhost:8080/api/songs/${route.params.id}`, {
          name: song.value.name,
          artist: { id: song.value.artistId },
          genre: song.value.genre,
          length: song.value.length
        });
        router.push({ path: '/', query: { message: 'Song updated successfully!' } });
      } catch (error) {
        console.error('Error updating song:', error);
      }
    };

    const goBack = () => {
      router.push('/');
    };

    onMounted(() => {
      fetchArtists();
      fetchSong();
    });

    return {
      song,
      artists,
      submitForm,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/SongCRUD.css';
</style>