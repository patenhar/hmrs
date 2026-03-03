package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.ReferralSpecDto;
import com.hrms.backend.entities.Referral;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReferralSpecification {

    public static Specification<Referral> getSpecification(ReferralSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getName().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + filter.getEmail().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getJobTitle() != null && !filter.getJobTitle().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.join("job", JoinType.LEFT).get("title")),
                        "%" + filter.getJobTitle().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("referralStatus").as(String.class)),
                        "%" + filter.getStatus().trim().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
