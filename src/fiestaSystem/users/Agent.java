package fiestaSystem.users;

import fiestaSystem.model.Transaction;
import fiestaSystem.model.User;
import fiestaSystem.enums.TransactionStatus;
import java.util.ArrayList;

public class Agent extends User {

    private ArrayList<Transaction> transactions;

    public Agent(String id, String name) {
        super(id, name, "AGENT");
        this.transactions = new ArrayList<>();
    }

    @Override
    public void login() {
        System.out.println("Agent " + name + " logged in.");
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public ArrayList<Transaction> getPendingTransactions() {
        ArrayList<Transaction> pending = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getStatus() == TransactionStatus.PENDING) {
                pending.add(t);
            }
        }
        return pending;
    }

    public ArrayList<Transaction> getAllTransactions() {
        return transactions;
    }

    public void approveTransaction(String txId) {
        for (Transaction t : transactions) {
            if (t.getId().equals(txId) && t.getStatus() == TransactionStatus.PENDING) {
                t.complete();
                return;
            }
        }
        System.out.println("Transaction not found or already processed.");
    }

    public void rejectTransaction(String txId) {
        for (Transaction t : transactions) {
            if (t.getId().equals(txId) && t.getStatus() == TransactionStatus.PENDING) {
                t.reject();
                return;
            }
        }
        System.out.println("Transaction not found or already processed.");
    }
}