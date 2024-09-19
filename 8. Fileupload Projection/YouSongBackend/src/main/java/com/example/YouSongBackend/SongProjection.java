package com.example.YouSongBackend;

public interface SongProjection {
    int getId();
    String getName();
    Artist getArtist();
    String getGenre();
    int getLength();

}