package com.hrms.backend.utils;

import com.hrms.backend.dtos.spec.TravelSpecDto;
import com.hrms.backend.entities.Travel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TravelSpecification {

    public static Specification<Travel> getSpecification(TravelSpecDto filterDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getTitle() != null && !filterDto.getTitle().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filterDto.getTitle().trim().toLowerCase() + "%"
                ));
            }

            if (filterDto.getDescription() != null && !filterDto.getDescription().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + filterDto.getDescription().trim().toLowerCase() + "%"
                ));
            }

            if (filterDto.getTravelDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("travelDate"),filterDto.getTravelDate()));
            } else if (filterDto.getTravelDateKeyword() != null && !filterDto.getTravelDateKeyword().isBlank()) {
                String keyword = "%" + filterDto.getTravelDateKeyword().trim() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.function("FORMAT", String.class, root.get("travelDate"), criteriaBuilder.literal("yyyy-MM-dd")),
                        keyword
                ));
            }

            if (filterDto.getReturnDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("returnDate"),filterDto.getReturnDate()));
            } else if (filterDto.getReturnDateKeyword() != null && !filterDto.getReturnDateKeyword().isBlank()) {
                String keyword = "%" + filterDto.getReturnDateKeyword().trim() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.function("FORMAT", String.class, root.get("returnDate"), criteriaBuilder.literal("yyyy-MM-dd")),
                        keyword
                ));
            }

            if (filterDto.getMaxGrantPerDay() != null) {
                predicates.add(criteriaBuilder.equal(root.get("maxGrantPerDay"), filterDto.getMaxGrantPerDay()));
            } else {
                if (filterDto.getMaxGrantPerDayMin() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("maxGrantPerDay"), filterDto.getMaxGrantPerDayMin()));
                }
                if (filterDto.getMaxGrantPerDayMax() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxGrantPerDay"), filterDto.getMaxGrantPerDayMax()));
                }
            }

            if (filterDto.getHrMail() != null && !filterDto.getHrMail().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("hrMail")),
                        "%" + filterDto.getHrMail().trim().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }

}
