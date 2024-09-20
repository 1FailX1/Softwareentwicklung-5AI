package com.example.YouSongBackend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName);
    Page<SongProjection> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName, Pageable pageable);
    Page<SongProjection> findAllProjectedBy(Pageable pageable);
    @Query("SELECT s.id as id, s.name as name, s.artist as artist, s.data_url as data_url FROM Song s WHERE s.id = :id")
    SongDataForStreamingProjection findProjectedById(@Param("id") int id);
}