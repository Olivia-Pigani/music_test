package com.example.music_unittest.service;

import com.example.music_unittest.domain.entity.Playlist;
import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.repository.PlaylistRepository;
import com.example.music_unittest.repository.SongRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;


    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    public Playlist saveAPlaylist(Playlist newPlaylist) {
        return playlistRepository.save(newPlaylist);
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Playlist getAPlaylistById(Long id) {
        return playlistRepository.findById(id).orElseThrow();
    }

    public Playlist updateAPlaylist(Long id, Playlist playlistNewData) {
        Playlist playlistToUpdate = playlistRepository.findById(id).orElseThrow();

        playlistToUpdate.setName(playlistNewData.getName());
        playlistToUpdate.setGenre(playlistNewData.getGenre().toString());
        playlistToUpdate.setSongList(playlistNewData.getSongList());

        return playlistRepository.save(playlistToUpdate);
    }

    public void deleteAPlaylistById(Long id) {
        Playlist playlistToDelete = playlistRepository.findById(id).orElseThrow();

        playlistRepository.deleteById(id);
    }

    public List<Song> getTopXSongsIncludedInAllPlaylists(int songAmount) {
        Pageable pageable = PageRequest.of(0, songAmount);
        return playlistRepository.getTopXSongsIncludedInAllPlaylists(pageable);
    }

    @Transactional
    public void putASongInAPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        Song song = songRepository.findById(songId).orElseThrow();

        if (playlist.getSongList() == null){
            playlist.setSongList(new ArrayList<>());
        }

        playlist.getSongList().add(song);
        song.getPlaylistList().add(playlist);
        playlistRepository.save(playlist);
    }
}
