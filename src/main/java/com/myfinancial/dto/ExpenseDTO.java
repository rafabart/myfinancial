package com.myfinancial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {

    private Long id;

    private String description;

    private Integer month;

    private Integer year;

    private Long userId;

    private BigDecimal value;

    private String typeExpense;

    private String statusExpense;
}
