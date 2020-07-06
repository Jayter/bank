package cz.newbank.boarding;

public class Account implements IAccount {

    public Account(Long balance, String name) {
        this.balance = balance;
        this.name = name;
    }

    private Long balance;
    private String name;


    @Override
    public Long getBalance() {
        return balance;
    }

    @Override
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(IAccount o) {
        return name.compareTo(o.getName());
    }
}
