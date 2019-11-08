package com.myfinancial.entity;

import com.myfinancial.entity.enums.StatusExpense;
import com.myfinancial.entity.enums.TypeExpense;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer month;

    @Column
    private Integer year;

    @ManyToOne
    @JoinColumn
    private User user;

    @Column
    private BigDecimal value;

    @Column
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    private LocalDate dateRegister;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeExpense typeExpense;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusExpense statusExpense;
}
