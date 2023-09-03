package Test;

import Exceptions.InsufficientFundsException;
import Resources.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertThrows;

public class TestTransactionToAnotherAccount {
    @Test
    public void testExecute() throws InterruptedException {
        Bank bank = new Bank("Alfa");
        Bank bank1 = new Bank("Clever-Bank");
        Account account0 = new Account(500, "123456789", LocalDateTime.now().minusMonths(1), new Client("Сахаров", "Андрей", "Андреевич"), bank);
        Account account1 = new Account(450, "574476849", LocalDateTime.now(), new Client("Корнеев", "Николай", "Александрович"), bank);
        Account account2 = new Account(670, "573567688", LocalDateTime.now(), new Client("Бурянов", "Андрей", "Сергеевич"), bank1);
        Thread threadProducer = new Thread(new Runnable() {
            @Override
            public void run() {
                TransactionToAnotherAccount transaction = new TransactionToAnotherAccount(account0, 300, account1);
                try {
                    transaction.execute();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }

            }
        });
        Thread threadProducer1 = new Thread(new Runnable() {
            @Override
            public void run() {
                TransactionToAnotherAccount transaction = new TransactionToAnotherAccount(account1, 100, account0);
                try {
                    transaction.execute();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }

            }
        });
        Thread threadProducer2 = new Thread(new Runnable() {
            @Override
            public void run() {
                TransactionToAnotherAccount transaction = new TransactionToAnotherAccount(account1, 100, account2);
                try {
                    transaction.execute();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }

            }
        });


        threadProducer.start();
        threadProducer1.start();
        threadProducer2.start();
        Thread.sleep(2000);
        Assertions.assertEquals(300, account0.getBalance());
        Assertions.assertEquals(550, account1.getBalance());
        Assertions.assertEquals(770,account2.getBalance());
        Assertions.assertEquals(2,account0.getClient().getClientTransactions().size());
        Assertions.assertEquals(3,account1.getClient().getClientTransactions().size());
        Assertions.assertEquals(1,account2.getClient().getClientTransactions().size());
        Throwable exception = assertThrows(
                InsufficientFundsException.class, () -> {
                    TransactionToAnotherAccount withdrawalTransaction = new TransactionToAnotherAccount(account0, 600, account1);
                    withdrawalTransaction.execute();
                }
        );
    }

}
