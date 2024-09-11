package com.example.YouSongBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return (List<Song>) songRepository.findAll();
    }

    public Optional<Song> getSongById(Integer id) {
        return songRepository.findById(id);
    }

    public Song updateSong(Integer id, Song song) {
        song.setId(id);
        return songRepository.save(song);
    }

    public void deleteSong(Integer id) {
        songRepository.deleteById(id);
    }
}