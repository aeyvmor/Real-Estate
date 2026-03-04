package fiestaSystem.payment;

public class CashPayment implements PaymentStrategy {

    @Override
    public double calculateMonthlyPayment(double principal, int years) {
        return 0; // full upfront, no monthly
    }

    @Override
    public double getInterestRate() {
        return 0;
    }
}