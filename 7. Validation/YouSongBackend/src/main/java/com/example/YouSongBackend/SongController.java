package com.example.YouSongBackend;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:5173")
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody Song song) {
        System.out.println(song.toString());
        Song createdSong = songService.createSong(song);
        return ResponseEntity.ok(createdSong);
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        for (Song song : songs) {
            System.out.println(song.toString());
        }
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Integer id) {
        Optional<Song> song = songService.getSongById(id);
        return song.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong(@PathVariable Integer id, @Valid @RequestBody Song song) {
        Song updatedSong = songService.updateSong(id, song);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Integer id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> searchSongs(@RequestParam String searchTerm) {
        List<Song> songs = songService.searchSongs(searchTerm);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Song>> getSongsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Song> songsPage = songService.getSongsPaginated(page, size);
        return ResponseEntity.ok(songsPage);
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<Page<Song>> searchSongsPaginated(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<Song> songsPage = songService.searchSongsPaginated(searchTerm, page, size);
        return ResponseEntity.ok(songsPage);
    }
}