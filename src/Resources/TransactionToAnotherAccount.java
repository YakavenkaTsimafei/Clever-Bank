package Resources;

import Exceptions.InsufficientFundsException;

import java.io.FileWriter;
import java.io.IOException;

public class TransactionToAnotherAccount extends BasicTransaction {
    private final Account toAccount;
    /*счет получатель  */

    public static String receiptTemplate =
            "--------------------------------------%n" +
                    "|           Банковский Чек           |%n" +
                    "| Чек:%31s|%n" +
                    "| %-10s %24tT|%n" +
                    "| Тип транзакции:%20s|%n" +
                    "| Банк отправителя:%18s|%n" +
                    "| Банк получателя:%19s|%n" +
                    "| Счет отправителя:%18s|%n" +
                    "| Счет получателя:%19s|%n" +
                    "| Сумма:%29.2f|%n" +
                    "--------------------------------------%n";

    public TransactionToAnotherAccount(Account changeableAccount, double amount, Account toAccount) {
        super(changeableAccount, amount);
        this.toAccount = toAccount;
        toAccount.getClient().addTransaction(this);
    }

    @Override
    public void execute() throws InsufficientFundsException {
        synchronized (Account.class) {
            if (changeableAccount.getBalance() >= amount || changeableAccount.getBalance() >= 0) {
                super.withdrawal(amount);
                /*снимаем со счета отправителя */
                toAccount.setBalance(toAccount.getBalance() + amount);
                /*добавляем к счету получателя */
                int checkNumber = random.nextInt(10000);
                String receipt = String.format(receiptTemplate, checkNumber, dTF.format(accrualTime), accrualTime, "Перевод", changeableAccount.getBank().getBankName(), toAccount.getBank().getBankName(), changeableAccount.getAccountNumber(), toAccount.getAccountNumber(), amount);
                /*подставляем к шаблону из родительского класса значения*/
                System.out.print(receipt);
                try (FileWriter fileWriter = new FileWriter(String.format("C:\\Users\\timmy\\IdeaProjects\\Clever-Bank\\src\\Check\\Check %d", checkNumber))) {
                    fileWriter.write(receipt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*сохраняем в файл*/
            } else {
                throw new InsufficientFundsException("Insufficient funds on the account");
            }
        }

    }
}
