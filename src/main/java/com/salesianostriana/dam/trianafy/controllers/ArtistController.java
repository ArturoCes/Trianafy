package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    @Operation(summary = "Obtiene todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado artistas",
                    content = { @Content(mediaType = "application/json",
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
        return ResponseEntity.ok(repo.findAll());
    }
    @GetMapping("/artist/{id}")
    public ResponseEntity<Artist> findById(@PathVariable Long id) {

        return ResponseEntity.of(repo.findById(id));
    }
    @PostMapping("/artist/")
    public ResponseEntity<Artist> addNew(@RequestBody Artist artist) {

        Artist newArtist = repo.save(artist);

        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(newArtist));
    }
    @PutMapping("/artist/{id}")
    public ResponseEntity<Artist>edit(@RequestBody Artist artist, @PathVariable Long id){

        return ResponseEntity.of(repo.findById(id).map(old->{
                old.setName(artist.getName());
            return Optional.of(repo.save(old));

        }).orElse(Optional.empty()));
    }
    @DeleteMapping("/artist/{id}")
    public ResponseEntity<Artist>delete(@PathVariable Long id){
        if(repo.existsById(id))
            repo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
