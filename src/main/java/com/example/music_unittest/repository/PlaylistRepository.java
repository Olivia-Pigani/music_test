package com.example.music_unittest.repository;

import com.example.music_unittest.domain.entity.Playlist;
import com.example.music_unittest.domain.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    @Query("SELECT s FROM Song s JOIN s.playlistList p GROUP BY s.songId ORDER BY COUNT(p) DESC")
    List<Song> getTopXSongsIncludedInAllPlaylists(Pageable pageable);
}
