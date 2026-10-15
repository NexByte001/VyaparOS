package com.vyparos.vyparos.finance.repository;

import com.vyparos.vyparos.finance.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndBusinessId(Long id, Long businessId);

    @Query("SELECT e FROM Expense e " +
           "WHERE e.business.id = :businessId " +
           "AND (:category IS NULL OR LOWER(e.category) = LOWER(:category)) " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate)")
    Page<Expense> findFilteredExpenses(
            @Param("businessId") Long businessId,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.business.id = :businessId " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate)")
    BigDecimal sumExpenses(
            @Param("businessId") Long businessId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e " +
           "WHERE e.business.id = :businessId " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate) " +
           "GROUP BY e.category")
    List<Object[]> sumExpensesByCategory(
            @Param("businessId") Long businessId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
