package com.example.moneytransferservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.moneytransferservice.exception.IncorrectDataEntry;
import com.example.moneytransferservice.exception.NotEnoughMoneyException;
import com.example.moneytransferservice.exception.ExceptionUnknownCard;
import com.example.moneytransferservice.logger.Logger;
import com.example.moneytransferservice.model.Amount;
import com.example.moneytransferservice.model.ConfirmOperation;
import com.example.moneytransferservice.model.TransferMoney;
import com.example.moneytransferservice.model.TransferResult;
import com.example.moneytransferservice.repository.CardRepository;

import java.util.Objects;

@SpringBootTest
public class ServiceTest {
    private CardService cardService;

    @BeforeEach
    void init() {
        Logger logger = new Logger();
        CardRepository cardRepository = new CardRepository(logger);
        cardService = new CardService(cardRepository,logger);
    }


    @Test
    void transferTest_exceptionUnknownCard() {
        TransferMoney transferMoney = new TransferMoney(
                "null",
                "03.24",
                "323",
                "2222000022220000",
                new Amount(100000, "RU"));
        Assertions.assertThrowsExactly(ExceptionUnknownCard.class, () -> cardService.transfer(transferMoney));


    }

    @Test
    void transferTest_IncorrectDataEntry() {
        TransferMoney transferMoney = new TransferMoney(
                "3333000033330000",
                "11.24",
                "323",
                "2222000022220000",
                new Amount(100000, "RU"));
        Assertions.assertThrowsExactly(IncorrectDataEntry.class, () -> cardService.transfer(transferMoney));
    }

    @Test
    void transferTest_NotEnoughMoneyException() {
        TransferMoney transferMoney = new TransferMoney(
                "3333000033330000",
                "03/24",
                "323",
                "2222000022220000",
                new Amount(120000, "RU"));
        Assertions.assertThrowsExactly(NotEnoughMoneyException.class, () -> cardService.transfer(transferMoney));
    }

    @Test
    void transferTest() {
        TransferMoney transferMoney = new TransferMoney(
                "3333000033330000",
                "03/24",
                "323",
                "2222000022220000",
                new Amount(10000, "RU"));
        ResponseEntity<TransferResult> result = cardService.transfer(transferMoney);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("0", result.getBody().operationId);
    }

    @Test
    void confirmOperation() {
        TransferMoney transferMoney = new TransferMoney(
                "3333000033330000",
                "03/24",
                "323",
                "2222000022220000",
                new Amount(10000, "RU"));
        ResponseEntity<TransferResult> result = cardService.transfer(transferMoney);
        String id = Objects.requireNonNull(result.getBody()).operationId;
        String code = "0000";

        ConfirmOperation confirmOperation = new ConfirmOperation(id, code);
        var response = cardService.confirmOperation(confirmOperation);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
