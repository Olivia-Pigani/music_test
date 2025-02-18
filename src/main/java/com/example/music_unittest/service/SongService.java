package com.example.music_unittest.service;

import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.domain.enumeration.Genre;
import com.example.music_unittest.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getASongById(Long id) {

        return songRepository.findById(id).orElseThrow();
    }

    public Song updateASong(Long songId, Song updatedSongData) {
        Song songToUpdate = songRepository.findById(songId).orElseThrow();

        songToUpdate.setTitle(updatedSongData.getTitle());
        songToUpdate.setArtist(updatedSongData.getArtist());
        songToUpdate.setGenre(updatedSongData.getGenre().toString());
        songToUpdate.setDuration(updatedSongData.getDuration());

        return songRepository.save(songToUpdate);
    }

    public void deleteASong(Long id) {
        Song songToDelete = songRepository.findById(id).orElseThrow();

        songRepository.deleteById(songToDelete.getId());
    }

    public List<Song> getSongByGenre(String userChoice) {

        Genre genre;

        try {
            genre = Genre.valueOf(userChoice.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("there is no such music genre!");
        }

        return songRepository.getSongByGenre(genre);

    }

}
