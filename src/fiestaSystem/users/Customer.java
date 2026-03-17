package fiestaSystem.users;

import fiestaSystem.model.Transaction;
import fiestaSystem.payment.PaymentStrategy;
import fiestaSystem.payment.CashPayment;
import fiestaSystem.model.User;
import fiestaSystem.model.Property;
import fiestaSystem.enums.TransactionType;
import fiestaSystem.enums.TransactionStatus;
import fiestaSystem.enums.PropertyStatus;
import java.util.ArrayList;

public class Customer extends User {

    private int txCounter = 0;
    private ArrayList<Transaction> myTransactions = new ArrayList<>();

    public Customer(String id, String name) {
        super(id, name, "CUSTOMER");
    }

    @Override
    public void login() {}

    // Returns only AVAILABLE properties matching the given filters.
    // block=0 = all blocks, minSqm=0 and maxPrice=0 = no limit.
    public ArrayList<Property> filterProperties(ArrayList<Property> allProperties,
                                                 int block, double minSqm, double maxPrice) {
        ArrayList<Property> result = new ArrayList<>();
        for (Property p : allProperties) {
            boolean blockMatch = (block == 0 || p.getBlockNumber() == block);
            boolean sqmMatch   = (p.getSqm() >= minSqm);
            boolean priceMatch = (maxPrice == 0 || p.getPrice() <= maxPrice);
            boolean available  = (p.getStatus() == PropertyStatus.AVAILABLE);
            if (blockMatch && sqmMatch && priceMatch && available) {
                result.add(p);
            }
        }
        return result;
    }

    public double calculateMortgage(double principal, int years, PaymentStrategy strategy) {
        return strategy.calculateMonthlyPayment(principal, years);
    }

    // True if this customer has an active (non-rejected) transaction on the property
    public boolean ownsProperty(Property p) {
        for (Transaction tx : myTransactions) {
            if (tx.getProperty().getId().equals(p.getId())
                    && tx.getStatus() != TransactionStatus.REJECTED) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Transaction> getMyTransactions() {
        return myTransactions;
    }

    public void submitReservation(Property property, Agent agent) {
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            return;
        }
        txCounter++;
        String txId = "TX-" + id + "-" + txCounter;
        Transaction tx = new Transaction(txId, property, this.id, this.getName(), 20000,
                                         TransactionType.RESERVATION, new CashPayment());
        property.updateStatus(PropertyStatus.RESERVED);
        agent.addTransaction(tx);
        myTransactions.add(tx);
    }

    // Accepts AVAILABLE or RESERVED — reserved is allowed when the customer already holds this unit
    public void submitPurchase(Property property, Agent agent, PaymentStrategy paymentMethod) {
        if (property.getStatus() != PropertyStatus.AVAILABLE
                && property.getStatus() != PropertyStatus.RESERVED) {
            return;
        }
        txCounter++;
        String txId = "TX-" + id + "-" + txCounter;
        Transaction tx = new Transaction(txId, property, this.id, this.getName(), property.getPrice(),
                                         TransactionType.PURCHASE, paymentMethod);
        property.updateStatus(PropertyStatus.RESERVED);
        agent.addTransaction(tx);
        myTransactions.add(tx);
    }
}
