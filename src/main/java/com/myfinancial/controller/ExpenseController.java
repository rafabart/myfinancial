package com.myfinancial.controller;

import com.myfinancial.dto.ExpenseDTO;
import com.myfinancial.entity.Expense;
import com.myfinancial.entity.User;
import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import com.myfinancial.exception.BusinessRuleException;
import com.myfinancial.service.Impl.ExpenseServiceImpl;
import com.myfinancial.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseServiceImpl expenseServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public ExpenseController(ExpenseServiceImpl expenseServiceImpl, UserServiceImpl userServiceImpl) {
        this.expenseServiceImpl = expenseServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }


    @PostMapping
    public ResponseEntity save(@RequestBody ExpenseDTO expenseDTO) {
        return null;
    }


    private Expense converter(ExpenseDTO expenseDTO) {

        Expense expense = new Expense();

        expense.setId(expenseDTO.getId());
        expense.setDescription(expenseDTO.getDescription());
        expense.setYear(expenseDTO.getYear());
        expense.setMonth(expenseDTO.getMonth());
        expense.setValue(expenseDTO.getValue());

        final User user = userServiceImpl
                .findById(expenseDTO.getId())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado para o Id informado!"));

        expense.setUser(user);
        expense.setTypeExpense(TypeExpense.valueOf(expenseDTO.getTypeExpense()));
        expense.setStatusExpense(StatusExpense.valueOf(expenseDTO.getStatusExpense()));

        return expense;
    }
}
