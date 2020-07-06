package cz.newbank.boarding;

import cz.newbank.boarding.exception.AccountNotFoundException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class NewBank implements IBank {

    private Set<Account> accounts = new TreeSet<>();
    private Set<Transaction> transactions = new HashSet<>();

    @Override
    public void setAccounts(Map<String, Long> accounts) {
        this.accounts = accounts.entrySet()
                                .stream()
                                .map(e -> new Account(e.getValue(), e.getKey()))
                                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public void setTransactions(Set<Object[]> transactions) {
        this.transactions = transactions
                .stream()
                .map(o -> new Transaction((String) o[0], (String) o[1], (Long) o[2], Instant.now().toEpochMilli()))
                .filter(transaction -> findAccount(transaction.getTargetAccount()).isPresent() && findAccount(transaction.getSourceAccount())
                        .isPresent())
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public synchronized void processTransaction(String source, String target, Long amount) throws AccountNotFoundException {
        IAccount sourceAccount = getAccount(source);
        IAccount targetAccount = getAccount(target);

        //todo: add synchronization
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);
    }

    @Override
    public IAccount getAccount(String name) throws AccountNotFoundException {
        return findAccount(name).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public Long getSumAmounts() {
        return transactions.stream()
                           .map(Transaction::getAmount)
                           .reduce(0L, Long::sum);
    }

    @Override
    public Long getBalance(String account) throws AccountNotFoundException {
        return getAccount(account).getBalance();
    }

    private Optional<Account> findAccount(String name) {
        return accounts.stream()
                       .filter(account -> account.getName().equals(name))
                       .findAny();
    }
}
