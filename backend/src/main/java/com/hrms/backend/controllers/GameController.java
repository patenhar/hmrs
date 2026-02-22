package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.GameReqDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.services.GameService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<GameResDto>>> getAllGames() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All games fetched successfully", gameService.getAllGames()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<GameResDto>> getGameById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game fetched successfully", gameService.getGameById(id)));
    }

    @GetMapping("/search")
//    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<ApiResponse<List<GameResDto>>> getGamesByGameNameContainingIgnoreCase(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game fetched successfully", gameService.getGamesByGameNameContainingIgnoreCase(name)));
    }

    @PostMapping("/")
//    @PreAuthorize("hasAuthority('ADD_ROLE')")
    public ResponseEntity<ApiResponse<GameResDto>> addGame(@RequestBody @Validated(OnCreate.class) GameReqDto gameReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game added successfully", gameService.addGame(gameReqDto)));
    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<GameResDto>> updateGame(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) GameReqDto gameReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game updated successfully", gameService.updateGame(id, gameReqDto)));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('MANAGE_USER')")
    public ResponseEntity<ApiResponse<String>> deleteGame(@PathVariable UUID id) {
        String res = "Game not deleted";
        if (gameService.deleteGame(id)){
            res = "Game deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
