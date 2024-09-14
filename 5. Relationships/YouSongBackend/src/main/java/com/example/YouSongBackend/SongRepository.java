package com.example.YouSongBackend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Integer> {
    List<Song> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName);

    @Query("SELECT s FROM Song s JOIN FETCH s.artist")
    List<Song> findAllWithArtists();
}