package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.GameBookingSpecDto;
import com.hrms.backend.entities.GameBooking;
import com.hrms.backend.entities.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GameBookingSpecification {

    public static Specification<GameBooking> getSpecification(GameBookingSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUserId() != null) {
                Join<GameBooking, User> teamMembers = root.join("teamMembers", JoinType.INNER);
                predicates.add(cb.equal(teamMembers.get("pkUserId"), filter.getUserId()));
                if (query != null) {
                    query.distinct(true);
                }
            }

            if (filter.getGameName() != null && !filter.getGameName().isBlank()) {
                Join<Object, Object> gameSlot = root.join("gameSlot", JoinType.LEFT);
                Join<Object, Object> game = gameSlot.join("game", JoinType.LEFT);
                predicates.add(cb.like(
                        cb.lower(game.get("gameName")),
                        "%" + filter.getGameName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getStatusName() != null && !filter.getStatusName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("gameBookingStatus").as(String.class)),
                        "%" + filter.getStatusName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getDateFrom() != null || filter.getDateTo() != null) {
                Join<Object, Object> slotJoin = root.join("gameSlot", JoinType.LEFT);
                if (filter.getDateFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(slotJoin.get("date"), filter.getDateFrom()));
                }
                if (filter.getDateTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(slotJoin.get("date"), filter.getDateTo()));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
