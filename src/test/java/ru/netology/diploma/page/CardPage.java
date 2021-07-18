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





    private SelenideElement findInputByText(String spanText) {
        return $$(".input__top").find(Condition.text(spanText))
                .parent().$(".input__box .input__control");
    }

    private SelenideElement findButtonByText(String spanText) {
        return $$(".button__text").find(Condition.text(spanText));
    }

    public void fillData(DataGenerator.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        holder.setValue(cardInfo.getCardHolderName());
        cvc.setValue(cardInfo.getCvc());
        commit.click();
    }

    public void checkSuccess() {
        $(".notification__title").shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Успешно"));
        $(".notification__content").shouldBe(Condition.visible, Duration.ofSeconds(60))
                .shouldHave(Condition.text("Операция одобрена Банком."));
    }

}
