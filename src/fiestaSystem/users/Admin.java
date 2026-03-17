package fiestaSystem.users;

import fiestaSystem.model.User;
import fiestaSystem.model.Property;
import fiestaSystem.enums.PropertyStatus;
import fiestaSystem.enums.HouseType;
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
        for (int block = 1; block <= 5; block++) {
            for (int lot = 1; lot <= 20; lot++) {
                HouseType ht;
                switch (block) {
                    case 1:  ht = HouseType.ROW_BARE;     break;
                    case 2:
                    case 3:  ht = (lot == 1 || lot == 20)
                                  ? HouseType.ROW_IMPROVED_END
                                  : HouseType.ROW_IMPROVED; break;
                    case 4:  ht = HouseType.DUPLEX_2BR;   break;
                    default: ht = HouseType.DUPLEX_3BR;   break;
                }
                double price  = ht.basePrice + (lot * 5000);
                String facing = facings[lot % 4];
                properties.add(new Property(block, lot, price, ht.lotSqm, facing, ht));
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

    public boolean removeProperty(String id) {
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getId().equals(id)) {
                properties.remove(i);
                System.out.println("Property removed: " + id);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * Filters properties by block, minimum sqm, and maximum price.
     * block=0 means all blocks. minSqm=0 and maxPrice=0 means no limit.
     */
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
        sb.append("╔══════════════════════════════════════════════════════╗\n");
        sb.append("║              MASTER LOT REPORT                       ║\n");
        sb.append("╠══════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Total Lots  : %-36d║\n", properties.size()));
        sb.append(String.format("║  Available   : %-36d║\n", available));
        sb.append(String.format("║  Reserved    : %-36d║\n", reserved));
        sb.append(String.format("║  Sold        : %-36d║\n", sold));
        sb.append(String.format("║  Revenue     : ₱%-35s║\n", String.format("%,.2f", revenue)));
        sb.append("╠══════════════════════════════════════════════════════╣\n");
        sb.append(String.format("  %-10s  %-6s  %-22s  %-9s  %s\n",
                "ID", "BLOCK", "HOUSE TYPE", "STATUS", "PRICE"));
        sb.append("  " + "─".repeat(68) + "\n");
        for (Property p : properties) {
            String typeName = p.getHouseType() != null ? p.getHouseType().displayName : "N/A";
            sb.append(String.format("  %-10s  BLK%-3d  %-22s  %-9s  ₱%,.0f\n",
                p.getId(),
                p.getBlockNumber(),
                typeName,
                p.getStatus(),
                p.getPrice()));
        }
        sb.append("╚══════════════════════  ════════════════════════════════╝\n");

        System.out.println(sb.toString());
        return sb.toString();
    }
}