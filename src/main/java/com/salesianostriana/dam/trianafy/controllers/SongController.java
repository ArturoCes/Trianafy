package com.salesianostriana.dam.trianafy.controllers;

import com.salesianostriana.dam.trianafy.model.Artist;
import com.salesianostriana.dam.trianafy.model.Song;
import com.salesianostriana.dam.trianafy.repos.ArtistRepository;
import com.salesianostriana.dam.trianafy.repos.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SongController{
    private final SongRepository repo;

    @GetMapping("/song/")
    public ResponseEntity<List<Song>> findAll() {
        return ResponseEntity.ok(repo.findAll());
    }
    @GetMapping("/song/{id}")
    public ResponseEntity<Song> findById(@PathVariable Long id) {
        return ResponseEntity.of(repo.findById(id));
    }
    @PostMapping("/song/")
    public ResponseEntity<Song> addNew(@RequestBody Song song) {
        Song newSong = repo.save(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(newSong));
    }
    @PutMapping("/song/{id}")
    public ResponseEntity<Song>edit(@RequestBody Song song, @PathVariable Long id){

        return ResponseEntity.of(repo.findById(id).map(old->{
            old.setAlbum(song.getAlbum());
            old.setYear(song.getYear());
            old.setTitle(song.getTitle());
            return Optional.of(repo.save(old));

        }).orElse(Optional.empty()));
    }
    @DeleteMapping("/song/{id}")
    public ResponseEntity<Song>delete(@PathVariable Long id){
        if(repo.existsById(id))
            repo.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
