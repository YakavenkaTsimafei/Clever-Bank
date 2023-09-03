package Resources;

import Exceptions.InsufficientFundsException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public abstract class BasicTransaction implements ExecuteTransaction {
    protected final Account changeableAccount;
    protected final double amount;
    protected final LocalDateTime accrualTime;
    protected final Bank executingBank;
    /* изменяемый счет
     * сумма
     * время создания транзакции
     * исполняющий банк
     * */

    public String receiptTemplate =
            "--------------------------------------%n" +
                    "|           Банковский Чек           |%n" +
                    "| Чек:%31s|%n" +
                    "| %-10s %24tT|%n" +
                    "| Тип транзакции:%20s|%n" +
                    "| Банк:%30s|%n" +
                    "| Счет:%30s|%n" +
                    "| Сумма:%25.2f BYN|%n" +
                    "--------------------------------------%n";

    public Random random = new Random();
    DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public BasicTransaction(Account changeableAccount, double amount) {
        this.changeableAccount = changeableAccount;
        this.amount = amount;
        this.accrualTime = LocalDateTime.now();
        this.executingBank = changeableAccount.getBank();
        changeableAccount.getClient().addTransaction(this);
        /*добавляем транзакцию к клиенту */
    }

    public Account getChangeableAccount() {
        return changeableAccount;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getAccrualTime() {
        return accrualTime;
    }

    public Bank getExecutingBank() {
        return executingBank;
    }

    public void accountRefill(double amount) {
        synchronized (Account.class) {
            changeableAccount.setBalance(amount + changeableAccount.getBalance());
        }
    }
    /* пополнение счета */

    public void withdrawal(double amount) throws InsufficientFundsException {
        synchronized (Account.class) {
            if (changeableAccount.getBalance() <= 0 || changeableAccount.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient funds on the account");
            }
            changeableAccount.setBalance(changeableAccount.getBalance() - amount);
        }
        /*списание со счета */
    }


}
