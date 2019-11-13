package com.myfinancial.controller;

import com.myfinancial.dto.ExpenseDTO;
import com.myfinancial.dto.StatusExpenseDTO;
import com.myfinancial.entity.Expense;
import com.myfinancial.entity.User;
import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import com.myfinancial.exception.BusinessRuleException;
import com.myfinancial.service.Impl.ExpenseServiceImpl;
import com.myfinancial.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor -> Cria um contrutor automaticamente para a classe com os atributos declarados como 'final'
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

        try {
            expenseDTO.setStatusExpense("PENDENTE");
            Expense expense = converter(expenseDTO);
            expense = expenseServiceImpl.saveExpense(expense);
            return new ResponseEntity(expense, HttpStatus.CREATED);

        } catch (BusinessRuleException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody ExpenseDTO expenseDTO) {

        return expenseServiceImpl.findById(id).map(entity -> {
            try {
                Expense expense = converter(expenseDTO);
                expense.setId(entity.getId());
                expenseServiceImpl.updateExpense(expense);
                return ResponseEntity.ok(expense);

            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado na base de dados!", HttpStatus.BAD_REQUEST));
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return expenseServiceImpl.findById(id).map(entity -> {
            expenseServiceImpl.deleteExpense(entity);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado na base de dados!", HttpStatus.BAD_REQUEST));
    }


    @PutMapping("{id}/updateStatusExpense")
    public ResponseEntity updateStatusExpense(@PathVariable("id") Long id, @RequestBody StatusExpenseDTO statusExpenseDTO) {

        return expenseServiceImpl.findById(id).map(entity -> {
            StatusExpense statusExpense = StatusExpense.valueOf(statusExpenseDTO.getStatus());

            if (statusExpense == null) {
                return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido!");
            }

            try {
                entity.setStatusExpense(statusExpense);
                expenseServiceImpl.updateExpense(entity);
                return ResponseEntity.ok(entity);
            } catch (BusinessRuleException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado na base de dados!", HttpStatus.BAD_REQUEST));
    }


    @GetMapping
    public ResponseEntity find(
            //Outra opção seria usa 'Map', mas isso torna os valores obrigatórios.
            //@RequestParam java.util.Map<String, String> params
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "value", required = false) BigDecimal value,
            @RequestParam("userId") Long userId
    ) {
        Expense expenseFilter = new Expense();
        expenseFilter.setDescription(description);
        expenseFilter.setMonth(month);
        expenseFilter.setYear(year);
        expenseFilter.setValue(value);

        Optional<User> userOptional = userServiceImpl.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o Id informado!");
        } else {
            expenseFilter.setUser(userOptional.get());
        }

        List<Expense> expenseList = expenseServiceImpl.find(expenseFilter);
        return ResponseEntity.ok(expenseList);
    }


    private Expense converter(ExpenseDTO expenseDTO) {

        Expense expense = new Expense();

        expense.setId(expenseDTO.getId());
        expense.setDescription(expenseDTO.getDescription());
        expense.setYear(expenseDTO.getYear());
        expense.setMonth(expenseDTO.getMonth());
        expense.setValue(expenseDTO.getValue());

        final User user = userServiceImpl
                .findById(expenseDTO.getUserId())
                .orElseThrow(() -> new BusinessRuleException("Usuário não encontrado para o Id informado!"));

        expense.setUser(user);
        if (expenseDTO.getTypeExpense() != null) {
            expense.setTypeExpense(TypeExpense.valueOf(expenseDTO.getTypeExpense()));
        }
        if (expenseDTO.getStatusExpense() != null) {
            expense.setStatusExpense(StatusExpense.valueOf(expenseDTO.getStatusExpense()));
        }
        return expense;
    }
}
