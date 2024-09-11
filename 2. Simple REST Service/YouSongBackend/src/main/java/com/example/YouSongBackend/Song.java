package com.example.YouSongBackend;

import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String name;
    private String artist;
    private String genre;
    private int length;

    public Song(int id, String name, String artist, String genre, int length) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
    }

    protected Song() {

    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public int getLength() {
        return length;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
