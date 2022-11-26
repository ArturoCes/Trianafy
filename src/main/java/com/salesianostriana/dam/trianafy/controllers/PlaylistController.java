package com.salesianostriana.dam.trianafy.controllers;


import com.salesianostriana.dam.trianafy.Dtos.DtoCreatePlayList;
import com.salesianostriana.dam.trianafy.Dtos.DtoGetPlaylist;
import com.salesianostriana.dam.trianafy.Dtos.DtoPlayListConverter;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final SongRepository songRepository;
    private final DtoPlayListConverter dtoConverter;

    @Operation(summary = "Crea una playlist nueva")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado una playlist correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DtoCreatePlayList.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La playlist no tiene nombre",
                    content = @Content),
    })
    @PostMapping("/playlist/")
    public ResponseEntity<Playlist> create(@RequestBody DtoCreatePlayList newPlayList) {
        if (newPlayList.getName().isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();

        }
        Playlist playlist = dtoConverter.dtoCreatePlayListToPlaylist(newPlayList);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(playlistService.add(playlist));
    }

    @Operation(summary = "Obtiene todas las listas de reproducción existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado una o varias playlists",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DtoGetPlaylist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna playlist",
                    content = @Content),
    })
    @GetMapping("/playlist/")
    public ResponseEntity<List<DtoGetPlaylist>> findAll() {

        List<Playlist> pl = playlistService.findAll();

        if (pl.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        List<DtoGetPlaylist> listDto =
                pl.stream()
                        .map(dtoConverter::getPlayListToPlatListDto)
                        .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(listDto);
    }

    @Operation(summary = "Obtiene la información de una playlist por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado la playlist con ese ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DtoGetPlaylist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una playlist con ese ID",
                    content = @Content),
    })
    @GetMapping("/playlist/{id}")
    public ResponseEntity<Playlist> findOne(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(playlistService.findById(id));
        }
    }

    @Operation(summary = "Editar una playlist por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado correctamente la playlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra la playlist con ese Id",
                    content = @Content),
    })
    @PutMapping("/playlist/{id}")
    public ResponseEntity<Playlist> editPlaylist(@RequestBody Playlist playlist, @PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            return ResponseEntity.of(
                    playlistService.findById(id).map(old -> {
                        old.setName(playlist.getName());
                        old.setDescription(playlist.getDescription());
                        playlistService.add(old);
                        return old;
                    })
            );
        }
    }

    @Operation(summary = "Borra una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "La playlist se ha borrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la playlist",
                    content = @Content),
    })
    @DeleteMapping("/playlist/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        playlistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca todas las canciones de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se devuelve correctamente la lista de canciones",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La playlist con el ID que buscamos no existe",
                    content = @Content)
    })
    @GetMapping("/playlist/songs/{id}/songs")
    public ResponseEntity<Playlist> findAllSongs(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return (ResponseEntity<Playlist>) ResponseEntity
                    .ok()
                    .body(playlistService.findById(id).get());
        }
    }

    @Operation(summary = "Busca una canción dentro de una lista de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se devuelve correctamente la canción",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "La playlist con el ID que buscamos no existe o la canción que buscamos no existe",
                    content = @Content)
    })
    @GetMapping("/playlist/{id1}/songs/{id2}")
    public ResponseEntity<Song> findSongInPlaylistById(@PathVariable Long id1, @PathVariable Long id2) {
        if (playlistService.findById(id1).isEmpty() || songRepository.findById(id2).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            Song cancionBuscada = playlistService.findById(id1).get().getSongs().get(Math.toIntExact(id2) - 1);
            return ResponseEntity
                    .ok()
                    .body(cancionBuscada);
        }
    }

}
