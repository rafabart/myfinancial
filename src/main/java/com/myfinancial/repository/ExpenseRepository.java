package com.myfinancial.repository;

import com.myfinancial.entity.Expense;
import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value =
            " select sum(e.value) from Expense e join e.user u " +
                    " where u.id = :idUser and e.typeExpense = :type and e.statusExpense = :status group by u ")
    BigDecimal getBalanceByUserAndStatusAndTypeExpense(
            @Param("idUser") Long idUser,
            @Param("type") TypeExpense type,
            @Param("status") StatusExpense status);
}
