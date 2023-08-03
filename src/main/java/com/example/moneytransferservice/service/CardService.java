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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class CardService {
    private final CardRepository dataBaseCards;
    private final Logger logger;
    AtomicLong operationId = new AtomicLong(0);

    List<PendingOperation> listPendingOperation = new ArrayList<>();

    public CardService(CardRepository dataBaseCards, Logger logger) {
        this.dataBaseCards = dataBaseCards;
        this.logger = logger;
    }

    public ResponseEntity<TransferResult> transfer(TransferMoney transferMoney) {
        String transferFromCard = transferMoney.getNumberWriteOffCard();
        String transferToCard = transferMoney.getNumberCardForEnrolment();
        Card fromCard = dataBaseCards.getCard(transferFromCard);
        Card toCard = dataBaseCards.getCard(transferToCard);

        if (fromCard == null || toCard == null) {
            throw new ExceptionUnknownCard("Неизвестный номер карты " + transferFromCard);
        }
        if (!fromCard.cardValidTill().equals(transferMoney.numberCardValidTill) ||
                !fromCard.cardCVV().equals(transferMoney.numberCardCVV)) {
            logger.getLog("Ошибка ввода данных карты");
            throw new IncorrectDataEntry("Ошибка ввода данных карты");
        }
        if (fromCard.amount().getValue() < transferMoney.getAmount().getValue()) {
            logger.getLog("На карте недостаточно средств");
            throw new NotEnoughMoneyException("На карте недостаточно средств");
        }

        String id = String.valueOf(operationId.getAndIncrement());
        String code = "0000";
        PendingOperation operation = new PendingOperation(id, code, transferMoney);
        listPendingOperation.add(operation);

        TransferResult result = new TransferResult();
        result.operationId = id;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<TransferResult> confirmOperation(ConfirmOperation confirmOperation) {
        PendingOperation pendingOperation = listPendingOperation.stream()
                .filter(x -> Objects.equals(x.id, confirmOperation.getOperationId()))
                .findFirst()
                .orElse(null);
        if (pendingOperation != null && pendingOperation.getCode().equals(confirmOperation.getCode())) {
            TransferMoney transferMoney = pendingOperation.getTransferMoney();
            logger.getLog("Операция: \"" + confirmOperation.getOperationId() + "\" выполнена \n" +
                    "Карта списания: " + transferMoney.getNumberWriteOffCard() + "\n" +
                    "Карта зачисления: " + transferMoney.getNumberCardForEnrolment() + "\n" +
                    "Сумма перевода: " + transferMoney.getAmount().getValue() + "\n" +
                    "Валюта перевода: " + transferMoney.getAmount().getCurrency());

            String transferFromCard = transferMoney.numberWriteOffCard;
            String transferToCard = transferMoney.numberCardForEnrolment;
            Amount amount = transferMoney.getAmount();
            Card fromCard = dataBaseCards.getCard(transferFromCard);
            Card toCard = dataBaseCards.getCard(transferToCard);
            updateBalance(transferFromCard, transferToCard, amount);
            logger.getLog("Баланс карты списания " + transferFromCard + " : " + fromCard.amount().getValue());
            logger.getLog("Баланс карты зачисления" + transferToCard + " : " + toCard.amount().getValue());
            TransferResult result = new TransferResult();
            result.operationId = confirmOperation.getOperationId();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            logger.getLog("Введен не верный код");
            throw new IncorrectCodeException("Введен не верный код");
        }
    }

    public void updateBalance(String transferFromCard, String transferToCard, Amount amount) {
        Card fromCard = dataBaseCards.getCard(transferFromCard);
        Card toCard = dataBaseCards.getCard(transferToCard);
        fromCard.amount().setValue(fromCard.amount().getValue() - amount.getValue());
        toCard.amount().setValue(toCard.amount().getValue() + amount.getValue());
    }

}
