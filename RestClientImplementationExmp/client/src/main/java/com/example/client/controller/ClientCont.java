package com.example.client.controller;

import com.example.client.Game;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
public class ClientCont {

    private final RestClient restClient;

    public ClientCont(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<?> getGamesId(@PathVariable Integer id) {
        ResponseEntity<Game> entity = restClient
                .get()
                .uri("/games/{id}",id)
                .retrieve()
                .toEntity(Game.class);
        return entity;

    }
    @PostMapping("/games")
    public ResponseEntity<?> postGames(@RequestBody Game game) {
        ResponseEntity<Game> entity = restClient
                .post()
                .uri("/games")
                .retrieve()
                .toEntity(Game.class);
        return entity;

    }
    @PutMapping("/games/{id}")
    public ResponseEntity<?> putGamesId(@PathVariable Integer id,@RequestBody Game game) {
        ResponseEntity<Game> entity = restClient
                .put()
                .uri("/games/{id}",id)
                .body(game)
                .retrieve()
                .toEntity(Game.class);
        return entity;

    }
    @DeleteMapping("/games/{id}")
    public void deleteGames(@PathVariable Integer id) {
        restClient
                .delete()
                .uri("/games/{id}",id)
                .retrieve()
                .toBodilessEntity();

    }

}
