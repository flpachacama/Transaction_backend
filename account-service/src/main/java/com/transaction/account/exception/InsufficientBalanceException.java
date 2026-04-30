package com.transaction.account.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Saldo no disponible");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
