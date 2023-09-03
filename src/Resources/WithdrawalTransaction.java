package Resources;

import Exceptions.InsufficientFundsException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WithdrawalTransaction extends BasicTransaction {


    public WithdrawalTransaction(Account changeableAccount, double amount) {
        super(changeableAccount, amount);
    }

    @Override
    public void execute() throws InsufficientFundsException {
        super.withdrawal(amount);
        /*снимаем деньги */
        int checkNumber = random.nextInt(10000);
        String receipt = String.format(receiptTemplate, checkNumber, dTF.format(accrualTime), accrualTime, "Снятие", changeableAccount.getBank().getBankName(), changeableAccount.getAccountNumber(), amount);
        /*подставляем к шаблону из родительского класса значения*/
        System.out.print(receipt);
        try (FileWriter fileWriter = new FileWriter(String.format("C:\\Users\\timmy\\IdeaProjects\\Clever-Bank\\src\\Check\\Check %d", checkNumber))) {
            fileWriter.write(receipt);
        } catch(IOException e) {
            e.printStackTrace();
        }
        /*сохраняем в файл */
    }
}
