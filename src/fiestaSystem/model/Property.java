package fiestaSystem.model;

import fiestaSystem.enums.PropertyStatus;
import fiestaSystem.enums.HouseType;

public class Property {

    private String       id;
    private int          blockNumber;
    private int          lotNumber;
    private double       price;       // can be adjusted from HouseType base price
    private double       sqm;         // lot sqm — from HouseType but overridable
    private String       facing;
    private PropertyStatus status;
    private HouseType    houseType;

    /**
     * Primary constructor — price and sqm auto-set from HouseType, but passed
     * in so admin can adjust them before saving.
     */
    public Property(int blockNumber, int lotNumber, double price,
                    double sqm, String facing, HouseType houseType) {
        this.blockNumber = blockNumber;
        this.lotNumber   = lotNumber;
        this.price       = price;
        this.sqm         = sqm;
        this.facing      = facing;
        this.houseType   = houseType;
        this.status      = PropertyStatus.AVAILABLE;
        this.id          = "BLK" + blockNumber + "-L" + String.format("%02d", lotNumber);
    }

    public void updateStatus(PropertyStatus newStatus) {
        this.status = newStatus;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String         getId()          { return id; }
    public int            getBlockNumber() { return blockNumber; }
    public int            getLotNumber()   { return lotNumber; }
    public double         getPrice()       { return price; }
    public double         getSqm()         { return sqm; }
    public String         getFacing()      { return facing; }
    public PropertyStatus getStatus()      { return status; }
    public HouseType      getHouseType()   { return houseType; }

    /** Convenience: reservation fee comes from the house type */
    public double getReservationFee() {
        return houseType != null ? houseType.reservationFee : 20_000;
    }

    /** Convenience: processing fee comes from the house type */
    public double getProcessingFee() {
        return houseType != null ? houseType.processingFee : 0;
    }

    @Override
    public String toString() {
        String type = houseType != null ? houseType.displayName : "N/A";
        return id + " | Block " + blockNumber
             + " | " + sqm + " sqm | ₱" + price
             + " | " + type + " | " + status;
    }
}