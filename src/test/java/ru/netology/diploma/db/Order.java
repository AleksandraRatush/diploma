package ru.netology.diploma.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private Date created;
    private String creditId;
    private String paymentId;
}
