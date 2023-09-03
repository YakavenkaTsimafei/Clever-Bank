package Test;

import Exceptions.InsufficientFundsException;
import Resources.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;

public class TestClient {
    @Test
    public void testGeneratingStatementsOfClientTransactions() throws InsufficientFundsException, InterruptedException {
        Bank bank = new Bank("Alfa");
        Bank bank1 = new Bank("Clever-Bank");
        Client client = new Client("Сахаров", "Андрей", "Андреевич");
        Account account0 = new Account(500, "123456789", LocalDateTime.now(), client, bank);
        Account account1 = new Account(450, "574476849", LocalDateTime.now(), new Client("Корнеев", "Николай", "Александрович"), bank);
        Account account2 = new Account(630, "464675687", LocalDateTime.now(), client, bank1);
        Account account3 = new Account(700, "574675888", LocalDateTime.now(), client, bank);

        TransactionToAnotherAccount transactionToAnotherAccount = new TransactionToAnotherAccount(account0, 50, account1);
        transactionToAnotherAccount.execute();
        AccountRefillTransaction withdrawalTransaction0 = new AccountRefillTransaction(account2, 50);
        withdrawalTransaction0.execute();
        Thread.sleep(60000);
        WithdrawalTransaction withdrawalTransaction1 = new WithdrawalTransaction(account0, 50);
        withdrawalTransaction1.execute();
        WithdrawalTransaction withdrawalTransaction2 = new WithdrawalTransaction(account3, 50);
        withdrawalTransaction2.execute();
        AccountRefillTransaction accountRefillTransaction = new AccountRefillTransaction(account1, 50);
        accountRefillTransaction.execute();
        Assertions.assertEquals(1, account0.getClient().generatingStatementsOfClientTransactions(LocalDateTime.now().minusMinutes(3), LocalDateTime.now().minusSeconds(30), bank, "TXT").size());
        /* в endDate записываем настоящее время - 30 секунд, поэтому withdrawalTransaction2 не попадает */
        Assertions.assertEquals(1, account0.getClient().generatingStatementsOfClientTransactions(LocalDateTime.now().minusMinutes(3), LocalDateTime.now(), bank1, "TXT").size());
        /*т.к мы привязаны к банку, то считается только одна транзакция произведенная bank1 */


    }
}
