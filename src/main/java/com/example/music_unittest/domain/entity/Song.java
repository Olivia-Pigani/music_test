package com.example.music_unittest.domain.entity;

import com.example.music_unittest.domain.enumeration.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long songId;

    @Column(nullable = false)
    private String title;

    private String artist = "unknown";

    // in seconds
    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private Genre genre;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "song_playlist",
            joinColumns = {
                    @JoinColumn(name = "song_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "playlist_id")
            })
    @JsonIgnore
    List<Playlist> playlistList;

    public Song(String title, String artist, int duration, Genre genre) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
    }

    public Song(String title, String artist, int duration, Genre genre, List<Playlist> playlistList) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
        this.playlistList = playlistList;
    }

    public Song() {
    }

    public long getId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", genre=" + genre +
                ", playlistList=" + playlistList +
                '}';
    }
}
