package ru.netology.diploma.test.util;

import ru.netology.diploma.data.Status;
import ru.netology.diploma.db.CreditRequest;
import ru.netology.diploma.db.DbUtil;
import ru.netology.diploma.db.Order;
import ru.netology.diploma.db.Payment;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtil {

    public static final int TIME_DELTA_MILLS = 20000;
    public static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/app";
    public static final String MY_SQL_URL = "jdbc:mysql://localhost:3306/app";

    private TestUtil() {

    }

    public static Order checkOrder(boolean isCredit,  String dbUrl) throws SQLException {
        List<Order> orders = DbUtil.getOrders(dbUrl);
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        Date now = new Date();
        assertTrue(now.getTime() - order.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(order.getPaymentId());
        assertNotNull(order.getId());
        if (isCredit) {
            assertNotNull(order.getCreditId());
        } else {
            assertNull(order.getCreditId());
        }
        return order;
    }

    public static void checkSuccessStateInDBDebt(String dbUrl) throws SQLException {
        Order order = checkOrder(false, dbUrl);
        checkCreditRequestNotExists(dbUrl);
        checkPayment(order.getPaymentId(), "45000", Status.APPROVED, MY_SQL_URL);
    }

    public static void checkSuccessStateInDBCredit(String dbUrl) throws SQLException {
        Order order = checkOrder(true, dbUrl);
        TestUtil.checkCreditRequest(Status.APPROVED, dbUrl);
        TestUtil.checkPayment(order.getPaymentId(), "45000", Status.APPROVED, MY_SQL_URL);
    }

    public static CreditRequest checkCreditRequest(Status expectedStatus, String dbUrl) throws SQLException {
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(dbUrl);
        assertFalse(creditRequests.isEmpty());
        CreditRequest creditRequest = creditRequests.get(0);
        assertNotNull(creditRequest.getId());
        assertEquals(expectedStatus.name(), creditRequest.getStatus());
        assertNotNull(creditRequest.getBankId());
        return creditRequest;
    }

    public static void checkCreditRequestNotExists(String dbUrl) throws SQLException {
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(dbUrl);
        assertTrue(creditRequests.isEmpty());
    }


    public static Payment checkPayment(String expectedPaymentId, String expectedPaymentAmount, Status status,
                                       String dbUrl) throws SQLException {
        Date now = new Date();
        List<Payment> payments = DbUtil.getPayments(dbUrl);
        Payment payment = payments.get(0);
        assertEquals(1, payments.size());
        assertTrue(now.getTime() - payment.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(payment.getId());
        assertNotNull(payment.getTransactionId());
        assertEquals(status.name(), payment.getStatus());
        assertEquals(expectedPaymentAmount, payment.getAmount().toString());
        assertEquals(expectedPaymentId, payment.getId());
        return payment;
    }

    public static void checkDbIsEmpty( String dbUrl) throws SQLException {
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(dbUrl);
        assertTrue(creditRequests.isEmpty());
        List<Order> orders = DbUtil.getOrders(dbUrl);
        assertTrue(orders.isEmpty());
        List<Payment> payments = DbUtil.getPayments(dbUrl);
        assertTrue(payments.isEmpty());
    }
}
