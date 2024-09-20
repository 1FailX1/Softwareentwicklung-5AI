<template>
  <div class="crud-song">
    <div class="crud-song-box">
      <h1>Add Song</h1>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="name">Song Name:</label>
          <input type="text" v-model="song.name" id="name"/>
          <span v-for="error in v$.name.$errors" :key="error.$uid" class="error-message">
          {{ error.$message }}
        </span>
          <p v-if="errors.name" class="error-message">{{ errors.name }}</p>
        </div>
        <div class="form-group">
          <label for="artist">Artist:</label>
          <select v-model="song.artist_id" id="artist" class="spotify-select">
            <option v-for="artist in artists" :key="artist.id" :value="artist.id">{{ artist.name }}</option>
          </select>
          <span v-for="error in v$.artist_id.$errors" :key="error.$uid" class="error-message">
          {{ error.$message }}
        </span>
          <p v-if="errors.artist_id" class="error-message">{{ errors.artist_id }}</p>
        </div>
        <div class="form-group">
          <label for="length">Length (seconds):</label>
          <input type="number" v-model="song.length" id="length"/>
          <span v-for="error in v$.length.$errors" :key="error.$uid" class="error-message">
          {{ error.$message }}
        </span>
          <p v-if="errors.length" class="error-message">{{ errors.length }}</p>
        </div>
        <div class="form-group">
          <label>Genres:</label>
          <div class="genre-buttons">
            <button
                v-for="genre in genreOptions"
                :key="genre"
                type="button"
                :class="{ selected: song.genres.includes(genre) }"
                @click="toggleGenre(genre)"
            >
              {{ genre }}
            </button>
          </div>
        </div>
        <div class="form-group">
          <label for="file">Audio File:</label>
          <input type="file" @change="handleFileUpload" accept=".mp3,audio/*" id="file" class="spotify-input"/>
        </div>
        <div class="button-group">
          <button type="submit" class="btn btn-primary">Save</button>
          <button type="button" class="btn btn-secondary" @click="goBack">Go Back</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import {ref, onMounted} from 'vue';
import {useRouter} from 'vue-router';
import axios from 'axios';
import useVuelidate from '@vuelidate/core';
import {required, minValue} from '@vuelidate/validators';
import {genreOptions} from '../assets/genres';

export default {
  setup() {
    const router = useRouter();
    const song = ref({
      name: '',
      artist_id: '',
      genres: [],
      length: '',
      data_url: '',
      version: 0
    });
    const artists = ref([]);
    const errors = ref({
      name: '',
      artist_id: '',
      length: '',
      data_url: ''
    });

    const rules = {
      name: {required},
      artist_id: {required},
      genres: {required},
      length: {required, minValue: minValue(1)},
      data_url: {required}
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

    const submitForm = async () => {
      const result = await v$.value.$validate();
      if (result) {
        try {
          const response = await axios.post('http://localhost:8080/api/songs', {
            name: song.value.name,
            artist: song.value.artist_id ? {id: song.value.artist_id} : null,
            genres: song.value.genres,
            length: song.value.length,
            data_url: song.value.data_url,
            version: song.value.version
          }, {withCredentials: true});
          console.log('Song added:', response.data);
          router.push({path: '/', query: {message: 'Song added successfully!'}});
        } catch (error) {
          if (error.response) {
            errors.value.name = error.response.data.name || '';
            errors.value.artist = error.response.data.artist || '';
            errors.value.length = error.response.data.length || '';
            console.error('Error adding song:', error.response.data);
          } else {
            console.error('Error adding song:', error.message);
          }
        }
      }
    };

    const goBack = () => {
      router.push('/');
    };

    const toggleGenre = (genre) => {
      const index = song.value.genres.indexOf(genre);
      if (index === -1) {
        song.value.genres.push(genre);
      } else {
        song.value.genres.splice(index, 1);
      }
    };

    onMounted(() => {
      fetchArtists();
      song.value.genres = [];
    });

    return {
      song,
      artists,
      errors,
      handleFileUpload,
      submitForm,
      goBack,
      toggleGenre,
      genreOptions,
      v$
    };
  }
};
</script>

<style scoped>
@import '../assets/SongCRUD.css';
</style>