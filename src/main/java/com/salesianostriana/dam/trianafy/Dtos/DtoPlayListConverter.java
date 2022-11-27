package com.salesianostriana.dam.trianafy.Dtos;

import com.salesianostriana.dam.trianafy.model.Playlist;
import org.springframework.stereotype.Component;

@Component
public class DtoPlayListConverter {

    public Playlist dtoCreatePlayListToPlaylist(DtoCreatePlayList crG) {

        return Playlist.builder()
                .description(crG.getDescription())
                .name(crG.getName())
                .build();
    }

    public DtoGetPlaylist playListToGetPlayListDto(Playlist playlist){
        return DtoGetPlaylist
                .builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .numberOfSongs(playlist.getSongs().size())
                .build();
    }
}

