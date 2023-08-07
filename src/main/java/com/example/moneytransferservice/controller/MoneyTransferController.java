package com.example.moneytransferservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.moneytransferservice.model.ConfirmOperation;
import com.example.moneytransferservice.model.TransferMoney;
import com.example.moneytransferservice.model.TransferResult;
import com.example.moneytransferservice.service.CardService;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "${client_url}")
public class MoneyTransferController {
    private final CardService service;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResult> transfer(@RequestBody TransferMoney transferMoney) {
        ResponseEntity response = service.transfer(transferMoney);
        return response;
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<TransferResult> confirmOperation(@RequestBody ConfirmOperation confirmOperation) {
        ResponseEntity response = service.confirmOperation(confirmOperation);
        return response;
    }
}
