package Resources;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Bank {
    private final ArrayList<Account> accounts;
    private final String bankName;
    /* счета находящиеся в банке
    * название банка
    *  */

    private static final BlockingQueue<Account> queueAccounts = new ArrayBlockingQueue<>(10);
    /* очередь в котору мы вносим счета которым нужно зачислить 1%*/

    public Bank(String bankName) {
        this.accounts = new ArrayList<>();
        this.bankName = bankName;
        threadProducer.start();
        threadConsumer.start();
        /*запускаем потоки которые проверяют и начисляют 1%
        * */
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public String getBankName() {
        return bankName;
    }


    private void producer() throws InterruptedException {

        while (true) {
            for (Account account : accounts) {
                /*проходим по всем счетам банка */
                LocalDateTime plusMonths = account.getDateNextInterestAccrual().plusMonths(1);
                /*к дате открытия счета добавляем месяц */
                if (LocalDateTime.now().isAfter(plusMonths)) {
                    /*если прошел месяц заходим в if*/
                    queueAccounts.put(account);
                    /*добавляем в очередь */
                    account.setDateNextInterestAccrual(plusMonths);
                    /*добавляем к переменной еще месяц, чтобы зайти в цикл через месяц */
                }
            }
            Thread.sleep(30000);

        }
    }

    private void consumer() throws InterruptedException {
        while (true) {
            Account account = queueAccounts.take();
            /*достаем значения которые добавили в методе producer*/
            double newBalance = account.getBalance() / 100 + account.getBalance();
            account.setBalance(newBalance);
            /*добавляем процент и изменяем переменную */
        }
    }

    Thread threadProducer = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                producer();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
    Thread threadConsumer = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
}
