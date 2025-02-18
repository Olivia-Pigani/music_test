package com.example.music_unittest.service;

import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.domain.enumeration.Genre;
import com.example.music_unittest.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SongServiceTests {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private Song song;

    private List<Song> songList;

    @BeforeEach
    public void setup() {
        song = new Song("Tom Sawyer", "Rush", 135, Genre.ROCK);
        songList = new ArrayList<>(List.of(
                new Song("Runaway", "Bon Jovi", 351, Genre.ROCK),
                new Song("Suicide Blonde", "INXS", 210, Genre.POP)
        ));
    }

    @DisplayName("test for save a song")
    @Test
    public void saveASongTest() {
        //GIVEN
        given(songRepository.save(song)).willReturn(song);

        //WHEN
        Song savedSong = songService.saveSong(song);

        //THEN
        assertThat(savedSong).isNotNull();
        assertThat(savedSong.getTitle()).isEqualTo("Tom Sawyer");
    }

    @DisplayName("test to get all songs ")
    @Test
    public void getAllSongsTest() {
        //GIVEN
        given(songRepository.findAll()).willReturn(songList);

        //WHEN
        List<Song> findedSongList = songService.getAllSongs();

        //THEN
        assertThat(findedSongList.get(0).getArtist()).isEqualTo("Bon Jovi");
        assertThat(findedSongList.size()).isEqualTo(2);
        assertThat(findedSongList.get(1).getArtist()).isEqualTo("INXS");
        assertThat(findedSongList).isNotNull();
    }

    @DisplayName("test to get a song by its id and it's present")
    @Test
    public void getASongByIdSuccessTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.of(song));

        //WHEN
        Song songToFind = songService.getASongById(0L);

        //THEN
        assertThat(songToFind).isNotNull();
        assertThat(songToFind.getTitle()).isEqualTo("Tom Sawyer");
    }

    @DisplayName("test to get a song by its id and that is not in database and throw exception")
    @Test
    public void getASongByIdFailTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> songService.getASongById(0L));
    }

    @DisplayName("test to update a song that exists in database")
    @Test
    public void updateASongSuccessTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.of(song));
        song.setTitle("2 cool");
        song.setArtist("MC cool");
        song.setDuration(42);
        song.setGenre("classic");

        given(songRepository.save(song)).willReturn(song);

        //WHEN
        Song updatedSong = songService.updateASong(0L, song);

        //THEN
        assertThat(updatedSong.getId()).isEqualTo(0L);
        assertThat(updatedSong.getTitle()).isEqualTo("2 cool");
        assertThat(updatedSong.getArtist()).isEqualTo("MC cool");
        assertThat(updatedSong.getDuration()).isEqualTo(42);
        assertThat(updatedSong.getGenre()).isEqualTo(Genre.CLASSIC);
    }

    @DisplayName("test to update a song that do not exists in database")
    @Test
    public void updateASongFailTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.empty());


        //THEN
        assertThrows(NoSuchElementException.class, () -> songService.getASongById(0L));
    }

    @DisplayName("test to delete a song based on its id")
    @Test
    public void deleteASongSuccessTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.ofNullable(song));
        willDoNothing().given(songRepository).deleteById(0L); // mocking the void

        //WHEN
        songService.deleteASong(0L);

        //THEN
        verify(songRepository, times(1)).deleteById(0L);
    }

    @DisplayName("test to delete a song based on its id but its not in database")
    @Test
    public void deleteASongFailTest() {
        //GIVEN
        given(songRepository.findById(0L)).willReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> songService.getASongById(0L));
    }

    @DisplayName("test to get all songs by a specific genre")
    @Test
    public void getAllSongsByGenreTest(){
        //GIVEN
        String userChoiceStr = "rOcK";
        Genre genre = Genre.valueOf(userChoiceStr.toUpperCase());
        given(songRepository.getSongByGenre(genre)).willReturn(songList.stream()
                .filter(song->song.getGenre().equals(genre)).toList());

        //WHEN
        List<Song> rockSongList = songService.getSongByGenre(userChoiceStr);

        //THEN
        assertThat(rockSongList.size()).isEqualTo(1);
        assertThat(rockSongList.getFirst().getGenre()).isEqualTo(Genre.ROCK);
    }

    @DisplayName("test to get all songs by a genre that do not exists in database")
    @Test
    public void getAllSongsByGenreFailTest(){
        //GIVEN
        String userChoiceStr = "jazz";

        //THEN
        assertThrows(IllegalArgumentException.class, () -> songService.getSongByGenre(userChoiceStr));

    }
}
