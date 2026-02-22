package com.hrms.backend.repos;

import com.hrms.backend.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, UUID>{
    @Query(value = "SELECT * " +
            "FROM expenses e " +
            "INNER JOIN user_travels ut " +
            "ON e.fk_user_travel_id = ut.pk_user_travel_id" +
            "WHERE ut.fk_user_id = :userId", nativeQuery = true)
    List<Expense> findExpensesByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * " +
            "FROM expenses e " +
            "INNER JOIN user_travels ut " +
            "ON e.fk_user_travel_id = ut.pk_user_travel_id" +
            "WHERE ut.fk_travel_id = :travelId", nativeQuery = true)
    List<Expense> findExpensesByTravelId(@Param("travelId") UUID travelId);

    @Query(value = """
SELECT * FROM expenses e INNER JOIN user_travels ut ON e.fk_user_travel_id = ut.pk_user_travel_id WHERE ut.pk_user_travel_id = :userTravelId
""", nativeQuery = true)
    List<Expense> findExpensesByUserTravelId(@Param("userTravelId") UUID userTravelId);

}
