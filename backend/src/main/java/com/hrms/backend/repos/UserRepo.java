package com.hrms.backend.repos;

import com.hrms.backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepo extends JpaRepository<User, UUID>{
    Optional<User> findByEmail(String email);

    List<User> findAllByEmailContainingIgnoreCase(String email);

    @Query("""
            SELECT DISTINCT u FROM User u
            JOIN Profile p ON p.user = u
            JOIN p.games g
            WHERE g.pkGameId = :gameId
            AND (:name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<User> findUsersByGameInterestAndProfileName(@Param("gameId") UUID gameId, @Param("name") String name);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    List<User> findAllByRole_RoleNameIgnoreCase(String roleName);
}
