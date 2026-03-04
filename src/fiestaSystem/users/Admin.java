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
        seedData();
    }

    private void seedData() {
        String[] facings = {"NORTH", "SOUTH", "EAST", "WEST"};
        // Base prices per block (increases per block)
        double[] basePrices = {625000, 700000, 900000, 1100000, 1200000};
        double[] sqmOptions = {40, 40, 55, 55, 40};

        for (int block = 1; block <= 5; block++) {
            for (int lot = 1; lot <= 20; lot++) {
                double basePrice = basePrices[block - 1];
                // slight variation per lot
                double price = basePrice + (lot * 5000);
                double sqm = sqmOptions[block - 1];
                String facing = facings[lot % 4];
                properties.add(new Property(block, lot, price, sqm, facing));
            }
        }
        System.out.println("Seeded " + properties.size() + " properties.");
    }

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
            boolean available  = (p.getStatus() == PropertyStatus.AVAILABLE);
            if (blockMatch && sqmMatch && priceMatch && available) {
                result.add(p);
            }
        }
        return result;
    }

    public void generateReport() {
        int sold = 0, reserved = 0, available = 0;
        double revenue = 0;
        for (Property p : properties) {
            switch (p.getStatus()) {
                case SOLD:       sold++;      revenue += p.getPrice(); break;
                case RESERVED:   reserved++;  break;
                case AVAILABLE:  available++; break;
            }
        }
        System.out.println("=== MASTER REPORT ===");
        System.out.println("Total Lots  : " + properties.size());
        System.out.println("Available   : " + available);
        System.out.println("Reserved    : " + reserved);
        System.out.println("Sold        : " + sold);
        System.out.println("Revenue     : P" + String.format("%.2f", revenue));
        System.out.println("=====================");
        for (Property p : properties) {
            System.out.println(p);
        }
    }
}