package edu.course.lab02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void createsAccountWithNonNegativeBalance() {
        assertEquals(0, new BankAccount(0).getBalance());
        assertEquals(100, new BankAccount(100).getBalance());
    }

    @Test
    void rejectsNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1));
    }

    @Test
    void depositsSuccessfully() {
        BankAccount account = new BankAccount(10);

        account.deposit(15);

        assertEquals(25, account.getBalance());
    }

    @Test
    void rejectsNonPositiveDeposit() {
        BankAccount account = new BankAccount(10);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-1));
        assertEquals(10, account.getBalance());
    }

    @Test
    void rejectsDepositThatWouldOverflowBalance() {
        BankAccount account = new BankAccount(Integer.MAX_VALUE);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(1));
        assertEquals(Integer.MAX_VALUE, account.getBalance());
    }

    @Test
    void withdrawsSuccessfully() {
        BankAccount account = new BankAccount(100);

        account.withdraw(35);

        assertEquals(65, account.getBalance());
    }

    @Test
    void rejectsNonPositiveWithdrawal() {
        BankAccount account = new BankAccount(10);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1));
        assertEquals(10, account.getBalance());
    }

    @Test
    void rejectsWithdrawalAboveBalanceWithoutChangingState() {
        BankAccount account = new BankAccount(20);

        assertThrows(IllegalArgumentException.class, () -> account.withdraw(21));

        assertEquals(20, account.getBalance());
    }
}
