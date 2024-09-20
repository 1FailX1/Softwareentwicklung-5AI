package com.example.YouSongBackend;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        return song.map(s -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setETag("\"" + s.getVersion() + "\"");
            return new ResponseEntity<>(s, headers, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSong(@PathVariable int id, @RequestBody Song song, @RequestHeader("If-Match") String ifMatch) {
        try {
            Optional<Song> existingSong = songService.getSongById(id);
            if (existingSong.isPresent()) {
                if (!ifMatch.equals("\"" + existingSong.get().getVersion() + "\"")) {
                    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Version mismatch");
                }
                Song updatedSong = songService.updateSong(id, song);
                return ResponseEntity.ok(updatedSong);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
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
    public ResponseEntity<Page<SongProjection>> getSongsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SongProjection> songsPage = songService.getSongsPaginated(page, size);
        return ResponseEntity.ok(songsPage);
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<Page<SongProjection>> searchSongsPaginated(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SongProjection> songsPage = songService.searchSongsPaginated(searchTerm, page, size);
        return ResponseEntity.ok(songsPage);
    }

    @GetMapping("/streaming/{id}")
    public ResponseEntity<SongDataForStreamingProjection> getSongDataForStreaming(@PathVariable int id) {
        SongDataForStreamingProjection songData = songService.getSongDataForStreaming(id);
        return songData != null ? ResponseEntity.ok(songData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search/genres")
    public ResponseEntity<Page<SongProjection>> searchSongsByGenres(
            @RequestParam List<String> genres,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SongProjection> songsPage = songService.searchSongsByGenres(genres, page, size);
        return ResponseEntity.ok(songsPage);
    }

    @GetMapping("/search/genres-and-term")
    public ResponseEntity<Page<SongProjection>> searchSongsByGenresAndTerm(
            @RequestParam List<String> genres,
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SongProjection> songsPage = songService.searchSongsByGenresAndTerm(genres, searchTerm, page, size);
        return ResponseEntity.ok(songsPage);
    }
}