package com.example.YouSongBackend;

import java.util.Set;

public interface SongProjection {
    int getId();
    String getName();
    Artist getArtist();
    int getLength();
    Set<String> getGenres();
}