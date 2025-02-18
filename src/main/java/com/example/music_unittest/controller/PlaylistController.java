package com.example.music_unittest.controller;

import com.example.music_unittest.domain.entity.Playlist;
import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public Playlist saveAPlaylist(@RequestBody Playlist newPlaylist) {
        return playlistService.saveAPlaylist(newPlaylist);
    }

    @PostMapping("/{playlistId}")
    public void putASongInAPlaylist(@PathVariable Long playlistId, @RequestParam Long songId){
        playlistService.putASongInAPlaylist(playlistId,songId);
    }

    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{playlistId}")
    public Playlist getAPlaylistById(@PathVariable Long playlistId) {
        return playlistService.getAPlaylistById(playlistId);
    }

    @GetMapping("/top-songs")
    public List<Song> getTopXSongsIncludedInAllPlaylists(@RequestParam(defaultValue = "2") int songAmount){
        return playlistService.getTopXSongsIncludedInAllPlaylists(songAmount);
    }

    @PutMapping("/{playlistId}")
    public Playlist updateAPlaylist(@PathVariable Long playlistId, @RequestBody Playlist playlistNewData) {
        return playlistService.updateAPlaylist(playlistId, playlistNewData);
    }

    @DeleteMapping("/{playlistId}")
    public void deleteAPlaylistById(@PathVariable Long playlistId) {
        playlistService.deleteAPlaylistById(playlistId);
    }
}









