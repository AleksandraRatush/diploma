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

public class TourTest {

    public static final int TIME_DELTA_MILLS = 10000;
    public static final String APPROVED_STATUS = "APPROVED";
    public static final String DECLINED_STATUS = "DECLINED";
    private static final String MY_SQL_URL =  "jdbc:mysql://localhost:3306/app";


    @BeforeEach
    public void login() {
        open("http://localhost:8080/");
    }

    @Test
    public void successDebt() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBDebt();


    }

    private void checkSuccessStateInDBDebt() throws SQLException {
        List<Order> orders = DbUtil.getOrders(MY_SQL_URL);
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        Date now = new Date();
        assertTrue(now.getTime() - order.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNull(order.getCreditId());
        assertNotNull(order.getPaymentId());
        assertNotNull(order.getId());
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(MY_SQL_URL);
        assertTrue(creditRequests.isEmpty());
        List<Payment> payments = DbUtil.getPayments(MY_SQL_URL);
        Payment payment = payments.get(0);
        assertEquals(1, payments.size());
        assertTrue(now.getTime() - payment.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(payment.getId());
        assertNotNull(payment.getTransactionId());
        assertEquals(APPROVED_STATUS, payment.getStatus());
        assertEquals(45000, payment.getAmount());
        assertEquals(order.getPaymentId(), payment.getId());
    }

    @Test
    public void successCredit() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(MY_SQL_URL);
        assertEquals(1, creditRequests.size());
        CreditRequest creditRequest = creditRequests.get(0);
        assertEquals(APPROVED_STATUS, creditRequest.getStatus());
        assertNotNull(creditRequest.getBankId());
        Date now = new Date();
        assertTrue(now.getTime() - creditRequest.getCreated().getTime() < TIME_DELTA_MILLS);
        List<Order> orders = DbUtil.getOrders(MY_SQL_URL);
        assertEquals(1, orders.size());
        Order order = orders.get(0);
        assertTrue(now.getTime() - order.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(order.getPaymentId());
        assertNotNull(order.getId());
        assertNotNull(order.getCreditId());
        assertEquals(creditRequest.getId(), order.getCreditId());
        List<Payment> payments = DbUtil.getPayments(MY_SQL_URL);
        Payment payment = payments.get(0);
        assertEquals(1, payments.size());
        assertTrue(now.getTime() - payment.getCreated().getTime() < TIME_DELTA_MILLS);
        assertNotNull(payment.getId());
        assertNotNull(payment.getTransactionId());
        assertEquals(APPROVED_STATUS, payment.getStatus());
        assertEquals(45000, payment.getAmount());
        assertEquals(order.getPaymentId(), payment.getId());
    }

    @Test
    public void invalidCardDebt() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutCardInfo());
        cardPage.checkCardInvalidFormatVisible();
        DataGenerator.CardInfo cardInfo = DataGenerator.generateWithInvalidCardCardInfo();
        cardPage.fillCardDataAndCommit(cardInfo);
        cardPage.checkCardInvalidFormatVisible();
        checkDbIsEmpty();
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addCardDigits(Character.toString(a));
            }
            assertEquals(cardInfo.getCardNumber(), cardPage.getCardValue());
        }
        cardPage.addCardDigits("1");
        String validCard = DataGenerator.generateValidCardInfo().getCardNumber();
        assertEquals(validCard, cardPage.getCardValue());
        cardPage.addCardDigits("2");
        assertEquals(validCard, cardPage.getCardValue());
        cardPage.commit();
        cardPage.checkSuccess();
        cardPage.checkCardInvalidFormatNotVisible();
        checkSuccessStateInDBDebt();
    }

    private void checkDbIsEmpty() throws SQLException {
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(MY_SQL_URL);
        assertTrue(creditRequests.isEmpty());
        List<Order> orders = DbUtil.getOrders(MY_SQL_URL);
        assertTrue(orders.isEmpty());
        List<Payment> payments = DbUtil.getPayments(MY_SQL_URL);
        assertTrue(payments.isEmpty());
    }

    @Test
    public void declinedCredit() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateDeclinedCardInfo());
        cardPage.checkDecline();
        List<CreditRequest> creditRequests = DbUtil.getCreditRequests(MY_SQL_URL);
        assertEquals(1, creditRequests.size());
        CreditRequest creditRequest = creditRequests.get(0);
        assertEquals(DECLINED_STATUS, creditRequest.getStatus());
        assertNotNull(creditRequest.getBankId());
        Date now = new Date();
        assertTrue(now.getTime() - creditRequest.getCreated().getTime() < TIME_DELTA_MILLS);
        List<Order> orders = DbUtil.getOrders(MY_SQL_URL);
        assertTrue(orders.isEmpty());
        List<Payment> payments = DbUtil.getPayments(MY_SQL_URL);
        assertTrue(payments.isEmpty());

     }

    @AfterEach
    public void clear() throws SQLException {
        DbUtil.clearDb(MY_SQL_URL);
    }
}
