package com.salesianostriana.dam.trianafy.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoGetSong {
    private Long id;
    private String title;
    private String album;
    private String year;
    private String artist;
}
