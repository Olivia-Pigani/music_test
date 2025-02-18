package com.example.music_unittest.repository;

import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.domain.enumeration.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song,Long> {

    List<Song> getSongByGenre(Genre genre);

}
