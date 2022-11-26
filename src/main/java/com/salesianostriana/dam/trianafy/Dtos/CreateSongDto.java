package com.salesianostriana.dam.trianafy.Dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateSongDto {

    private String title;
    private Long artistId;
    private String album;
    private String year;



}
