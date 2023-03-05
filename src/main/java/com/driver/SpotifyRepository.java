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
        boolean marker1 = false;
        for(Artist artist : artists) {
            if (artist.getName().equals(artistName)) {
                art = artist;
                marker1 = true;
                break;
            }
        }
        if(!marker1)
        {
            art = createArtist(artistName);
        }

        Album album = new Album(title);
        albums.add(album);
        artistAlbumMap.get(art).add(album);
        albumSongMap.put(album, new ArrayList<>());

        return album;
    }

    //4th
    public Song createSong(String title, String albumName, int length) throws Exception {

        Album album = null;
        boolean marker1 = false;
        for(Album al : albums) {
            if(al.getTitle().equals(albumName)) {
                album = al;
                marker1 = true;
                break;
            }
        }
        if (!marker1) {
            throw new Exception("Album does not exist");
        }

        Song song = new Song(title, length);
        songs.add(song);
        albumSongMap.get(album).add(song);

        songLikeMap.put(song, new ArrayList<>());

        return song;
    }

    //5th
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        playlistSongMap.put(playlist, new ArrayList<>());



        for (Song song : songs) {
            if (song.getLength() == length) {
                playlistSongMap.get(playlist).add(song);
            }
        }

        User presentuser = null;
        boolean marker1 = false;
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                presentuser = user;
                marker1 = true;
            }
        }

        if (!marker1) {
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

        Playlist playlist = new Playlist(title);
        playlists.add(playlist);

        playlistSongMap.put(playlist, new ArrayList<>());


        for (Song song : songs) {
            if (songTitles.contains(song.getTitle())) {
                playlistSongMap.get(playlist).add(song);
            }
        }


        User presentuser = null;
        boolean marker1 = false;
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                presentuser = user;
                marker1 = true;
            }
        }

        if (!marker1)
        {
            throw new Exception("User does not exist");
        }

        creatorPlaylistMap.put(presentuser, playlist);

        userPlaylistMap.get(presentuser).add(playlist);

//        songLikeMap.get(s).add(presentuser);
//        s.setLikes(s.getLikes()+1);

        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        userslist.add(presentuser);
        playlistListenerMap.put(playlist,userslist);
        return playlist;
    }

    //7th
    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {

        boolean marker1 = false;
        boolean marker2 = false;
        Playlist playlist = null;
        for (Playlist p : playlists) {
            if (p.getTitle().equals(playlistTitle)) {
                playlist = p;
                marker1 = true;
                break;
            }
        }

        if (!marker1) {
            throw new Exception("Playlist does not exist");
        }

        User presentuser = null;
        for (User u : users) {
            if (u.getMobile().equals(mobile)) {
                presentuser = u;
                marker2 = true;
                break;
            }
        }
        if (!marker2) {
            throw new Exception("User does not exist");
        }
//isko dekha hai....
        List<User> userslist = new ArrayList<>();
        if(playlistListenerMap.containsKey(playlist)){
            userslist=playlistListenerMap.get(playlist);
        }
        if(!userslist.contains(presentuser))
            userslist.add(presentuser);
        playlistListenerMap.put(playlist,userslist);
//        public HashMap<User, Playlist> creatorPlaylistMap;
        if(creatorPlaylistMap.get(presentuser)!=playlist)
            creatorPlaylistMap.put(presentuser,playlist);
//        public HashMap<User, List<Playlist>> userPlaylistMap;
        List<Playlist>userplaylists = new ArrayList<>();
        if(userPlaylistMap.containsKey(presentuser)){
            userplaylists=userPlaylistMap.get(presentuser);
        }
        if(!userplaylists.contains(playlist))userplaylists.add(playlist);
        userPlaylistMap.put(presentuser,userplaylists);

//meri banai hui
//        if (!creatorPlaylistMap.containsKey(presentuser)) {
//            creatorPlaylistMap.put(presentuser, play);
//        }
//        if (!userPlaylistMap.containsKey(presentuser)) {
//            userPlaylistMap.get(presentuser).add(play);
//        }

        return playlist;
    }

    //8th
    public Song likeSong(String mobile, String songTitle) throws Exception {

        boolean marker1 = false;
        boolean marker2 = false;

        User presentuser = new User();
        for (User u : users) {
            if (u.getMobile().equals(mobile)) {
                presentuser = u;
                marker1 = true;
                break;
            }
        }

        if (!marker1) {
            throw new Exception("User does not exist");
        }

        Song song = new Song();
        for (Song l : songs) {
            if (l.getTitle().equals(songTitle)) {
                song = l;
                marker2 = true;
                break;
            }
        }
        if (!marker2) {
            throw new Exception("Song does not exist");
        }

        //isko dekhna hai...

//        List<User> users = new ArrayList<>();
////        if(songLikeMap.containsKey(song)){
//            users=songLikeMap.get(song);
////        }
//        if (!users.contains(presentuser)) {
//            users.add(presentuser);
//            songLikeMap.put(song, users);
//            song.setLikes(song.getLikes() + 1);

        for (User l : songLikeMap.get(song)) {
            if (l == presentuser) {
                songLikeMap.get(song).add(presentuser);
                song.setLikes(song.getLikes() + 1);


                //way 1:-
                Album need1 = new Album();
                for (Album a1 : albumSongMap.keySet()) {
                    for (Song a2 : albumSongMap.get(a1)) {
                        if (a2.getTitle().equals(songTitle)) {
                            need1 = a1;
                            break;
                        }
                    }
                }

                Artist need2 = new Artist();
                for (Artist a1 : artistAlbumMap.keySet()) {
                    for (Album a2 : artistAlbumMap.get(a1)) {
                        if (a2 == need1) {
                            need2 = a1;
                            break;
                        }
                    }
                }
                need2.setLikes(need2.getLikes() + 1);
            }
        }


        //way 2:-
//            Album album = new Album();
//            for(Album curAlbum : albumSongMap.keySet()){
//                List<Song> temp = albumSongMap.get(curAlbum);
//                if(temp.contains(song)){
//                    album=curAlbum;
//                    break;
//                }
//            }
//
//
//            Artist artist = new Artist();
//            for(Artist curArtist : artistAlbumMap.keySet()){
//                List<Album> temp = artistAlbumMap.get(curArtist);
//                if(temp.contains(album)){
//                    artist=curArtist;
//                    break;
//                }
//            }
//
//            artist.setLikes(artist.getLikes()+1);
//        }


        //replace all s to song...
//        boolean present = false;
//
//        for (User l : songLikeMap.get(s)) {
//            if (l.getMobile().equals(mobile)) {
//                present = true;
//                break;
//            }
//        }
//// if user is not present then it like both the song and artist
//        if (!present) {
//            songLikeMap.get(s).add(presentuser);
//
//            // like the song
//            int like = songLikeMap.get(s).size();
//            s.setLikes(like + 1);
////            s.setLikes(s.getLikes()+1);
////            for(Song e : songs) {
////                if (e == s) {
////                    e.setLikes(e.getLikes() + 1);
////                }
        return song;
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
