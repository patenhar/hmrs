package com.hrms.backend.repos;

import com.hrms.backend.entities.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TravelRepo extends JpaRepository<Travel, UUID>{
    Page<Travel> findAll(Specification<Travel> specification, Pageable pageable);
}
