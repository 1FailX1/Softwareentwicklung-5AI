<template>
  <div class="crud-song">
    <h1>Edit Song</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Name:</label>
        <input type="text" v-model="song.name" id="name" />
        <span v-for="error in v$.name.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.name" class="error-message">{{ errors.name }}</p>
      </div>
      <div class="form-group">
        <label for="artist">Artist:</label>
        <select v-model="song.artist_id" id="artist">
          <option v-for="artist in artists" :key="artist.id" :value="artist.id">{{ artist.name }}</option>
        </select>
        <span v-for="error in v$.artist_id.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.artist_id" class="error-message">{{ errors.artist }}</p>
      </div>
      <div class="form-group">
        <label for="genre">Genre:</label>
        <input type="text" v-model="song.genre" id="genre" />
        <span v-for="error in v$.genre.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.genre" class="error-message">{{ errors.genre }}</p>
      </div>
      <div class="form-group">
        <label for="length">Length (seconds):</label>
        <input type="number" v-model="song.length" id="length" />
        <span v-for="error in v$.length.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.length" class="error-message">{{ errors.length }}</p>
      </div>
      <div class="form-group">
        <label for="file">Audio File:</label>
        <div v-if="isFileAttached">
          <p style="color: #1DB954">File already attached. Upload a new file to replace it.</p>
        </div>
        <div v-else>
          <p>No file attached. Upload a new file.</p>
        </div>
        <input type="file" @change="handleFileUpload" id="file" class="spotify-input" />
      </div>
      <div class="button-group">
        <button type="submit" class="btn btn-primary">Save</button>
        <button type="button" class="btn btn-secondary" @click="goBack">Go Back</button>
      </div>
    </form>
  </div>
</template>

<script>
import {ref, onMounted, computed} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import useVuelidate from '@vuelidate/core';
import { required, minValue } from '@vuelidate/validators';

export default {
  setup() {
    const router = useRouter();
    const route = useRoute();
    const song = ref({
      name: '',
      artist_id: '',
      genre: '',
      length: '',
      data_url: ''
    });
    const artists = ref([]);
    const errors = ref({
      name: '',
      artist_id: '',
      genre: '',
      length: '',
      data_url: ''
    });

    const rules = {
      name: { required },
      artist_id: { required },
      genre: { required },
      length: { required, minValue: minValue(1) },
      data_url: { required }
    };

    const v$ = useVuelidate(rules, song);

    const handleFileUpload = (event) => {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        song.value.data_url = e.target.result;
      };
      reader.readAsDataURL(file);
    };

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
        song.value = response.data;
        song.value.artist_id = response.data.artist.id
      } catch (error) {
        console.error('Error fetching song:', error);
      }
    };

    const submitForm = async () => {
      const result = await v$.value.$validate();
      if (result) {
        try {
          const response = await axios.put(`http://localhost:8080/api/songs/${route.params.id}`, {
            name: song.value.name,
            artist: song.value.artist_id ? { id: song.value.artist_id } : null,
            genre: song.value.genre,
            length: song.value.length,
            data_url: song.value.data_url
          });
          console.log('Song updated:', response.data);
          router.push({ path: '/', query: { message: 'Song updated successfully!' } });
        } catch (error) {
          if (error.response) {
            errors.value.name = error.response.data.name || '';
            errors.value.artist = error.response.data.artist || '';
            errors.value.genre = error.response.data.genre || '';
            errors.value.length = error.response.data.length || '';
            console.error('Error updating song:', error.response.data);
          } else {
            console.error('Error updating song:', error.message);
          }
        }
      }
    };

    const goBack = () => {
      router.push('/');
    };

    const isFileAttached = computed(() => !!song.value.data_url);

    onMounted(() => {
      fetchArtists();
      fetchSong();
    });

    return {
      song,
      artists,
      errors,
      handleFileUpload,
      submitForm,
      goBack,
      isFileAttached,
      v$
    };
  }
};
</script>

<style scoped>
@import '../assets/SongCRUD.css';
</style>