package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.RoleSpecDto;
import com.hrms.backend.entities.Role;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification {

    public static Specification<Role> getSpecification(RoleSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getRoleName() != null && !filter.getRoleName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("roleName")),
                        "%" + filter.getRoleName().trim().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
