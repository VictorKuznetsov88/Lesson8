package transfer.service;

import transfer.model.Account;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MoneyTransferService {

    public void transfer(Account from, Account to, long amount) throws InterruptedException {

        Lock lock = new ReentrantLock();
        lock.lockInterruptibly();
        try {
            if (from.getBalance() >= amount) {
                from.setBalance(from.getBalance() - amount);
                to.setBalance(to.getBalance() + amount);
            } else {
                throw new IllegalArgumentException("Недостаточно денежных средств!");
            } }finally{
                lock.unlock();
            }
        }
    }

