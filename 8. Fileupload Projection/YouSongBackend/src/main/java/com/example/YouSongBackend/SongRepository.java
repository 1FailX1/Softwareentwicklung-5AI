package com.example.YouSongBackend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName);
    Page<SongProjection> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName, Pageable pageable);
    Page<SongProjection> findAllProjectedBy(Pageable pageable);
}