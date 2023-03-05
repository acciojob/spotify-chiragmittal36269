package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    //1st
    public User createUser(String name, String mobile) {
       return spotifyRepository.createUser(name, mobile);
    }

    //2nd
    public Artist createArtist(String name) {
        return spotifyRepository.createArtist(name);
    }

    //3rd
    public Album createAlbum(String title, String artistName) {
        return spotifyRepository.createAlbum(title, artistName);
    }

    //4th
    public Song createSong(String title, String albumName, int length) throws Exception {
        return spotifyRepository.createSong(title, albumName, length);
    }

    //5th
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        return spotifyRepository.createPlaylistOnLength(mobile, title, length);
    }

    //6th
    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
    }

    //7th
    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        return spotifyRepository.findPlaylist(mobile, playlistTitle);
    }

    //8th
    public Song likeSong(String mobile, String songTitle) throws Exception {
            return spotifyRepository.likeSong(mobile, songTitle);
    }

    public String mostPopularArtist() {
        return spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return spotifyRepository.mostPopularSong();
    }
}
