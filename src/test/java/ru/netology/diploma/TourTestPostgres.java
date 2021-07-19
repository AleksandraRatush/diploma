package ru.netology.diploma;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.db.CreditRequest;
import ru.netology.diploma.db.DbUtil;
import ru.netology.diploma.db.Order;
import ru.netology.diploma.db.Payment;
import ru.netology.diploma.page.CardPage;
import ru.netology.diploma.page.MainPage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class TourTestPostgres {

    public static final int TIME_DELTA_MILLS = 10000;
    public static final String APPROVED_STATUS = "APPROVED";
    private static final String POSTGRES_URL =  "jdbc:postgresql://localhost:5432/app";


    @BeforeEach
    public void login() {
        open("http://localhost:8081/");
    }

    @Test
    public void successDebt() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        List<Order> orders = DbUtil.getOrders(POSTGRES_URL);
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        Date now = new Date();
        assertTrue(now.getTime() - order.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNull(order.getCreditId());
        assertNotNull(order.getPaymentId());
        assertNotNull(order.getId());
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(POSTGRES_URL);
        assertTrue(creditRequests.isEmpty());
        List<Payment> payments = DbUtil.getPayments(POSTGRES_URL);
        Payment payment = payments.get(0);
        assertEquals(1, payments.size());
        assertTrue(now.getTime() - payment.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(payment.getId());
        assertNotNull(payment.getTransactionId());
        assertEquals(APPROVED_STATUS, payment.getStatus());
        assertEquals(45000, payment.getAmount());
        assertEquals(order.getPaymentId(), payment.getId());
    }


    @AfterEach
    public void clear() throws SQLException {
        DbUtil.clearDb(POSTGRES_URL);
    }
}
