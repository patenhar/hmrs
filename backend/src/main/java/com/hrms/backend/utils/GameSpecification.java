package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.GameSpecDto;
import com.hrms.backend.entities.Game;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GameSpecification {

    public static Specification<Game> getSpecification(GameSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getGameName() != null && !filter.getGameName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("gameName")),
                        "%" + filter.getGameName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getMaxPlayersMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxPlayers"), filter.getMaxPlayersMin()));
            }

            if (filter.getMaxPlayersMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPlayers"), filter.getMaxPlayersMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
