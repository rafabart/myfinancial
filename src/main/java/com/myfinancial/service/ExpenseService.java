package com.myfinancial.service;

import com.myfinancial.entity.Expense;
import com.myfinancial.entity.enums.StatusExpense;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Expense saveExpense(Expense expense);

    Expense updateExpense(Expense expense);

    void deleteExpense(Expense expense);

    List<Expense> find(Expense expenseFilter);

    void updateStatus(Expense expense, StatusExpense statusExpense);

    void validateExpense(Expense expense);

    Optional<Expense> findById(Long id);

    BigDecimal getBalanceByUser(Long idUser);
}
