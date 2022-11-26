package com.salesianostriana.dam.trianafy.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoCreatePlayList {

    private String name;
    private String description;
}
