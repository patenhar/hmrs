package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.UserSpecDto;
import com.hrms.backend.entities.User;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> getSpecification(UserSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + filter.getEmail().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.join("profile", JoinType.LEFT).get("name")),
                        "%" + filter.getName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getRoleName() != null && !filter.getRoleName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.join("role", JoinType.LEFT).get("roleName")),
                        "%" + filter.getRoleName().trim().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
