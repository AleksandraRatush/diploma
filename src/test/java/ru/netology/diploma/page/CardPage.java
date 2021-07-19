package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.diploma.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {

    private final SelenideElement cardNumber = findInputByText("Номер карты");
    private final SelenideElement month =  findInputByText("Месяц");
    private final SelenideElement year = findInputByText("Год");
    private final SelenideElement holder = findInputByText("Владелец");
    private final SelenideElement cvc = findInputByText("CVC/CVV");
    private final SelenideElement commit = findButtonByText("Продолжить");
    private final SelenideElement okNotificationTittle = $(".notification_status_ok .notification__title");
    private final SelenideElement okNotificationContent = $(".notification_status_ok .notification__content");
    private final SelenideElement errorNotificationTittle = $(".notification_status_error .notification__title");
    private final SelenideElement errorNotificationContent = $(".notification_status_error .notification__content");
    private final SelenideElement invalidCardFormat = findSpanByFieldText("Номер карты");





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

    public void commit() {
        commit.click();
    }

    public String addCardDigits (String digits) {
        cardNumber.setValue(digits);
        return cardNumber.getValue();
    }

    public String getCardValue () {
        return cardNumber.getValue();
    }


    public void checkSuccess() {
        okNotificationTittle.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Успешно"));
        okNotificationContent.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Операция одобрена Банком."));
    }

    public void checkDecline() {
        errorNotificationTittle.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Ошибка"));
        errorNotificationContent.shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Ошибка! Банк отказал в проведении операции."));
    }

    public void checkCardInvalidFormatVisible() {
      invalidCardFormat.shouldBe(Condition.visible).shouldBe(Condition.text("Неверный формат"));
    }

    public void checkCardInvalidFormatNotVisible() {
        invalidCardFormat.shouldNot(Condition.visible).shouldBe(Condition.text("Неверный формат"));
    }


}
