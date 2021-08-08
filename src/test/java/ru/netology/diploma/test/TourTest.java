package ru.netology.diploma.test;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    private String currentUrl;



    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void successDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBDebt(url);
        DbUtil.clearDb(url);
    }

    private CardPage byDebt() {
        MainPage mainPage = new MainPage();
        return mainPage.byDebt();
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void successCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBCredit(url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidCardDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        invalidCard(cardPage, url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidCardCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        invalidCard(cardPage, url);
    }

    private CardPage byCredit() {
        MainPage mainPage = new MainPage();
        return mainPage.byCredit();
    }

    private void invalidCard(CardPage cardPage, String url) throws SQLException {
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
        TestUtil.checkDbIsEmpty(url);
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

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidMonthDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        invalidMonth(cardPage, url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidMonthCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        invalidMonth(cardPage, url);
    }

    private void invalidMonth(CardPage cardPage, String url) throws SQLException {
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
        TestUtil.checkDbIsEmpty(url);
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

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidYearDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        invalidYear(cardPage, url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidYearCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        invalidYear(cardPage, url);
    }


    private void invalidYear(CardPage cardPage, String url) throws SQLException {
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
        TestUtil.checkDbIsEmpty(url);
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addYearDigits(Character.toString(a));
            }
            assertEquals("1", cardPage.getMonthValue());
        }
        cardPage.clearAndFillYearAndCommit("13");
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkCardExpiredVisible();
        TestUtil.checkDbIsEmpty(url);
        cardPage.clearAndFillYearAndCommit("33");
        cardPage.checkYearInvalidTermVisible();
        TestUtil.checkDbIsEmpty(url);
        String month = DataGenerator.generateValidCardInfo().getYear();
        cardPage.clearAndFillYearAndCommit(month);
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkSuccess();

    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidHolderDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        invalidHolder(cardPage, url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidHolderCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        invalidHolder(cardPage, url);
    }

    private void invalidHolder(CardPage cardPage, String url) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutHolder());
        cardPage.checkHolderFieldRequiredVisible();
        TestUtil.checkDbIsEmpty(url);
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

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidCvcDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        invalidCvc(cardPage, url);
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void invalidCvcDCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        invalidCvc(cardPage, url);
    }

    private void invalidCvc(CardPage cardPage, String url) throws SQLException {
        cardPage.fillDataAndCommmit(DataGenerator.generateWithoutCvc());
        cardPage.checkCvcInvalidFormatVisible();
        cardPage.checkCardExpiredNotVisible();
        cardPage.checkCardInvalidFormatNotVisible();
        cardPage.checkMonthInvalidFormatNotVisible();
        cardPage.checkMonthInvalidTermNotVisible();
        cardPage.checkYearInvalidFormatNotVisible();
        cardPage.checkYearInvalidTermNotVisible();
        cardPage.checkHolderFieldRequiredNotVisible();
        TestUtil.checkDbIsEmpty(url);
        cardPage.fillCvcAndCommit("1");
        cardPage.checkCvcInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(url);
        for (char a = 0; a < Character.MAX_VALUE; a++) {
            if (!Character.isDigit(a) && a != 8) {
                cardPage.addCvcDigits(Character.toString(a));
            }
            assertEquals("1", cardPage.getCvcValue());
        }
        cardPage.fillCvcAndCommit("2");
        cardPage.checkCvcInvalidFormatVisible();
        TestUtil.checkDbIsEmpty(url);
        cardPage.fillCvcAndCommit("3");
        cardPage.checkCvcInvalidFormatNotVisible();
        cardPage.checkSuccess();
    }


    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void declinedCredit(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byCredit();
        cardPage.fillDataAndCommmit(DataGenerator.generateDeclinedCardInfo());
        cardPage.checkDecline();
        checkCreditRequest(Status.DECLINED, url);
        List<Order> orders = DbUtil.getOrders(url);
        assertTrue(orders.isEmpty());
        List<Payment> payments = DbUtil.getPayments(url);
        assertTrue(payments.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {MY_SQL_URL, POSTGRES_URL})
    void declinedDebt(String url) throws SQLException {
        openPage(url);
        CardPage cardPage = byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateDeclinedCardInfo());
        cardPage.checkDecline();
        checkCreditRequestNotExists(url);
        Order order = checkOrder(false, url);
        checkPayment(order.getPaymentId(), "45000", Status.DECLINED, url);
    }

    private void openPage(String url) throws SQLException {
        currentUrl = url;
        DbUtil.clearDb(url);
        if (MY_SQL_URL.equals(url)) {
            open( "http://localhost:8080/");
        } else {
            open( "http://localhost:8081/");
        }
    }

    @AfterEach
    void clearDb() throws SQLException {
        DbUtil.clearDb(currentUrl);
    }
}
