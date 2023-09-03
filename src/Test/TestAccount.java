package Test;

import Resources.Account;
import Resources.Bank;
import Resources.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestAccount {
    @Test
    public void testSetMoney() {
        Bank bank = new Bank("Alfa");
        Account account0 = new Account(500, "123456789", LocalDateTime.now().minusMonths(1), new Client("Корнеев", "Николай", "Александрович"), bank);
        Account account1 = new Account(450, "574476849", LocalDateTime.now(), new Client("Корнеев", "Николай", "Александрович"), bank);
        Assertions.assertEquals(2,bank.getAccounts().size());
        Assertions.assertEquals(1,account0.getClient().getAccounts().size());
        Assertions.assertEquals(1,account1.getClient().getAccounts().size());


    }
}
