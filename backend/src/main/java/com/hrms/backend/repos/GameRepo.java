package com.hrms.backend.repos;

import com.hrms.backend.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, UUID> {
    List<Game> findGamesByGameNameContainingIgnoreCase(String gameName);
}
