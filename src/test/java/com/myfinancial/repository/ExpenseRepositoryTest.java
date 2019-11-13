package com.myfinancial.repository;

import com.myfinancial.entity.Expense;
import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    public void mustSaveExpense() {

        Expense expense = createExpense();
        expense = expenseRepository.save(expense);

        Assertions.assertThat(expense.getId()).isNotNull();
    }


    @Test
    public void mustDeleteExpense() {
        Expense expense = createExpense();
        testEntityManager.persist(expense);

        expense = testEntityManager.find(Expense.class, expense.getId());

        expenseRepository.delete(expense);

        Expense expenseFind = testEntityManager.find(Expense.class, expense.getId());

        Assertions.assertThat(expenseFind).isNull();
    }


    @Test
    public void mustUpdateExpense() {

        Expense expense = createExpense();

        expense.setYear(2019);
        expense.setDescription("Teste atualizar");
        expense.setStatusExpense(StatusExpense.CANCELADO);

        expenseRepository.save(expense);

        Expense expenseUpdate = testEntityManager.find(Expense.class, expense.getId());

        Assertions.assertThat(expenseUpdate.getYear()).isEqualTo(2019);
        Assertions.assertThat(expenseUpdate.getDescription()).isEqualTo("Teste atualizar");
        Assertions.assertThat(expenseUpdate.getStatusExpense()).isEqualTo(StatusExpense.CANCELADO);
    }


    @Test
    public void mustFindExpenseById() {
        Expense expense = createExpense();
        testEntityManager.persist(expense);

        Optional<Expense> expenseOptional = expenseRepository.findById(expense.getId());
        Assertions.assertThat(expenseOptional.isPresent()).isTrue();
    }


    private Expense createExpense() {
        return Expense.builder()
                .year(2019)
                .month(11)
                .description("lançamento qualquer")
                .value(new BigDecimal(150))
                .typeExpense(TypeExpense.RECEITA)
                .statusExpense(StatusExpense.PENDENTE)
                .dateRegister(LocalDate.now())
                .build();
    }
}
