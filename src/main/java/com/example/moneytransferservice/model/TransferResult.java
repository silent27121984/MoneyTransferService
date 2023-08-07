package com.example.moneytransferservice.model;

import java.util.concurrent.atomic.AtomicLong;

public record TransferResult(AtomicLong operationId) {
}
