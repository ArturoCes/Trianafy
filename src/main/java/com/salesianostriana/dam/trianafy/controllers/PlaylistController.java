package com.salesianostriana.dam.trianafy.controllers;


import com.salesianostriana.dam.trianafy.Dtos.DtoCreatePlayList;
import com.salesianostriana.dam.trianafy.Dtos.DtoGetPlaylist;
import com.salesianostriana.dam.trianafy.Dtos.DtoPlayListConverter;
import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.service.PlaylistService;
import com.salesianostriana.dam.trianafy.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    private final SongService songService;
    private final DtoPlayListConverter dtoConverter;

    @Operation(summary = "Obtiene todas las listas de reproducción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 OK",
                    description = "Se ha encontrado listas de reproducción",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": 12,
                                                    "name": "Random",
                                                    "numberOfSongs": 2
                                                }
                                            ]                                         
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404 NOT FOUND",
                    description = "No se ha encontrado ninguna lista de reproducción",
                    content = @Content),
    })
    @GetMapping("/list")
    public ResponseEntity<List<DtoGetPlaylist>> findAll() {

        List<Playlist> playlist = playlistService.findAll();

        if (playlist.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        List<DtoGetPlaylist> listDto =
                playlist.stream()
                        .map(dtoConverter::playListToGetPlayListDto)
                        .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
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
    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findOne(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            return ResponseEntity
                    .of(playlistService.findById(id));
        }
    }

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
    @PostMapping("/list")
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
    @PutMapping("/list/{id}")
    public ResponseEntity<DtoGetPlaylist> editPlaylist(@io.swagger.v3.oas.annotations.parameters
            .RequestBody(description = "Es el cuerpo de la petición ", content = @Content(examples = @ExampleObject("""
                [
                    {
                        "id": 12,
                        "name": "Big Fan",
                        "numberOfSongs": 5
                    }
                ]
            """))) @RequestBody DtoCreatePlayList dtoCreatePlayList, @PathVariable Long id) {


        Optional<Playlist> playlist1 = playlistService.findById(id);


        if (playlist1.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            playlist1.get().setName(dtoCreatePlayList.getName());
            playlist1.get().setDescription(dtoCreatePlayList.getDescription());
            playlistService.add(playlist1.get());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dtoConverter.playListToGetPlayListDto(playlist1.get()));
        }
    }

    @Operation(summary = "Elimina una Playlist por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "La Playlist se ha borrado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra una Playlist relacionada con este ID",
                    content = @Content),
    })
    @DeleteMapping("/list/{id}")
    public ResponseEntity<Playlist> delete(@PathVariable Long id) {
        if (playlistService.findById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        playlistService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
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
    @GetMapping("/list/{id}/song")
    public ResponseEntity<List<Song>> findAllSongs(@PathVariable Long id) {

        Optional<Playlist> list = playlistService.findById(id);
        if (list.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(list.get().getSongs());
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
    @GetMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Song> findSongInPlaylistById(@PathVariable Long id1, @PathVariable Long id2) {
        Optional<Song> song = songService.findById(id2);
        Optional<Playlist> playlist = playlistService.findById(id1);

        if (playlist.isEmpty() || song.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            List<Song> songList = playlistService.findById(id1).get()
                    .getSongs()
                    .stream()
                    .filter(song1 -> (song1.getId() == song.get().getId())).collect(Collectors.toList());
            if (songList.size() == 0) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(songList.get(0));
        }
    }

    @Operation(summary = "Añade una canción por ID a una playlist por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha añadido correctamente la canción a la playlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra una playlist o una canción vinculada a ese ID",
                    content = @Content),
    })
    @PostMapping("/list/{id1}/song/{id2}")
    public ResponseEntity<Playlist> addSongInPlayListById(@PathVariable Long id1, @PathVariable Long id2) {
        Optional<Song> song = songService.findById(id2);
        Optional<Playlist> playlist = playlistService.findById(id1);

        if (song.isEmpty() || playlist.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            playlist.get().addSong(song.get());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(playlistService.add(playlist.get()));
        }
    }

    @Operation(summary = "Borra una canción de la Playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "La canción se ha removido de manera satisfactoria",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No existe la canción dentro de la Playlist especificada",
                    content = @Content),
    })
    @DeleteMapping("/list/{id}/song/{id2}")
    public ResponseEntity<Playlist> deleteSong(@PathVariable Long id, @PathVariable Long id2) {
        Optional<Song> song = songService.findById(id2);
        Optional<Playlist> playlist = playlistService.findById(id);

        if (playlist.isEmpty() || song.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            List<Song>songs = playlist.get().getSongs()
                    .stream().filter(s -> s!=song.get()).collect(Collectors.toList());
            playlist.get().setSongs(songs);
            playlistService.add(playlist.get());
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }

}
