package ru.netology.diploma.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement debt = $$("button").findBy(Condition.text("Купить"));
    private final SelenideElement credit = $$("button").findBy(Condition.text("Купить в кредит"));


    public CardPage byDebt() {
        debt.click();
        return new CardPage();
    }

    public CardPage byCredit() {
        credit.click();
        return new CardPage();
    }

}
