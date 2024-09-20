<template>
  <div class="artist-page">
    <div class="artist-box">
      <h1>ARTIST LIST</h1>
      <ul v-if="artists.length > 0">
        <li v-for="artist in artists" :key="artist.id">
          <h2>{{ artist.name }}</h2>
          <button @click="editArtist(artist.id)">Edit</button>
          <button @click="deleteArtist(artist.id)">Delete</button>
        </li>
      </ul>
      <p v-else>No artists found.</p>
      <div class="button-group">
        <button @click="addArtist" id="add-artist-button">Add New Artist</button>
        <button @click="goBack" id="go-back-button">Go Back</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
  setup() {
    const artists = ref([]);
    const router = useRouter();

    const fetchArtists = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/artists');
        artists.value = response.data;
      } catch (error) {
        console.error('Error fetching artists:', error);
      }
    };

    const addArtist = () => {
      router.push('/add-artist');
    };

    const editArtist = (id) => {
      router.push(`/edit-artist/${id}`);
    };

    const goBack = () => {
      router.push('/');
    };

    const deleteArtist = async (id) => {
      try {
        await axios.delete(`http://localhost:8080/api/artists/${id}`);
        artists.value = artists.value.filter(artist => artist.id !== id);
      } catch (error) {
        console.error('Error deleting artist:', error);
      }
    };

    onMounted(() => {
      fetchArtists();
    });

    return {
      artists,
      addArtist,
      editArtist,
      deleteArtist,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistList.css';
</style>
