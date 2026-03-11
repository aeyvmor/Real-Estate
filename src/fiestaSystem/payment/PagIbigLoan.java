package fiestaSystem.payment;

/**
 * PagIbigLoan — Circular 310 compliant.
 * Rate varies by property price bracket:
 *   below ₱750,000  → 7.375%
 *   ₱750,000+       → 6.5%
 * The rate is passed in at construction time from HouseType.getPagIbigRate().
 */
public class PagIbigLoan implements PaymentStrategy {

    private final double annualRate;

    /** Default constructor keeps backward compatibility (6.5%) */
    public PagIbigLoan() {
        this.annualRate = 0.065;
    }

    /** Use this constructor to pass in the correct rate from HouseType */
    public PagIbigLoan(double annualRate) {
        this.annualRate = annualRate;
    }

    @Override
    public double calculateMonthlyPayment(double principal, int years) {
        double monthlyRate = annualRate / 12;
        int n = years * 12;
        return principal * (monthlyRate * Math.pow(1 + monthlyRate, n))
                         / (Math.pow(1 + monthlyRate, n) - 1);
    }

    @Override
    public double getInterestRate() {
        return annualRate;
    }
}