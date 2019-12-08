package com.myfinancial.service.Impl;

import com.myfinancial.entity.Expense;
import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import com.myfinancial.exception.BusinessRuleException;
import com.myfinancial.repository.ExpenseRepository;
import com.myfinancial.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }


    @Override
    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalanceByUser(Long idUser) {

        BigDecimal incomes = expenseRepository.getBalanceByUserAndStatusAndTypeExpense(idUser, TypeExpense.RECEITA,StatusExpense.EFETIVADO);
        BigDecimal expenses = expenseRepository.getBalanceByUserAndStatusAndTypeExpense(idUser, TypeExpense.DESPESA,StatusExpense.EFETIVADO);

        if (incomes == null) {
            incomes = BigDecimal.ZERO;
        }

        if (expenses == null) {
            expenses = BigDecimal.ZERO;
        }

        return incomes.subtract(expenses);
    }


    @Override
    @Transactional
    public Expense saveExpense(Expense expense) {

        return expenseRepository.save(expense);

    }

    @Override
    @Transactional
    public Expense updateExpense(Expense expense) {

        //Verifica se o Id não é nulo, se for, lança uma excessão.
        Objects.requireNonNull(expense.getId());

        return expenseRepository.save(expense);
    }


    @Override
    public void deleteExpense(Expense expense) {

        Objects.requireNonNull(expense.getId());

        expenseRepository.delete(expense);
    }

    @Override
    public List<Expense> find(Expense expenseFilter) {

        //Pega os atributos preenchidos do objeto expenseRepository para configurar uma busca personalizada.
        //withIgnoreCase() -> Ignora maiusculas e minusculas.
        //StringMatcher.CONTAINING -> Contendo o conteudo da busca em qualquer lugar da string.
        Example example = Example.of(expenseFilter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return expenseRepository.findAll(example);
    }


    @Override
    public void updateStatus(Expense expense, StatusExpense statusExpense) {

        expense.setStatusExpense(statusExpense);

        updateExpense(expense);
    }


    @Override
    public void validateExpense(Expense expense) {

        if (expense.getDescription() == null || expense.getDescription().trim().equals("")) {
            throw new BusinessRuleException("Informe uma Descrição válida!");
        }

        if (expense.getMonth() == null || expense.getMonth() < 1 || expense.getMonth() > 12) {
            throw new BusinessRuleException("Informe uma Mês válido!");
        }

        if (expense.getYear() == null || expense.getYear().toString().length() != 4) {
            throw new BusinessRuleException("Informe um Ano válido!");
        }

        if (expense.getUser() == null || expense.getUser().getId() == null) {
            throw new BusinessRuleException("Informe um Usuário!");
        }

        if (expense.getValue() == null || expense.getValue().compareTo(BigDecimal.ZERO) < 1) {
            throw new BusinessRuleException("Informe um Valor válido!");
        }

        if (expense.getTypeExpense() == null) {
            throw new BusinessRuleException("Informe um tipo válido!");
        }
    }
}