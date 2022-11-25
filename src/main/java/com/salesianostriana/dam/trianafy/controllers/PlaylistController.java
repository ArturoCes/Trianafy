package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Playlist;
import com.salesianostriana.dam.trianafy.repos.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistRepository repo;

    @GetMapping("/list/")
    public ResponseEntity<List<Playlist>> findAll() {

        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Playlist> findById(@PathVariable Long id) {

        return ResponseEntity.of(repo.findById(id));
    }

    @PostMapping("/list/")
    public ResponseEntity<Playlist> addNew(@RequestBody Playlist playList) {

        Playlist newArtist = repo.save(playList);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(newArtist));
    }

    @PutMapping("/list/{id}")
    public ResponseEntity<Playlist> edit(@RequestBody Playlist playlist, @PathVariable Long id) {

        return ResponseEntity.of(repo.findById(id).map(old -> {
            old.setName(playlist.getName());
            old.setDescription(playlist.getDescription());
            old.setSongs(playlist.getSongs());
            return Optional.of(repo.save(old));

        }).orElse(Optional.empty()));
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Artist> delete(@PathVariable Long id) {

        if (repo.existsById(id))
            repo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
