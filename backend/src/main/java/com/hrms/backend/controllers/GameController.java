package com.hrms.backend.controllers;

import com.hrms.backend.dtos.request.GameInDto;
import com.hrms.backend.dtos.request.GameReqDto;
import com.hrms.backend.dtos.response.GameResDto;
import com.hrms.backend.entities.Game;
import com.hrms.backend.services.GameService;
import com.hrms.backend.utils.ApiResponse;
import com.hrms.backend.validations.OnCreate;
import com.hrms.backend.validations.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/filtering&pagination&sorting")
    @PreAuthorize("hasAuthority('VIEW_GAME') or hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<Page<GameResDto>> getGamesPaginated(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", defaultValue = "[{\"field\":\"gameName\",\"direction\":\"asc\"}]") String sort,
            @RequestParam(name = "game-name", required = false) String gameName,
            @RequestParam(name = "max-players-min", required = false) Integer maxPlayersMin,
            @RequestParam(name = "max-players-max", required = false) Integer maxPlayersMax
    ) {
        Page<GameResDto> games = gameService.searchGamesWithPaginationSortingAndFiltering(
                GameInDto.builder()
                        .gameName(normalizeOptionalString(gameName))
                        .maxPlayersMin(maxPlayersMin)
                        .maxPlayersMax(maxPlayersMax)
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .build());
        return ResponseEntity.ok(games);
    }

    private String normalizeOptionalString(String value) {
        if (value == null) return null;
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_GAME') or hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<List<GameResDto>>> getAllGames() {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("All games fetched successfully", gameService.getAllGames()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_GAME') or hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<GameResDto>> getGameById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game fetched successfully", gameService.getGameById(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('VIEW_GAME') or hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<List<GameResDto>>> getGamesByGameNameContainingIgnoreCase(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game fetched successfully", gameService.getGamesByGameNameContainingIgnoreCase(name)));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<GameResDto>> addGame(@RequestBody @Validated(OnCreate.class) GameReqDto gameReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game added successfully", gameService.addGame(gameReqDto)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<GameResDto>> updateGame(@PathVariable UUID id, @RequestBody @Validated(OnUpdate.class) GameReqDto gameReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Game updated successfully", gameService.updateGame(id, gameReqDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_ALL_GAME')")
    public ResponseEntity<ApiResponse<String>> deleteGame(@PathVariable UUID id) {
        String res = "Game not deleted";
        if (gameService.deleteGame(id)){
            res = "Game deleted successfully";
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(res, null ));
    }
}
