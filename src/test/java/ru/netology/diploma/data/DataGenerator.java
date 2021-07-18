package ru.netology.diploma.data;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.Value;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class DataGenerator {


    private static final String SPACE = " ";
    private static final Faker FAKER = new Faker();
    private static final String APPROVED_CARD = "4444 4444 4444 4441";
    public static final String ZERO = "0";
    public static final int MIN_CVC = 100;
    public static final int MAX_CVC = 999;

    public static String generateCardHolderName() {
        Name name = FAKER.name();
        return name.lastName() + SPACE + name.firstName();
    }

    public static Calendar generateFutureDate(int atMost, TimeUnit timeUnit) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(FAKER.date().future(atMost, timeUnit));
        return calendar;
    }

    public static Calendar generatePastDate(int atMost, TimeUnit timeUnit) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(FAKER.date().past(atMost, timeUnit));
        return calendar;
    }

    public static String generateCVC(int min, int max){
        return String.valueOf(FAKER.number().numberBetween(min, max));
    }

    public static CardInfo generateValidCardInfo(){
        Calendar date = generateFutureDate(300, TimeUnit.DAYS);
        return new CardInfo (APPROVED_CARD, getMonth(date),
                getYear(date), generateCardHolderName(),
                generateCVC(MIN_CVC, MAX_CVC));
    }

    private static String getYear(Calendar date) {
        return String.valueOf(date.get(Calendar.YEAR)).substring(2,4);
    }

    private static String getMonth(Calendar date) {
        return addZero(String.valueOf(date.get(Calendar.MONTH)));
    }

    private static String addZero(String result) {
        if (result.length() == 1) {
            return ZERO + result;
        }
        return result;
    }


    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String cardHolderName;
        String cvc;
    }
}
