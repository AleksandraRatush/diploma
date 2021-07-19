package ru.netology.diploma.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DbUtil {

    private static BeanListHandler<Order> orderResultHandler = new BeanListHandler<>(Order.class);
    private static BeanListHandler<Payment> paymentResultHandler = new BeanListHandler<>(Payment.class);
    private static BeanListHandler<CreditRequest> creditRequestResultHandler = new BeanListHandler<>(CreditRequest.class);


    private static QueryRunner runner = new QueryRunner();

    public static List<Order> getOrders(String url) throws SQLException {
        var dataSQL = "select id, created, credit_id as creditId, payment_id as paymentId from order_entity";
        try (
                var conn = getConnection(url)
        ) {
            return runner.query(conn, dataSQL, orderResultHandler);
        }
    }

    public static List<Payment> getPayments(String url) throws SQLException {
        var dataSQL = "select id, amount, created, status, transaction_id as transactionId from payment_entity";
        try (
                var conn = getConnection(url)
        ) {
            return runner.query(conn, dataSQL, paymentResultHandler);
        }
    }

    public static List<CreditRequest> getCreditRequests(String url) throws SQLException {
        var dataSQL = "select id, bank_id as bankId, created, status from credit_request_entity";
        try (
                var conn = getConnection(url)
        ) {
            return runner.query(conn, dataSQL, creditRequestResultHandler);
        }
    }


    public static void clearDb(String url) throws SQLException {
        var orderSQL = "DELETE from order_entity";
        var paymentSQL = "DELETE from payment_entity";
        var requestSQL = "DELETE from credit_request_entity";
        try (
                var conn = getConnection(url)
        ) {
            runner.execute(conn, orderSQL);
            runner.execute(conn, paymentSQL);
            runner.execute(conn, requestSQL);
        }
    }

    private static Connection getConnection(String url) throws SQLException {
        return DriverManager.getConnection(url, "app",
                "pass");
    }


}
