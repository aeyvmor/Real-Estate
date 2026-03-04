package fiestaSystem.payment;

public class PagIbigLoan implements PaymentStrategy {

    private static final double ANNUAL_RATE = 0.065;

    @Override
    public double calculateMonthlyPayment(double principal, int years) {
        double monthlyRate = ANNUAL_RATE / 12;
        int n = years * 12;
        return principal * (monthlyRate * Math.pow(1 + monthlyRate, n))
                         / (Math.pow(1 + monthlyRate, n) - 1);
    }

    @Override
    public double getInterestRate() {
        return ANNUAL_RATE;
    }
}