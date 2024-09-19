package com.example.YouSongBackend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "songs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Positive(message = "Length is required")
    private int length;

    @NotBlank(message = "Song File is required")
    @Lob  // Large object to store the Data URL
    @Column(columnDefinition = "LONGTEXT")
    private String data_url;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public Song() {}

    public Song(int id, String name, Artist artist, String genre, int length, String dataUrl) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.length = length;
        this.data_url = dataUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name +
                ", artist=" + artist +
                ", genre='" + genre +
                ", length=" + length +
                '}';
    }

    public @NotBlank(message = "Song File is required") String getData_url() {
        return data_url;
    }

    public void setData_url(@NotBlank(message = "Song File is required") String dataUrl) {
        this.data_url = dataUrl;
    }
}