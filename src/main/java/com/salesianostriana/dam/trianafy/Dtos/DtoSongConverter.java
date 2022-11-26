package com.salesianostriana.dam.trianafy.Dtos;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import org.springframework.stereotype.Component;

@Component
public class DtoSongConverter {

    public Song createSongDtoToSong(CreateSongDto c, Artist artist){
        return Song.builder()
                .title(c.getTitle())
                .album(c.getAlbum())
                .year(c.getYear())
                .artist(artist)
                .build();
    }

    public DtoGetSong songToGetSongDto(Song s){
        return DtoGetSong
                .builder()
                .id(s.getId())
                .title(s.getTitle())
                .album(s.getAlbum())
                .year(s.getYear())
                .artist(s.getArtist().getName())
                .build();
    }
}
