package Resources;

import java.time.LocalDateTime;


public class Account {

    private double balance;
    private final String accountNumber;
    private final LocalDateTime accountOpeningDate;
    private final Client client;
    private final Bank bank;
    /*  баланс
     *  номер аккаунта
     *  дата создания
     *  владелец
     *  банк, где находится счет
     * */

    private LocalDateTime dateNextInterestAccrual;
    /*дата начисления процентов  */

    public Account(double balance, String accountNumber, LocalDateTime accountOpeningDate, Client client, Bank bank) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.accountOpeningDate = accountOpeningDate;
        this.client = client;
        this.bank = bank;
        this.dateNextInterestAccrual = accountOpeningDate;
        bank.addAccount(this);
        client.addAccounts(this);
        /* добавление счета в банк
         *  добавление счета к клиенту
         *  */
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getAccountOpeningDate() {
        return accountOpeningDate;
    }

    public Client getClient() {
        return client;
    }

    public Bank getBank() {
        return bank;
    }

    public void setDateNextInterestAccrual(LocalDateTime dateNextInterestAccrual) {
        this.dateNextInterestAccrual = dateNextInterestAccrual;
    }

    public LocalDateTime getDateNextInterestAccrual() {
        return dateNextInterestAccrual;
    }

}

