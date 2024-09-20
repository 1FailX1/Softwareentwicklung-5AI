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

    @Query("SELECT s FROM Song s JOIN s.genres g WHERE g IN :genres GROUP BY s.id")
    Page<SongProjection> findByGenresIn(@Param("genres") List<String> genres, Pageable pageable);

    @Query("SELECT s FROM Song s JOIN s.genres g WHERE g IN :genres AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(s.artist.name) LIKE LOWER(CONCAT('%', :artistName, '%'))) GROUP BY s.id")
    Page<SongProjection> findByGenresInAndNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(
            @Param("genres") List<String> genres, @Param("name") String name, @Param("artistName") String artistName, Pageable pageable);
}