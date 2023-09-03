package Test;

import Exceptions.InsufficientFundsException;
import Resources.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;

public class TestWithdrawalTransaction {
    @Test
    public void testExecute() throws InterruptedException {
        Bank bank = new Bank("Alfa");
        Account account = new Account(500, "123456789", LocalDateTime.now().minusMonths(1), new Client("Корнеев", "Николай", "Александрович"), bank);
        Thread threadProducer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    BasicTransaction transaction = new WithdrawalTransaction(account, 50);
                    transaction.execute();

                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread threadProducer1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    BasicTransaction transaction = new WithdrawalTransaction(account, 50);
                    transaction.execute();

                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }
            }
        });
        threadProducer.start();
        threadProducer1.start();
        Thread.sleep(1000);
        Assertions.assertEquals(400, account.getBalance());
        Assertions.assertEquals(2,account.getClient().getClientTransactions().size());
        Throwable exception = assertThrows(
                InsufficientFundsException.class, () -> {
                    WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(account, 500);
                    withdrawalTransaction.execute();
                }
        );

    }


}
