<template>
  <div class="crud-artist">
    <h1>Add New Artist</h1>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="name">Artist Name:</label>
        <input type="text" v-model="artist.name" id="name" required class="spotify-input" />
      </div>
      <div class="button-group">
        <button type="submit" class="spotify-button btn-primary">Save</button>
        <button type="button" class="spotify-button btn-secondary" @click="goBack">Go Back</button>
      </div>
    </form>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const router = useRouter();
    const artist = ref({
      name: ''
    });

    const submitForm = () => {
      // Logic to add the artist
      console.log('Artist added:', artist.value);
      router.push('/');
    };

    const goBack = () => {
      router.push('/');
    };

    return {
      artist,
      submitForm,
      goBack
    };
  }
};
</script>

<style scoped>
@import '../assets/ArtistCRUD.css';
</style>
