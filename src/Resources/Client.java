package Resources;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    private final String lastName;
    private final String firstName;
    private final String patronymic;
    private final ArrayList<Account> accounts;
    private final ArrayList<BasicTransaction> ClientTransactions;
    /* фамилия
     *  имя
     *  отчество
     *  счета клиента
     *  транзакции клиента
     * */

    private final String statementTemplate =
            "                             Выписка %n" +
                    "                            | %s    %n" +
                    "Клиент                      | %s %s %s %n" +
                    "Счет                        | %s %n" +
                    "Валюта                      | BYN %n" +
                    "Дата открытия               | %s  %n" +
                    "Период                      | %s - %s  %n" +
                    "Дата и время формирования   | %s, %tH.%tM  %n" +
                    "Остаток                     | %.2f BYN      %n " +
                    "   Дата    |        Примечание                       | Сумма %n" +
                    "------------------------------------------------------------- %n";

    public Client(String lastName, String firstName, String patronymic) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.accounts = new ArrayList<>();
        this.ClientTransactions = new ArrayList<>();
    }

    public String getLastName() {
        return lastName;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getPatronymic() {
        return patronymic;
    }


    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccounts(Account account) {

        accounts.add(account);
    }

    public ArrayList<BasicTransaction> getClientTransactions() {
        return ClientTransactions;
    }

    public void addTransaction(BasicTransaction transaction) {
        synchronized (accounts) {
            ClientTransactions.add(transaction);
        }
    }

    public ArrayList<BasicTransaction> generatingStatementsOfClientTransactions(LocalDateTime startDate, LocalDateTime endDate, Bank executingBank, String saveAs) {
        /*указываем за какое время нам нужна выписка, из какого банка и в каком формате */
        Random random = new Random();
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder translationsList = new StringBuilder();
        String statement = null;
        ArrayList<BasicTransaction> transactions = new ArrayList<>();
        for (BasicTransaction transaction : ClientTransactions) {
            if (transaction.getAccrualTime().isAfter(startDate) && transaction.getAccrualTime().isBefore(endDate) && executingBank.equals(transaction.getChangeableAccount().getBank())) {
                /*проходим по транзакциям клиента если совпадает время и банк исполнитель то формируем шаблон */
                transactions.add(transaction);
                statement = String.format(statementTemplate, transaction.getChangeableAccount().getBank().getBankName(), lastName, firstName, patronymic, transaction.getChangeableAccount().getAccountNumber(), dTF.format(transaction.getChangeableAccount().getAccountOpeningDate()), dTF.format(startDate), dTF.format(endDate), dTF.format(LocalDateTime.now()), LocalDateTime.now(), LocalDateTime.now(), transaction.getChangeableAccount().getBalance());

                if (transaction instanceof TransactionToAnotherAccount) {
                    translationsList.append(String.format("%s  |  Пополнение с другого счета             | %.2f BYN %n", dTF.format(transaction.accrualTime), transaction.amount));
                }
                if (transaction instanceof WithdrawalTransaction) {
                    translationsList.append(String.format("%s  |  Снятие средств                         | %.2f BYN %n", dTF.format(transaction.accrualTime), transaction.amount));
                }
                if (transaction instanceof AccountRefillTransaction) {
                    translationsList.append(String.format("%s  |  Пополнение                             | %.2f BYN %n", dTF.format(transaction.accrualTime), transaction.amount));
                }
                /*в зависимости от типа транзакции добавляем примечание */
            }

        }
        String forSave = statement + translationsList;
        System.out.println(forSave);
        if (saveAs.equals("PDF")) {
            try (Document document = new Document(new PdfDocument(new PdfWriter(String.format("C:\\Users\\timmy\\IdeaProjects\\Clever-Bank\\src\\Statement\\ PDF statement %d ", random.nextInt(10000)))))) {
                document.add(new Paragraph(statement + translationsList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*сохраняем в PDF
        с gradle были проблемы,пришлось скачивать и добавлять библиотеку
          https://sourceforge.net/projects/itext/files/latest/download*/

        if (saveAs.equals("TXT")) {
            try (FileWriter fileWriter = new FileWriter(String.format("C:\\Users\\timmy\\IdeaProjects\\Clever-Bank\\src\\Statement\\ TXT statement %d", random.nextInt(10000)))) {
                fileWriter.write(forSave);
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        /*сохраняем в TXT  */
        return transactions;
    }

}
