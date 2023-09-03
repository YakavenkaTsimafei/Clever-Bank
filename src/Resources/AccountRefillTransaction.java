package Resources;

import java.io.FileWriter;
import java.io.IOException;

public class AccountRefillTransaction extends BasicTransaction {
    /* класс для пополнения счета */
    public AccountRefillTransaction(Account changeableAccount, double amount) {
        super(changeableAccount, amount);
    }

    @Override
    public void execute() {

        super.accountRefill(amount);
        /* начисляем деньги */
        int checkNumber = random.nextInt(10000);
        String receipt = String.format(receiptTemplate, checkNumber, dTF.format(accrualTime), accrualTime, "Пополнение", changeableAccount.getBank().getBankName(), changeableAccount.getAccountNumber(), amount);
        System.out.println(receipt);
        /*подставляем к шаблону из родительского класса значения*/
        try (FileWriter fileWriter = new FileWriter(String.format("C:\\Users\\timmy\\IdeaProjects\\Clever-Bank\\src\\Check\\Check %d", checkNumber))) {
            fileWriter.write(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*создаем и записываем в txt файл*/
    }
    /* метод выполняет пополнение счета  */
}

