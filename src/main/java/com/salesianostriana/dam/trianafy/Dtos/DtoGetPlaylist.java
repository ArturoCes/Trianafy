package com.salesianostriana.dam.trianafy.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoGetPlaylist {

    private Long id;
    private String name;
    private int numberOfSongs;

}
