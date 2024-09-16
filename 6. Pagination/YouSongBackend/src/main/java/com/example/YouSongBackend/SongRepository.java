package com.example.YouSongBackend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Integer> {
    List<Song> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName);
    Page<Song> findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(String name, String artistName, Pageable pageable);
}