package com.example.YouSongBackend;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongRepository extends CrudRepository<Song, Integer> {
    List<Song> findByNameContainingIgnoreCaseOrArtistContainingIgnoreCase(String name, String artist);
}
