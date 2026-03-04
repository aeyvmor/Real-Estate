package fiestaSystem;

import fiestaSystem.users.Admin;
import fiestaSystem.users.Agent;
import fiestaSystem.users.Customer;
import fiestaSystem.payment.PagIbigLoan;
import fiestaSystem.model.Property;

public class TestRun {
    public static void main(String[] args) {

        Admin admin = new Admin("A01", "Maria");
        Agent agent = new Agent("AG01", "Jose");
        Customer customer = new Customer("C01", "Juan");

        // Filter
        System.out.println("\n--- Available in Block 1 under 700k ---");
        for (Property p : customer.filterProperties(admin.getProperties(), 1, 0, 700000)) {
            System.out.println(p);
        }

        // Mortgage
        PagIbigLoan pagibig = new PagIbigLoan();
        double monthly = customer.calculateMortgage(625000, 30, pagibig);
        System.out.println("\nPag-IBIG monthly (625k, 30yr): P" + String.format("%.2f", monthly));

        // Reserve
        Property lot = admin.getProperties().get(0);
        customer.submitReservation(lot, agent);

        // Agent approves
        System.out.println("\n--- Agent Approves ---");
        agent.approveTransaction("TX-C01-1");

        // Report
        System.out.println();
        admin.generateReport();
    }
}