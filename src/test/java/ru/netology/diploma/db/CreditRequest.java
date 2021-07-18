package ru.netology.diploma.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequest {
    private String id;
    private String bankId;
    private Date created;
    private String status;
}
