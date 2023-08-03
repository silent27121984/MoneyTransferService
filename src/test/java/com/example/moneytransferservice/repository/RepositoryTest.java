package com.example.moneytransferservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.moneytransferservice.logger.Logger;
import com.example.moneytransferservice.model.Card;

@SpringBootTest
public class RepositoryTest {
    private CardRepository cardRepository;

    @BeforeEach
    void init() {
        cardRepository = new CardRepository(new Logger());
    }

    @Test
    void addCardTest() {
        int expected = 2;
        int res = cardRepository.getListCards().size();
        Assertions.assertEquals(expected, res);
    }

    @Test
    void getCard() {
        String cardNumber = "3333000033330000";
        Card card = cardRepository.getCard(cardNumber);
        Assertions.assertNotNull(card);
    }

    @Test
    void getCard2() {
        String cardNumber = "3333000033330000";
        Card card = cardRepository.getCard(cardNumber);
        int expected = 100000;
        int currency = card.amount().getValue();
        Assertions.assertEquals(expected, currency);
    }
}
