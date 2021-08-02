package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.diploma.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {

    public static final String ILLEGAL_FORMAT_TEXT = "Неверный формат";
    public static final String FIELD_IS_REQUIRED_TEXT = "Поле обязательно для заполнения";
    public static final String INVALID_TERM_TEXT = "Неверно указан срок действия карты";
    public static final String CAR_EXPIRED_TEXT = "Истёк срок действия карты";
    public static final String ERROR_TEXT = "Ошибка";
    public static final String SUCESS_TEXT = "Успешно";
    public static final String APPROVE_TEXT = "Операция одобрена Банком.";
    public static final String DECLINE_TEXT = "Ошибка! Банк отказал в проведении операции.";
    private final SelenideElement cardNumber = findInputByText("Номер карты");
    private final SelenideElement month = findInputByText("Месяц");
    private final SelenideElement year = findInputByText("Год");
    private final SelenideElement holder = findInputByText("Владелец");
    private final SelenideElement cvc = findInputByText("CVC/CVV");
    private final SelenideElement commit = findButtonByText("Продолжить");
    private final SelenideElement okNotificationTittle = $(".notification_status_ok .notification__title");
    private final SelenideElement okNotificationContent = $(".notification_status_ok .notification__content");
    private final SelenideElement errorNotificationTittle = $(".notification_status_error .notification__title");
    private final SelenideElement errorNotificationContent = $(".notification_status_error .notification__content");
    private final SelenideElement invalidCardFormat = findSpanByFieldText("Номер карты");
    private final SelenideElement invalidMonthFormat = findSpanByFieldText("Месяц");
    private final SelenideElement invalidYearFormat = findSpanByFieldText("Год");
    private final SelenideElement invalidHolderFormat = findSpanByFieldText("Владелец");
    private final SelenideElement invalidCvcFormat = findSpanByFieldText("CVC/CVV");


    private SelenideElement findInputByText(String spanText) {
        return $$(".input__top").find(Condition.text(spanText))
                .parent().$(".input__box .input__control");
    }

    private SelenideElement findSpanByFieldText(String spanText) {
        return $$(".input__top").find(Condition.text(spanText))
                .parent().$(".input__sub");
    }

    private SelenideElement findButtonByText(String spanText) {
        return $$(".button__text").find(Condition.text(spanText));
    }

    public void fillDataAndCommmit(DataGenerator.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolderName());
        cvc.setValue(cardInfo.getCvc());
        commit();
    }

    public void fillCardDataAndCommit(DataGenerator.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        commit();
    }

    public void fillMonthAndCommit(String monthValue) {
        month.setValue(monthValue);
        commit();
    }

    public void fillYearAndCommit(String yearValue) {
        month.setValue(yearValue);
        commit();
    }

    public void fillCvcAndCommit(String cvcValue) {
        cvc.setValue(cvcValue);
        commit();
    }


    public void clearAndFillMonthAndCommit(String monthValue) {
        month.doubleClick().sendKeys(Keys.BACK_SPACE);
        month.setValue(monthValue);
        commit();
    }

    public void clearAndFillHolderAndCommit(String monthValue) {
        holder.doubleClick().sendKeys(Keys.BACK_SPACE);
        holder.setValue(monthValue);
        commit();
    }

    public void clearAndFillYearAndCommit(String monthValue) {
        year.doubleClick().sendKeys(Keys.BACK_SPACE);
        year.setValue(monthValue);
        commit();
    }

    public void commit() {
        commit.click();
    }

    public String addCardDigits(String digits) {
        cardNumber.setValue(digits);
        return cardNumber.getValue();
    }

    public String addMonthDigits(String digits) {
        month.setValue(digits);
        return month.getValue();
    }

    public String addYearDigits(String digits) {
        year.setValue(digits);
        return year.getValue();
    }

    public String addCvcDigits(String digits) {
        cvc.setValue(digits);
        return cvc.getValue();
    }

    public String addHolderChars(String chars) {
        holder.setValue(chars);
        return holder.getValue();
    }

    public String getCardValue() {
        return cardNumber.getValue();
    }

    public String getMonthValue() {
        return month.getValue();
    }

    public String getHolderValue() {
        return holder.getValue();
    }

    public String getCvcValue() {
        return cvc.getValue();
    }


    public void checkSuccess() {
        okNotificationTittle.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text(SUCESS_TEXT));
        okNotificationContent.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text(APPROVE_TEXT));
    }

    public void checkDecline() {
        errorNotificationTittle.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text(ERROR_TEXT));
        errorNotificationContent.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text(DECLINE_TEXT));


    }

    public void checkCardInvalidFormatVisible() {
        invalidCardFormat.shouldBe(Condition.visible).shouldBe(Condition.text(ILLEGAL_FORMAT_TEXT));
    }


    public void checkCardInvalidFormatNotVisible() {
        invalidCardFormat.shouldBe(Condition.visible).shouldNotBe(Condition.text(ILLEGAL_FORMAT_TEXT));
    }

    public void checkMonthInvalidFormatVisible() {
        invalidMonthFormat.shouldBe(Condition.visible).shouldBe(Condition.text(ILLEGAL_FORMAT_TEXT));
    }

    public void checkYearInvalidFormatVisible() {
        invalidYearFormat.shouldBe(Condition.visible).shouldBe(Condition.text(ILLEGAL_FORMAT_TEXT));
    }

    public void checkYearInvalidFormatNotVisible() {
        if (invalidYearFormat.exists()) {
            invalidYearFormat.shouldNotBe(Condition.text(ILLEGAL_FORMAT_TEXT));
        }
    }

    public void checkCvcInvalidFormatVisible() {
        invalidCvcFormat.shouldBe(Condition.visible).shouldBe(Condition.text(ILLEGAL_FORMAT_TEXT));
    }

    public void checkCvcInvalidFormatNotVisible() {
        if (invalidCvcFormat.exists()) {
            invalidCvcFormat.shouldNotBe(Condition.text(ILLEGAL_FORMAT_TEXT));
        }
    }



    public void checkHolderFieldRequiredVisible() {
        invalidHolderFormat.shouldBe(Condition.visible).shouldBe(Condition.text(FIELD_IS_REQUIRED_TEXT));
    }

    public void checkHolderFieldRequiredNotVisible() {
        if (invalidHolderFormat.exists()) {
            invalidHolderFormat.shouldNotBe(Condition.text(FIELD_IS_REQUIRED_TEXT));
        }
    }

    public void checkMonthInvalidTermVisible() {
        invalidMonthFormat.shouldBe(Condition.visible).shouldBe(Condition.text(INVALID_TERM_TEXT));
    }

    public void checkMonthInvalidTermNotVisible() {
        if (invalidMonthFormat.exists()) {
            invalidMonthFormat.shouldNotBe(Condition.text(INVALID_TERM_TEXT));
        }
    }

    public void checkYearInvalidTermVisible() {
        invalidYearFormat.shouldBe(Condition.visible).shouldBe(Condition.text(INVALID_TERM_TEXT));
    }

    public void checkYearInvalidTermNotVisible() {
        if (invalidYearFormat.exists()) {
            invalidYearFormat.shouldNotBe(Condition.text(INVALID_TERM_TEXT));
        }
    }

    public void checkCardExpiredVisible() {
        invalidYearFormat.shouldBe(Condition.visible).shouldBe(Condition.text(CAR_EXPIRED_TEXT));
    }

    public void checkCardExpiredNotVisible() {
        if (invalidYearFormat.exists()) {
            invalidYearFormat.shouldNotBe(Condition.text(CAR_EXPIRED_TEXT));
        }
    }


    public void checkMonthInvalidFormatNotVisible() {
        if (invalidMonthFormat.exists()) {
            invalidMonthFormat.shouldNotBe(Condition.text(ILLEGAL_FORMAT_TEXT));
        }
    }


}
