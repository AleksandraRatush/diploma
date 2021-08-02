package ru.netology.diploma.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.diploma.data.DataGenerator;
import ru.netology.diploma.db.DbUtil;
import ru.netology.diploma.page.CardPage;
import ru.netology.diploma.page.MainPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.diploma.test.util.TestUtil.*;


class TourPostgresTest {



    @BeforeEach
    public void login() {
        open("http://localhost:8081/");
    }

    @Test
    void successDebt() throws SQLException {
        MainPage mainPage = new MainPage();
        CardPage cardPage = mainPage.byDebt();
        cardPage.fillDataAndCommmit(DataGenerator.generateValidCardInfo());
        cardPage.checkSuccess();
        checkSuccessStateInDBDebt(POSTGRES_URL);
    }


    @AfterEach
    public void clear() throws SQLException {
        DbUtil.clearDb(POSTGRES_URL);
    }
}
