import { createRouter, createWebHistory } from 'vue-router';
import SongList from '../components/SongList.vue';
import AddSong from '../components/AddSong.vue';
import EditSong from '../components/EditSong.vue';
import ArtistList from '../components/ArtistList.vue';
import AddArtist from '../components/AddArtist.vue';
import EditArtist from '../components/EditArtist.vue';
import SongManager from '../components/SongManager.vue';

const routes = [
    { path: '/', component: SongList },
    { path: '/login', component: SongManager },
    { path: '/add-song', component: AddSong },
    { path: '/edit-song/:id', component: EditSong },
    { path: '/artists', component: ArtistList },
    { path: '/add-artist', component: AddArtist },
    { path: '/edit-artist/:id', component: EditArtist }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;