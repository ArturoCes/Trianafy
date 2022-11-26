package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;

import com.salesianostriana.dam.trianafy.repos.SongRepository;
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

@Controller
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistRepository repo;
    private final SongRepository repoSong;

    @Operation(summary = "Obtiene todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado artistas",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 1, "nombre": "Aitana"},
                                                {"id": 2, "nombre": "Selena Gomez"}
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún artista",
                    content = @Content),
    })
    @GetMapping("/artist/")
    public ResponseEntity<List<Artist>> findAll() {
        List<Artist> listaArtistas = repo.findAll();

        if (listaArtistas.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .ok()
                    .body(listaArtistas);
        }
    }

    @Operation(summary = "Se busca a un artista por el ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha encontrado un artista relacionado con ese ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encontró a un artista con ese ID",
                    content = @Content),
    })
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findById(@PathVariable Long id) {

        if (repo.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(repo.findById(id));
        }
    }

    @Operation(summary = "Crea un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado un artista",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Artist.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {"id": 13, "nombre": "Aitana"},
                                                                                 
                                            ]                                          
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "Este artista ya existe")
    })
    @PostMapping("/artist/")
    public ResponseEntity<Artist> addNew(@RequestBody Artist artist) {

        if (artist.getName().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .build();
        } else {
            Artist newArtist = repo.save(artist);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newArtist);
        }
    }

    @Operation(summary = "Edita las propiedades de un artista por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "El artista ha sido modificado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningún artista relacionado con ese ID",
                    content = @Content),
    })
    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist> editArtist(@RequestBody Artist artist, @PathVariable Long id) {
        if (repo.findById(id).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } else {
            return ResponseEntity.of(
                    repo.findById(id).map(old -> {
                        old.setName(artist.getName());
                        repo.save(old);
                        return old;
                    })
            );
        }
    }

    @Operation(summary = "Elimina al artista que tenga el ID seleccionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "El artista ha sido removido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra el artista con ese ID",
                    content = @Content),
    })
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<Artist> delete(@PathVariable Long id) {
        Optional<Artist> artist = repo.findById(id);
        if (artist.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            List<Song> songList = repoSong.findByArtist(artist.get());
            for (Song s : songList) {
                s.setArtist(null);
                repoSong.save(s);

            }
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }
}

