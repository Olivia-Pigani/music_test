package com.example.music_unittest.controller;

import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.service.SongService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public Song saveASong(@RequestBody Song newSong) {
        return songService.saveSong(newSong);
    }

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{songId}")
    public Song getASongById(@PathVariable Long songId) {
        return songService.getASongById(songId);
    }

    @GetMapping("/genre/{genreType}")
    public List<Song> getAllSongsByGenre(@RequestParam String genreType){
        return songService.getSongByGenre(genreType);
    }

    @PutMapping("/{songId}")
    public Song updateASong(@PathVariable Long songId, @RequestBody Song updatedSongData) {
        return songService.updateASong(songId, updatedSongData);
    }

    @DeleteMapping("/{songId}")
    public void deleteASong(@PathVariable Long songId) {
        songService.deleteASong(songId);
    }
}
