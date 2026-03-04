package fiestaSystem.users;

import fiestaSystem.model.Transaction;
import fiestaSystem.payment.PaymentStrategy;
import fiestaSystem.payment.CashPayment;
import fiestaSystem.model.User;
import fiestaSystem.model.Property;
import fiestaSystem.enums.TransactionType;
import fiestaSystem.enums.PropertyStatus;
import java.util.ArrayList;

public class Customer extends User {

    private int txCounter = 0;

    public Customer(String id, String name) {
        super(id, name, "CUSTOMER");
    }

    @Override
    public void login() {
        System.out.println("Customer " + name + " logged in.");
    }

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

    public void submitReservation(Property property, Agent agent) {
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            System.out.println("Property is not available.");
            return;
        }
        txCounter++;
        String txId = "TX-" + id + "-" + txCounter;
        Transaction tx = new Transaction(txId, property, this.getName(), 20000,
                                         TransactionType.RESERVATION, new CashPayment());
        property.updateStatus(PropertyStatus.RESERVED);
        agent.addTransaction(tx);
        System.out.println("Reservation submitted: " + txId);
    }

    public void submitPurchase(Property property, Agent agent, PaymentStrategy paymentMethod) {
        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            System.out.println("Property is not available.");
            return;
        }
        txCounter++;
        String txId = "TX-" + id + "-" + txCounter;
        Transaction tx = new Transaction(txId, property, this.getName(), property.getPrice(),
                                         TransactionType.PURCHASE, paymentMethod);
        property.updateStatus(PropertyStatus.RESERVED);
        agent.addTransaction(tx);
        System.out.println("Purchase submitted: " + txId);
    }
}