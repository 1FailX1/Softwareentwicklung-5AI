package com.example.YouSongBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public Song createSong(Song song) {
        Optional<Artist> artist = artistRepository.findById(song.getArtist().getId());
        if (artist.isPresent()) {
            song.setArtist(artist.get());
        } else {
            throw new RuntimeException("Artist not found");
        }
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAllWithArtists();
    }

    public Optional<Song> getSongById(Integer id) {
        return songRepository.findById(id);
    }

    public List<Song> searchSongs(String searchTerm) {
        return songRepository.findByNameContainingIgnoreCaseOrArtist_NameContainingIgnoreCase(searchTerm, searchTerm);
    }

    public Song updateSong(Integer id, Song song) {
        Optional<Artist> artist = artistRepository.findById(song.getArtist().getId());
        if (artist.isPresent()) {
            song.setArtist(artist.get());
        } else {
            throw new RuntimeException("Artist not found");
        }
        song.setId(id);
        return songRepository.save(song);
    }

    public void deleteSong(Integer id) {
        songRepository.deleteById(id);
    }
}