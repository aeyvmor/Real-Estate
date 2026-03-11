package fiestaSystem.users;

import fiestaSystem.model.User;
import fiestaSystem.model.Property;
import fiestaSystem.enums.PropertyStatus;
import java.util.ArrayList;

public class Admin extends User {

    private ArrayList<Property> properties;

    public Admin(String id, String name) {
        super(id, name, "ADMIN");
        this.properties = new ArrayList<>();
        // seedData(); // commented out — admin adds lots manually via UI
    }

    /*
    private void seedData() {
        String[] facings = {"NORTH", "SOUTH", "EAST", "WEST"};
        double[] basePrices = {625000, 700000, 900000, 1100000, 1200000};
        double[] sqmOptions = {40, 40, 55, 55, 40};

        for (int block = 1; block <= 5; block++) {
            for (int lot = 1; lot <= 20; lot++) {
                double basePrice = basePrices[block - 1];
                double price = basePrice + (lot * 5000);
                double sqm = sqmOptions[block - 1];
                String facing = facings[lot % 4];
                properties.add(new Property(block, lot, price, sqm, facing));
            }
        }
        System.out.println("Seeded " + properties.size() + " properties.");
    }
    */

    @Override
    public void login() {
        System.out.println("Admin " + name + " logged in.");
    }

    public void addProperty(Property p) {
        properties.add(p);
        System.out.println("Property added: " + p.getId());
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public ArrayList<Property> filterProperties(int block, double minSqm, double maxPrice) {
        ArrayList<Property> result = new ArrayList<>();
        for (Property p : properties) {
            boolean blockMatch = (block == 0 || p.getBlockNumber() == block);
            boolean sqmMatch   = (p.getSqm() >= minSqm);
            boolean priceMatch = (maxPrice == 0 || p.getPrice() <= maxPrice);
            if (blockMatch && sqmMatch && priceMatch) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * Returns a formatted report string for display in the UI dialog.
     */
    public String generateReport() {
        int sold = 0, reserved = 0, available = 0;
        double revenue = 0;
        for (Property p : properties) {
            switch (p.getStatus()) {
                case SOLD:      sold++;     revenue += p.getPrice(); break;
                case RESERVED:  reserved++; break;
                case AVAILABLE: available++; break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════╗\n");
        sb.append("║       MASTER LOT REPORT      ║\n");
        sb.append("╠══════════════════════════════╣\n");
        sb.append(String.format("║  Total Lots  : %-14d║\n", properties.size()));
        sb.append(String.format("║  Available   : %-14d║\n", available));
        sb.append(String.format("║  Reserved    : %-14d║\n", reserved));
        sb.append(String.format("║  Sold        : %-14d║\n", sold));
        sb.append(String.format("║  Revenue     : ₱%-13s║\n", String.format("%,.2f", revenue)));
        sb.append("╠══════════════════════════════╣\n");
        sb.append("║  LOT BREAKDOWN               ║\n");
        sb.append("╠══════════════════════════════╣\n");
        for (Property p : properties) {
            sb.append(String.format("  %-10s │ BLK%-2d │ %-9s │ ₱%,.0f\n",
                p.getId(), p.getBlockNumber(), p.getStatus(), p.getPrice()));
        }
        sb.append("╚══════════════════════════════╝\n");

        // Also print to console
        System.out.println(sb.toString());
        return sb.toString();
    }
}