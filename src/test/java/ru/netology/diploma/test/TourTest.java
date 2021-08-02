package ru.netology.diploma.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.data.Status;
import ru.netology.diploma.db.DbUtil;
import ru.netology.diploma.db.Order;
import ru.netology.diploma.db.Payment;
import ru.netology.diploma.page.CardPage;
import ru.netology.diploma.page.MainPage;
import ru.netology.diploma.test.util.TestUtil;


import java.sql.SQLException;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.diploma.test.util.TestUtil.*;


class TourTest {


    @BeforeEach
    public void login() {
        open("http://localhost:8080/");
    }

    @Test
    void successDebt() throws SQLException {
        CardPage cardPage = byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBDebt(MY_SQL_URL);
    }

    private CardPage byDebt() {
        MainPage mainPage = new MainPage();
        return mainPage.byDebt();
    }

    @Test
    void successCredit() throws SQLException {
        CardPage cardPage = byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBCredit(MY_SQL_URL);
    }

    @Test
    void invalidCardDebt() throws SQLException {
        CardPage cardPage = byDebt();
        invalidCard(cardPage);
    }

    @Test
    void invalidCardCredit() throws SQLException {
        CardPage cardPage = byCredit();
        invalidCard(cardPage);
    }

    private CardPage byCredit() {
        MainPage mainPage = new MainPage();
        return mainPage.byCredit();
    }

    private void invalidCard(CardPage cardPage) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutCardInfo());
        cardPage.checkCardInvalidFormatVisible();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkHolderFieldRequiredNotVisible();
        cardPage.checkCvcInvalidFormatNotVisible();
        DataGenerator.CardInfo cardInfo = DataGenerator.generateWithInvalidCardCardInfo();
        cardPage.fillCardDataAndCommit(cardInfo);
        cardPage.checkCardInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
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
    }

    @Test
    void invalidMonthDebt() throws SQLException {
        CardPage cardPage = byDebt();
        invalidMonth(cardPage);
    }

    @Test
    void invalidMonthCredit() throws SQLException {
        CardPage cardPage = byCredit();
        invalidMonth(cardPage);
    }

    private void invalidMonth(CardPage cardPage) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutMonth());
        cardPage.checkMonthInvalidFormatVisible();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkCardInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkHolderFieldRequiredNotVisible();
        cardPage.checkCvcInvalidFormatNotVisible();
        cardPage.fillMonthAndCommit("1");
        cardPage.checkMonthInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addMonthDigits(Character.toString(a));
            }
            assertEquals("1", cardPage.getMonthValue());
        }
        cardPage.clearAndFillMonthAndCommit("13");
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        String month = DataGenerator.generateValidCardInfo().getMonth();
        cardPage.clearAndFillMonthAndCommit(month);
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkSuccess();
    }

    @Test
    void invalidYearDebt() throws SQLException {
        CardPage cardPage = byDebt();
        invalidYear(cardPage);
    }

    @Test
    void invalidYearCredit() throws SQLException {
        CardPage cardPage = byCredit();
        invalidYear(cardPage);
    }


    private void invalidYear(CardPage cardPage) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutYear());
        cardPage.checkYearInvalidFormatVisible();
        cardPage.fillYearAndCommit("1");
        cardPage.checkYearInvalidFormatVisible();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkCardInvalidFormatNotVisible();
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkHolderFieldRequiredNotVisible();
        cardPage.checkCvcInvalidFormatNotVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addYearDigits(Character.toString(a));
            }
            assertEquals("1", cardPage.getMonthValue());
        }
        cardPage.clearAndFillYearAndCommit("13");
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkCardExpiredVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        cardPage.clearAndFillYearAndCommit("33");
        cardPage.checkYearInvalidTermVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        String month = DataGenerator.generateValidCardInfo().getYear();
        cardPage.clearAndFillYearAndCommit(month);
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkSuccess();
    }

    @Test
    void invalidHolderDebt() throws SQLException {
        CardPage cardPage = byDebt();
        invalidHolder(cardPage);
    }

    @Test
    void invalidHolderCredit() throws SQLException {
        CardPage cardPage = byCredit();
        invalidHolder(cardPage);
    }

    private void invalidHolder(CardPage cardPage) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutHolder());
        cardPage.checkHolderFieldRequiredVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        cardPage.addHolderChars("A");
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            boolean isLatinUpper = a > 'A' && a < 'Z';
            boolean isLatin = a > 'a' && a < 'z';
            if (!isLatin && !isLatinUpper && a != '-' && a != '\'' && a != 8 && a != 32) {
                cardPage.addHolderChars(Character.toString(a));
            }
            assertEquals("A", cardPage.getHolderValue());
        }
        cardPage.clearAndFillHolderAndCommit(DataGenerator.generateValidCardInfo().getCardHolderName());
        cardPage.checkHolderFieldRequiredNotVisible();
        cardPage.checkSuccess();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkCardInvalidFormatNotVisible();
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkCvcInvalidFormatNotVisible();
    }

    @Test
    void invalidCvcDebt() throws SQLException {
        CardPage cardPage = byDebt();
        invalidCvc(cardPage);
    }

    @Test
    void invalidCvcDCredit() throws SQLException {
        CardPage cardPage = byCredit();
        invalidCvc(cardPage);
    }

    private void invalidCvc(CardPage cardPage) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutCvc());
        cardPage.checkCvcInvalidFormatVisible();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkCardInvalidFormatNotVisible();
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkHolderFieldRequiredNotVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        cardPage.fillCvcAndCommit("1");
        cardPage.checkCvcInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addCvcDigits(Character.toString(a));
            }
            assertEquals("1", cardPage.getCvcValue());
        }
        cardPage.fillCvcAndCommit("2");
        cardPage.checkCvcInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(MY_SQL_URL);
        cardPage.fillCvcAndCommit("3");
        cardPage.checkCvcInvalidFormatNotVisible();
        cardPage.checkSuccess();
    }


    @Test
    void declinedCredit() throws SQLException {
        CardPage cardPage = byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateDeclinedCardInfo());
        cardPage.checkDecline();
        checkCreditRequest(Status.DECLINED, MY_SQL_URL);
        List<Order> orders = DbUtil.getOrders(MY_SQL_URL);
        assertTrue(orders.isEmpty());
        List<Payment> payments = DbUtil.getPayments(MY_SQL_URL);
        assertTrue(payments.isEmpty());
    }

    @Test
    void declinedDebt() throws SQLException {
        CardPage cardPage = byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateDeclinedCardInfo());
        cardPage.checkDecline();
        checkCreditRequestNotExists(MY_SQL_URL);
        Order order = checkOrder(false,MY_SQL_URL);
        checkPayment(order.getPaymentId(), "45000", Status.DECLINED, MY_SQL_URL);
    }

    @AfterEach
    public void clear() throws SQLException {
        DbUtil.clearDb(MY_SQL_URL);
    }
}
