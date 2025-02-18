package com.example.music_unittest.domain.entity;

import com.example.music_unittest.domain.enumeration.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playlistId;

    private String name;

    private Genre genre;

    @ManyToMany(cascade = {CascadeType.ALL},
            mappedBy = "playlistList")
    @JsonIgnore
    private List<Song> songList;

    public Playlist(String name, Genre genre, List<Song> songList) {
        this.name = name;
        this.genre = genre;
        this.songList = songList;
    }

    public Playlist(String name, Genre genre) {
        this.name = name;
        this.genre = genre;
    }

    public Playlist() {
    }

    public long getId() {
        return playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        try {
            this.genre = Genre.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("there is no such genre!");
        }
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", name='" + name + '\'' +
                ", genre=" + genre +
                ", songList=" + songList +
                '}';
    }
}
