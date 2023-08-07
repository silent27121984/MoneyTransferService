package com.example.moneytransferservice.service;

import com.example.moneytransferservice.exception.IncorrectCodeException;
import com.example.moneytransferservice.exception.NotEnoughMoneyException;
import com.example.moneytransferservice.logger.Logger;
import com.example.moneytransferservice.model.*;
import com.example.moneytransferservice.repository.CardRepository;
import com.example.moneytransferservice.exception.IncorrectDataEntry;
import com.example.moneytransferservice.exception.ExceptionUnknownCard;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class CardService {
    private final CardRepository dataBaseCards;
    private final Logger logger;
    AtomicLong operationId = new AtomicLong();

    List<PendingOperation> listPendingOperation = new ArrayList<>();

    public CardService(CardRepository dataBaseCards, Logger logger) {
        this.dataBaseCards = dataBaseCards;
        this.logger = logger;
    }

    public ResponseEntity<TransferResult> transfer(TransferMoney transferMoney) {
        final String numberWriteOffCard= transferMoney.numberWriteOffCard();
        final String numberCardForEnrolment = transferMoney.numberCardForEnrolment();
        final String numberCardCVV = transferMoney.numberCardCVV();
        final String numberCardValidTill = transferMoney.numberCardValidTill();
        Card fromCard = dataBaseCards.getCard(numberWriteOffCard);
        Card toCard = dataBaseCards.getCard(numberCardForEnrolment);

        if (fromCard == null || toCard == null) {
            throw new ExceptionUnknownCard("Неизвестный номер карты " + numberWriteOffCard);
        }
        if (!fromCard.cardValidTill().equals(numberCardValidTill) ||
                !fromCard.cardCVV().equals(numberCardCVV)) {
            logger.getLog("Ошибка ввода данных карты");
            throw new IncorrectDataEntry("Ошибка ввода данных карты");
        }
        if (fromCard.amount().value() < transferMoney.amount().value()) {
            logger.getLog("На карте недостаточно средств");
            throw new NotEnoughMoneyException("На карте недостаточно средств");
        }

        String id = String.valueOf(operationId.getAndIncrement());
        String code = "0000";
        PendingOperation operation = new PendingOperation(id, code, transferMoney);
        listPendingOperation.add(operation);

        TransferResult result = new TransferResult(operationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<TransferResult> confirmOperation(ConfirmOperation confirmOperation) {
        PendingOperation pendingOperation = listPendingOperation.stream()
                .filter(x -> Objects.equals(x.id(), confirmOperation.operationId()))
                .findFirst()
                .orElse(null);
        if (pendingOperation != null && pendingOperation.code().equals(confirmOperation.code())) {
            TransferMoney transferMoney = pendingOperation.transferMoney();
            logger.getLog("Операция: \"" + confirmOperation.operationId() + "\" выполнена \n" +
                    "Карта списания: " + transferMoney.numberWriteOffCard() + "\n" +
                    "Карта зачисления: " + transferMoney.numberCardForEnrolment() + "\n" +
                    "Сумма перевода: " + transferMoney.amount().value() + "\n" +
                    "Валюта перевода: " + transferMoney.amount().currency());

            String transferFromCard = transferMoney.numberWriteOffCard();
            String transferToCard = transferMoney.numberCardForEnrolment();
            Amount amount = transferMoney.amount();
            Card fromCard = dataBaseCards.getCard(transferFromCard);
            Card toCard = dataBaseCards.getCard(transferToCard);
            updateBalance(transferFromCard, transferToCard, amount);
            logger.getLog("Баланс карты списания " + transferFromCard + " : " + fromCard.amount().value());
            logger.getLog("Баланс карты зачисления" + transferToCard + " : " + toCard.amount().value());
            TransferResult result = new TransferResult(operationId);
            confirmOperation.operationId();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            logger.getLog("Введен не верный код");
            throw new IncorrectCodeException("Введен не верный код");
        }
    }

    public void updateBalance(String transferFromCard, String transferToCard, Amount amount) {
        Card fromCard = dataBaseCards.getCard(transferFromCard);
        Card toCard = dataBaseCards.getCard(transferToCard);
        fromCard.amount().setValue(fromCard.amount().value() - amount.value());
        toCard.amount().setValue(toCard.amount().value() + amount.value());
    }

}
