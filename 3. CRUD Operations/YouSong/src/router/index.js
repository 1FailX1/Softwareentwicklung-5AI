import { createRouter, createWebHistory } from 'vue-router';
import SongList from '@/components/SongList.vue';
import AddSong from '@/components/AddSong.vue';
import EditSong from "@/components/EditSong.vue";

const routes = [
    { path: '/', component: SongList },
    { path: '/add-song', component: AddSong },
    { path: '/edit-song/:id', component: EditSong }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;