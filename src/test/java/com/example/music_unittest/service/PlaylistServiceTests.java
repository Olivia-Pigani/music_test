package com.example.music_unittest.service;

import com.example.music_unittest.domain.entity.Playlist;
import com.example.music_unittest.domain.entity.Song;
import com.example.music_unittest.domain.enumeration.Genre;
import com.example.music_unittest.repository.PlaylistRepository;
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
public class PlaylistServiceTests {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist classicalMusicPlaylist;

    private List<Playlist> playlistList;

    private List<Song> classicSongList;

    @BeforeEach
    public void setup() {
        classicSongList = new ArrayList<>(List.of(
                new Song("Les Quatre Saisons", "Vivaldi", 360, Genre.CLASSIC),
                new Song("Oboe", " Ennio Morricone", 360, Genre.CLASSIC)

        ));
        classicalMusicPlaylist = new Playlist("top 14", Genre.CLASSIC, classicSongList);

        playlistList = new ArrayList<>(List.of(
                new Playlist("my fav", Genre.POP),
                new Playlist("music for pogo parties :)", Genre.METAL)
        ));
    }

    @DisplayName("test to check playlist saving")
    @Test
    public void saveAPlaylist() {
        //GIVEN
        given(playlistRepository.save(classicalMusicPlaylist)).willReturn(classicalMusicPlaylist);

        //WHEN
        Playlist savedPlaylist = playlistService.saveAPlaylist(classicalMusicPlaylist);

        //THEN
        assertThat(savedPlaylist.getSongList()).isEqualTo(classicSongList);
        assertThat(savedPlaylist.getName()).isEqualTo("top 14");
    }

    @DisplayName("test to get all playlists")
    @Test
    public void getAllPlaylistsTest() {
        //GIVEN
        given(playlistRepository.findAll()).willReturn(playlistList);

        //WHEN
        List<Playlist> playlistsFromDB = playlistService.getAllPlaylists();

        //THEN
        assertThat(playlistsFromDB.size()).isEqualTo(2);
        assertThat(playlistsFromDB.getFirst().getName()).isEqualTo("my fav");
    }

    @DisplayName("test to get an existing playlist in database, by its id")
    @Test
    public void getAPlaylistByIdSucessTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.of(classicalMusicPlaylist));

        //WHEN
        Playlist playlistToFind = playlistService.getAPlaylistById(0L);

        //THEN
        assertThat(playlistToFind).isNotNull();
        assertThat(playlistToFind.getSongList()).isEqualTo(classicSongList);
    }

    @DisplayName("test to get an playlist in database, by its id, but it's not present")
    @Test
    public void getAPlaylistByIdFailTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> playlistService.getAPlaylistById(0L));
    }

    @DisplayName("test to update an playlist that is in database")
    @Test
    public void updateAPlaylistSucessTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.of(classicalMusicPlaylist));
        classicalMusicPlaylist.setGenre(Genre.METAL.toString());
        classicalMusicPlaylist.setName("best metal songs!");
        classicalMusicPlaylist.setSongList(
                new ArrayList<>(List.of(
                        new Song("test", "my artiste", 45, Genre.METAL)
                ))
        );
        given(playlistRepository.save(classicalMusicPlaylist)).willReturn(classicalMusicPlaylist);

        //WHEN
        Playlist updatedPlaylist = playlistService.updateAPlaylist(0L, classicalMusicPlaylist);

        //THEN
        assertThat(updatedPlaylist.getSongList().size()).isEqualTo(1);
        assertThat(updatedPlaylist.getName()).isEqualTo("best metal songs!");
        assertThat(updatedPlaylist.getGenre()).isEqualTo(Genre.METAL);
    }

    @DisplayName("test to update an playlist that is NOT in database")
    @Test
    public void updateAPlaylistFailTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> playlistService.getAPlaylistById(0L));
    }

    @DisplayName("test to delete an existing playlist by its id")
    @Test
    public void deleteAPlaylistByIdSuccessTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.of(classicalMusicPlaylist));
        willDoNothing().given(playlistRepository).deleteById(0L);

        //WHEN
        playlistService.deleteAPlaylistById(0L);

        //THEN
        verify(playlistRepository, times(1)).deleteById(0L);
    }

    @DisplayName("test to delete a playlist by its id and that is not database")
    @Test
    public void deleteAPlaylistByIdFailTest() {
        //GIVEN
        given(playlistRepository.findById(0L)).willReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> playlistService.getAPlaylistById(0L));
    }

    @DisplayName("test to get the top X songs most commonly included in all playlists")
    @Test
    public void getTopXSongsIncludedInAllPlaylistsTest() {
        //GIVEN
        //songs
        Song song1 = new Song("song1", "artist1", 65, Genre.CLASSIC);
        Song song2 = new Song("song2", "artist2", 24, Genre.CLASSIC);

        //playlists
        Playlist myPlaylist = new Playlist("random playlist", Genre.CLASSIC, new ArrayList<>());
        myPlaylist.getSongList().add(song1);
        myPlaylist.getSongList().add(song1);
        myPlaylist.getSongList().add(song2);
        classicalMusicPlaylist.getSongList().add(song1);

        // all playlists
        List<Playlist> allPlaylists = new ArrayList<>(List.of(myPlaylist, classicalMusicPlaylist));

        given(playlistRepository.findAll()).willReturn(allPlaylists);

        //WHEN
        int songsAmount = 4;
        List<Song> topSongs = playlistService.getTopXSongsIncludedInAllPlaylists(songsAmount);


        //THEN
        assertThat(topSongs.size()).isEqualTo(songsAmount);
        assertThat(topSongs.getFirst().getTitle()).isEqualTo("song1");
        assertThat(topSongs.get(1).getTitle()).isEqualTo("song2");
    }

    @DisplayName("test to put songs in a playlist")
    @Test
    public void putASongInAPlaylistTest() {
        //GIVEN
        Song songToAdd = new Song("test", "test", 45, Genre.CLASSIC);
        given(playlistRepository.findById(0L)).willReturn(Optional.of(classicalMusicPlaylist));
        given(songRepository.findById(0L)).willReturn(Optional.of(songToAdd));
        classicalMusicPlaylist.getSongList().add(songToAdd);
        given(playlistRepository.save(classicalMusicPlaylist)).willReturn(classicalMusicPlaylist);

        //WHEN
        playlistService.putASongInAPlaylist(0L, 0L);

        //THEN
        assertThat(classicSongList.getLast()).isEqualTo(songToAdd);
    }
}
