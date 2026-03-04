package fiestaSystem.payment;

public interface PaymentStrategy {
    double calculateMonthlyPayment(double principal, int years);
    double getInterestRate();
}