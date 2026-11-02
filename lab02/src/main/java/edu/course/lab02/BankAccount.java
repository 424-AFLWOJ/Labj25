package edu.course.lab02;

/**
 * Учебный банковский счёт для подготовительной части лабораторной № 2.
 */
public final class BankAccount {
    private int balance;

    public BankAccount(int initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance must not be negative");
        }
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (balance > Integer.MAX_VALUE - amount) {
            throw new IllegalArgumentException("balance overflow");
        }
        balance += amount;
    }

    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("insufficient funds");
        }
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
}
