package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;             //used
    public HashMap<Album, List<Song>> albumSongMap;                 //used
    public HashMap<Playlist, List<Song>> playlistSongMap;           //used
    public HashMap<Playlist, List<User>> playlistListenerMap;       //used
    public HashMap<User, Playlist> creatorPlaylistMap;              //used
    public HashMap<User, List<Playlist>> userPlaylistMap;           //used
    public HashMap<Song, List<User>> songLikeMap;                   //used

    public List<User> users;                                        //used
    public List<Song> songs;                                        //used
    public List<Playlist> playlists;                                //used
    public List<Album> albums;                                      //used
    public List<Artist> artists;                                    //used


    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    //1st
    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        userPlaylistMap.put(user, new ArrayList<>());
        users.add(user);
        return user;
    }

    //2nd
    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        artistAlbumMap.put(artist, new ArrayList<>());
        return artist;
    }

    //3rd
    public Album createAlbum(String title, String artistName) {
        //check artist is present or not
        Artist art = null;
        for(Artist artist : artists) {
            if (artist.getName().equals(artistName)) {
                art = artist;
            } else {
                art = createArtist(artistName);
            }
        }

        Album album = new Album(title);
        albums.add(album);
        artistAlbumMap.get(art).add(album);
        albumSongMap.put(album, new ArrayList<>());

        return album;
    }

    //4th
    public Song createSong(String title, String albumName, int length) throws Exception {

        Song song = new Song(title, length);
        songs.add(song);

        songLikeMap.put(song, new ArrayList<>());

        Album album = new Album(albumName);
        if (!albumSongMap.containsKey(album)) {
            throw new Exception("Album does not exist");
        }
        albumSongMap.get(album).add(song);
        return song;
    }

    //5th
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        playlistSongMap.put(playlist, new ArrayList<>());

        Song s = null;

        for (Song song : songs) {
            if (song.getLength() == length) {
                s = song;
            }
        }
        playlistSongMap.get(playlist).add(s);

        User presentuser = null;
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                presentuser = user;
            }
        }

        if (presentuser == null) {
            throw new Exception("User does not exist");
        }

        creatorPlaylistMap.put(presentuser, playlist);

        userPlaylistMap.get(presentuser).add(playlist);

//        songLikeMap.get(s).add(presentuser);
//        s.setLikes(s.getLikes()+1);

        if (!playlistListenerMap.containsKey(playlist)) {
            playlistListenerMap.put(playlist, new ArrayList<>());
        }
            playlistListenerMap.get(playlist).add(presentuser);

        return playlist;
    }

    //6th
    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(title))
                return  playlist;
        }

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        playlistSongMap.put(playlist, new ArrayList<>());

        Song s = null;

        for(String songName: songTitles) {
            for (Song song : songs) {
                if (song.getTitle().equals(songName)) {
                    s = song;
                }
            }
        }
        playlistSongMap.get(playlist).add(s);

        User presentuser = null;
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                presentuser = user;
            }
        }

        if (presentuser == null)
        {
            throw new Exception("User does not exist");
        }

        creatorPlaylistMap.put(presentuser, playlist);

        userPlaylistMap.get(presentuser).add(playlist);

//        songLikeMap.get(s).add(presentuser);
//        s.setLikes(s.getLikes()+1);

        if(!playlistListenerMap.containsKey(playlist)) {
            playlistListenerMap.put(playlist, new ArrayList<>());
        }
        playlistListenerMap.get(playlist).add(presentuser);
        return playlist;
    }

    //7th
    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        Playlist play = null;
        for (Playlist p : playlists) {
            if (p.getTitle().equals(playlistTitle)) {
                play = p;
            }
        }

        if (play == null) {
            throw new Exception("Playlist does not exist");
        }

        User presentuser = null;
        for (User u : users) {
            if (u.getMobile().equals(mobile)) {
                presentuser = u;
            }
        }
        if (presentuser == null) {
            throw new Exception("User does not exist");
        }

//        if (!creatorPlaylistMap.containsKey(presentuser)) {
//            creatorPlaylistMap.put(presentuser, play);
//        }
//        if (!userPlaylistMap.containsKey(presentuser)) {
//            userPlaylistMap.get(presentuser).add(play);
//        }

        //        public HashMap<Playlist, List<User>> playlistListenerMap;
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(play)){
            userslist=playlistListenerMap.get(play);
        }
        if(!userslist.contains(presentuser))
            userslist.add(presentuser);
        playlistListenerMap.put(play,userslist);
//        public HashMap<User, Playlist> creatorPlaylistMap;
        if(creatorPlaylistMap.get(presentuser)!=play)
            creatorPlaylistMap.put(presentuser,play);
//        public HashMap<User, List<Playlist>> userPlaylistMap;
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(presentuser)){
            userplaylists=userPlaylistMap.get(presentuser);
        }
        if(!userplaylists.contains(play))userplaylists.add(play);
        userPlaylistMap.put(presentuser,userplaylists);

        return play;
    }

    //8th
    public Song likeSong(String mobile, String songTitle) throws Exception {

        User presentuser = null;
        for (User u : users) {
            if (u.getMobile().equals(mobile)) {
                presentuser = u;
            }
        }

        if (presentuser == null) {
            throw new Exception("User does not exist");
        }

        Song s = null;
        for (Song l : songs) {
            if (l.getTitle().equals(songTitle)) {
                s = l;
            }
        }
        if (s == null) {
            throw new Exception("Song does not exist");
        }

        boolean present = false;

        for (User l : songLikeMap.get(s)) {
            if (l.getMobile().equals(mobile)) {
                present = true;
                break;
            }
        }

        if (!present) {
            songLikeMap.get(s).add(presentuser);

            // like the song
            int like = songLikeMap.get(s).size();
            s.setLikes(like+1);
//            s.setLikes(s.getLikes()+1);
//            for(Song e : songs) {
//                if (e == s) {
//                    e.setLikes(e.getLikes() + 1);
//                }
        }
        Album need1 = null;
        for (Album a1 : albumSongMap.keySet()) {
            for (Song a2 : albumSongMap.get(a1)) {
                if (a2.getTitle().equals(songTitle)) {
                    need1 = a1;
                    break;
                }
            }
        }

        Artist need2 = null;
        for(Artist a1 : artistAlbumMap.keySet()) {
            for (Album a2 : artistAlbumMap.get(a1)) {
                if (a2.equals(need1)) {
                    need2 = a1;
                    break;
                }
            }
        }
        need2.setLikes(need2.getLikes()+1);

        return s;
    }

    public String mostPopularArtist() {
        int max = Integer.MIN_VALUE;
        String name = "";

        for(Artist a : artists)
        {
            max = Math.max(max, a.getLikes());
        }
        for(Artist a : artists)
        {
            if(max == a.getLikes())
            {
                name = a.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {

        int max = Integer.MIN_VALUE;
        String name = "";

        for(Song a : songs)
        {
            max = Math.max(max, a.getLikes());
        }
        for(Song a : songs)
        {
            if(max == a.getLikes())
            {
                name = a.getTitle();
            }
        }
        return name;
    }
}
