package ru.netology.diploma.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private String id;
    private Integer amount;
    private Date created;
    private String status;
    private String transactionId;
}
