package com.example.moneytransferservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.moneytransferservice.model.ConfirmOperation;
import com.example.moneytransferservice.model.TransferMoney;
import com.example.moneytransferservice.model.TransferResult;
import com.example.moneytransferservice.service.CardService;
@RestController
@CrossOrigin
public class MoneyTransferController {
    private final CardService service;

    public MoneyTransferController(CardService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResult> transfer(@RequestBody TransferMoney transferMoney) {
        return service.transfer(transferMoney);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferResult> confirmOperation(@RequestBody ConfirmOperation confirmOperation) {
        return service.confirmOperation(confirmOperation);
    }
}
