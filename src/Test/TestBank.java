package Test;

import Resources.Account;
import Resources.Bank;
import Resources.Client;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TestBank {
    @Test
    public void testProducerConsumer() throws InterruptedException {
        Bank bank = new Bank("Alfa");
        Account account = new Account(500, "123456789", LocalDateTime.now().minusMonths(1), new Client("Корнеев", "Николай", "Александрович"), bank);
        bank.addAccount(account);
        Assertions.assertEquals(account.getAccountOpeningDate(),account.getDateNextInterestAccrual());
        Assertions.assertEquals(500, account.getBalance());
        Thread.sleep(30000);
        Assertions.assertEquals(account.getAccountOpeningDate().plusMonths(1),account.getDateNextInterestAccrual());
        Assertions.assertEquals(505, account.getBalance());
        Thread.sleep(30000);
        Assertions.assertNotEquals(510.05, account.getBalance());

    }

}
