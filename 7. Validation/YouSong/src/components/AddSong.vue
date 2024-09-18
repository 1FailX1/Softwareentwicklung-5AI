<template>
  <div class="crud-song">
    <h1>Add Song</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Song Name:</label>
        <input type="text" v-model="song.name" id="name"/>
        <span v-for="error in v$.name.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.name" class="error-message">{{ errors.name }}</p>
      </div>
      <div class="form-group">
        <label for="artist">Artist:</label>
        <select v-model="song.artistId" id="artist" class="spotify-select">
          <option v-for="artist in artists" :key="artist.id" :value="artist.id">{{ artist.name }}</option>
        </select>
        <span v-for="error in v$.artistId.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.artistId" class="error-message">{{ errors.artist }}</p>
      </div>
      <div class="form-group">
        <label for="genre">Genre:</label>
        <input type="text" v-model="song.genre" id="genre"/>
        <span v-for="error in v$.genre.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.genre" class="error-message">{{ errors.genre }}</p>
      </div>
      <div class="form-group">
        <label for="length">Length (seconds):</label>
        <input type="number" v-model="song.length" id="length"/>
        <span v-for="error in v$.length.$errors" :key="error.$uid" style="color: red"> {{ error.$message }}</span>
        <p v-if="errors.length" class="error-message">{{ errors.length }}</p>
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
import { useRouter } from 'vue-router';
import axios from 'axios';
import useVuelidate from '@vuelidate/core';
import { required, minValue } from '@vuelidate/validators';

export default {
  setup() {
    const router = useRouter();
    const song = ref({
      name: '',
      artistId: '',
      genre: '',
      length: ''
    });
    const artists = ref([]);
    const errors = ref({
      name: '',
      artistId: '',
      genre: '',
      length: ''
    });

    const rules = {
        name: { required },
        artistId: { required },
        genre: { required },
        length: { required, minValue: minValue(1) }
    };

    const v$ = useVuelidate(rules, song);

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
          console.log(song.value.artistId)
          const response = await axios.post('http://localhost:8080/api/songs', {
            name: song.value.name,
            artist: song.value.artistId ? { id: song.value.artistId } : null,
            genre: song.value.genre,
            length: song.value.length
          });
          console.log('Song added:', response.data);
          router.push({ path: '/', query: { message: 'Song added successfully!' } });
        } catch (error) {
          //backend validation
          if (error.response) {
            errors.value.name = error.response.data.name || '';
            errors.value.artist = error.response.data.artist || '';
            errors.value.genre = error.response.data.genre || '';
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

    onMounted(() => {
      fetchArtists();
    });

    return {
      song,
      artists,
      errors,
      submitForm,
      goBack,
      v$
    };
  }
};
</script>

<style scoped>
@import '../assets/SongCRUD.css';
</style>