package com.example.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import com.example.moneytransferservice.logger.Logger;
import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.Card;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepository {
    private final String FOLDERPATH = "src/main/resources/application.properties";
    private final Map<String, Card> listCards;
    private final Logger logger;

    public CardRepository(Logger logger) {
        this.logger = logger;
        listCards = new ConcurrentHashMap<>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(FOLDERPATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addCard(properties);
    }

    private void addCard(Properties properties) {
        var prop = properties.getProperty("NUMBER_USERS");
        var c = Integer.parseInt(prop);

        for (int i = 1; i <= c; i++) {
            String cardNumber = properties.getProperty("CARD_NUMBER_" + i);
            var amountStr = properties.getProperty("CARD_AMOUNT_" + i);
            var amountValue = Integer.parseInt(amountStr);
            var amountCurrency = properties.getProperty("CURRENCY_" + i);
            var amount = new Amount(amountValue, amountCurrency);
            var card = new Card(cardNumber,
                    properties.getProperty("CARD_VALID_TILL_" + i),
                    properties.getProperty("CARD_CVV_" + i),
                    amount);
            listCards.put(cardNumber, card);
        }
    }


    public Map<String, Card> getListCards() {
        return listCards;
    }


    public Card getCard(String cardNumber) {
        if (!listCards.containsKey(cardNumber)) {
            System.out.println(cardNumber + " такой карты нет в базе.");
            logger.getLog(cardNumber + " введен не верный номер карты");
            return null;
        }
        return listCards.get(cardNumber);
    }
}
