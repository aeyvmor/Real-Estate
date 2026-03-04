package fiestaSystem.model;

import fiestaSystem.enums.PropertyStatus;

public class Property {

    private String id;
    private int blockNumber;
    private int lotNumber;
    private double price;
    private double sqm;
    private String facing;
    private PropertyStatus status;

    public Property(int blockNumber, int lotNumber, double price, double sqm, String facing) {
        this.blockNumber = blockNumber;
        this.lotNumber = lotNumber;
        this.price = price;
        this.sqm = sqm;
        this.facing = facing;
        this.status = PropertyStatus.AVAILABLE;
        this.id = "BLK" + blockNumber + "-L" + String.format("%02d", lotNumber);
    }

    public void updateStatus(PropertyStatus newStatus) {
        this.status = newStatus;
    }

    // Getters
    public String getId()               { return id; }
    public int getBlockNumber()         { return blockNumber; }
    public int getLotNumber()           { return lotNumber; }
    public double getPrice()            { return price; }
    public double getSqm()              { return sqm; }
    public String getFacing()           { return facing; }
    public PropertyStatus getStatus()   { return status; }

    @Override
    public String toString() {
        return id + " | Block " + blockNumber + " | " + sqm + " sqm | ₱" + price + " | " + status;
    }
}