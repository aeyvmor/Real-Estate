package fiestaSystem;

import fiestaSystem.users.Admin;
import fiestaSystem.users.Agent;
import fiestaSystem.users.Customer;
import fiestaSystem.payment.PagIbigLoan;
import fiestaSystem.model.Property;
import fiestaSystem.enums.HouseType;

public class TestRun {
    public static void main(String[] args) {

        Admin admin    = new Admin("A01", "Maria");
        Agent agent    = new Agent("AG01", "Jose");
        Customer customer = new Customer("C01", "Juan");

        // Manually add a few test properties (since seedData is commented out)
        admin.addProperty(new Property(1, 1,
                HouseType.ROW_BARE.basePrice,
                HouseType.ROW_BARE.lotSqm,
                "NORTH", HouseType.ROW_BARE));

        admin.addProperty(new Property(1, 2,
                HouseType.ROW_IMPROVED.basePrice,
                HouseType.ROW_IMPROVED.lotSqm,
                "SOUTH", HouseType.ROW_IMPROVED));

        admin.addProperty(new Property(2, 1,
                HouseType.DUPLEX_3BR.basePrice,
                HouseType.DUPLEX_3BR.lotSqm,
                "EAST", HouseType.DUPLEX_3BR));

        // Filter
        System.out.println("\n--- Available in Block 1 under 700k ---");
        for (Property p : customer.filterProperties(admin.getProperties(), 1, 0, 700000)) {
            System.out.println(p);
        }

        // Mortgage — use house-type-aware Pag-IBIG rate
        HouseType ht = HouseType.ROW_IMPROVED;
        PagIbigLoan pagibig = new PagIbigLoan(ht.getPagIbigRate());
        double monthly = customer.calculateMortgage(ht.basePrice, 30, pagibig);
        System.out.println("\nPag-IBIG monthly (Row Improved, 30yr): ₱"
                + String.format("%.2f", monthly));

        // Reserve first available lot
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