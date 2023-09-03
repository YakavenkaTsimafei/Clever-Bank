package Test;

import Exceptions.InsufficientFundsException;
import Resources.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class TestAccountRefillTransaction {
    @Test
    public void testExecute() throws InterruptedException {
        Bank bank = new Bank("Alfa");
        Account account = new Account(500, "123456789", LocalDateTime.now().minusMonths(1), new Client("Корнеев", "Николай", "Александрович"), bank);
        Thread threadProducer = new Thread(new Runnable() {
            @Override
            public void run() {
                AccountRefillTransaction transaction = new AccountRefillTransaction(account, 50);
                transaction.execute();

            }
        });
        Thread threadProducer1 = new Thread(new Runnable() {
            @Override
            public void run() {
                AccountRefillTransaction transaction = new AccountRefillTransaction(account, 50);
                transaction.execute();

            }
        });
        threadProducer.start();
        Thread.sleep(1000);
        threadProducer1.start();
        Thread.sleep(1000);
        Assertions.assertEquals(600, account.getBalance());
        Assertions.assertEquals(2,account.getClient().getClientTransactions().size());
    }
}
