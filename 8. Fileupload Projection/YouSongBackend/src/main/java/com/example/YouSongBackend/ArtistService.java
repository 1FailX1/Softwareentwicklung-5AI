package com.example.YouSongBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {
    @Autowired
    private ArtistRepository artistRepository;

    public Artist createArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public List<Artist> getAllArtists() {
        return (List<Artist>) artistRepository.findAll();
    }

    public Artist getArtistById(Integer id) {
        return artistRepository.findById(id).orElse(null);
    }

    public Artist updateArtist(Integer id, Artist artist) {
        Artist existingArtist = artistRepository.findById(id).orElseThrow(() -> new RuntimeException("Artist not found"));
        existingArtist.setName(artist.getName());
        return artistRepository.save(existingArtist);
    }
    public void deleteArtist(Integer id) {
        artistRepository.deleteById(id);
    }
}