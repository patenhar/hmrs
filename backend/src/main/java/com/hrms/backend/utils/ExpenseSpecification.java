package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.ExpenseSpecDto;
import com.hrms.backend.entities.Expense;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecification {

    public static Specification<Expense> getSpecification(ExpenseSpecDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getUserTravelId() != null) {
                predicates.add(cb.equal(
                        root.get("userTravel").get("pkUserTravelId"),
                        filter.getUserTravelId()
                ));
            }

            if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("description")),
                        "%" + filter.getDescription().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getType() != null && !filter.getType().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("expenseType").as(String.class)),
                        "%" + filter.getType().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("expenseStatus").as(String.class)),
                        "%" + filter.getStatus().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getActor() != null && !filter.getActor().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.join("lastActionBy", JoinType.LEFT).get("email")),
                        "%" + filter.getActor().trim().toLowerCase() + "%"
                ));
            }

            if (filter.getAmountMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getAmountMin()));
            }

            if (filter.getAmountMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getAmountMax()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
