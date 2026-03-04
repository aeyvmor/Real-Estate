package fiestaSystem.model;

import fiestaSystem.payment.PaymentStrategy;
import fiestaSystem.model.Property;
import fiestaSystem.enums.TransactionStatus;
import fiestaSystem.enums.TransactionType;
import fiestaSystem.enums.PropertyStatus;

public class Transaction {

    private String id;
    private Property property;
    private String customerName;
    private double amount;
    private TransactionType type;
    private TransactionStatus status;
    private PaymentStrategy paymentMethod;

    public Transaction(String id, Property property, String customerName,
                       double amount, TransactionType type, PaymentStrategy paymentMethod) {
        this.id = id;
        this.property = property;
        this.customerName = customerName;
        this.amount = amount;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.status = TransactionStatus.PENDING;
    }

    public void process() {
        System.out.println("Processing transaction: " + id);
    }

    public void complete() {
        this.status = TransactionStatus.APPROVED;
        if (type == TransactionType.RESERVATION) {
            property.updateStatus(PropertyStatus.RESERVED);
        } else if (type == TransactionType.PURCHASE) {
            property.updateStatus(PropertyStatus.SOLD);
        }
        System.out.println("Transaction " + id + " completed. Property: " + property.getStatus());
    }

    public void reject() {
        this.status = TransactionStatus.REJECTED;
        property.updateStatus(PropertyStatus.AVAILABLE);
        System.out.println("Transaction " + id + " rejected. Property is now AVAILABLE.");
    }

    public String getId()                     { return id; }
    public Property getProperty()             { return property; }
    public String getCustomerName()           { return customerName; }
    public double getAmount()                 { return amount; }
    public TransactionType getType()          { return type; }
    public TransactionStatus getStatus()      { return status; }
    public PaymentStrategy getPaymentMethod() { return paymentMethod; }

    @Override
    public String toString() {
        return id + " | " + type + " | " + property.getId() 
             + " | P" + amount + " | " + status + " | " + customerName;
    }
}