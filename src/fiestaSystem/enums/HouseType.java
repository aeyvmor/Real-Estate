package fiestaSystem.enums;

/**
 * HouseType enum — represents each unit type offered by Fiesta Communities.
 * Each constant carries all fixed specs and fees for that house type.
 * Used by Property to determine pricing, fees, and display info.
 */
public enum HouseType {

    ROW_BARE(
        "Row House Bare",
        625_000,
        40,   // lot sqm
        20,   // floor sqm
        1,    // bedrooms
        1,    // bathrooms
        5_000,  // reservation fee
        55_000  // processing fee
    ),

    ROW_IMPROVED(
        "Row House Improved",
        625_000,
        40,
        30,
        2,
        1,
        5_000,
        55_000
    ),

    ROW_IMPROVED_END(
        "Row House Improved End Unit",
        700_000,
        40,
        30,
        2,
        1,
        5_000,
        60_000
    ),

    DUPLEX_2BR(
        "Duplex 2 Bedrooms",
        1_100_000,
        55,
        51,
        2,
        1,
        10_000,
        95_000
    ),

    DUPLEX_3BR(
        "Duplex 3 Bedrooms",
        1_200_000,
        55,
        55,
        3,
        1,
        10_000,
        105_000
    );

    // ── Fields ────────────────────────────────────────────────────────────────
    public final String  displayName;
    public final double  basePrice;
    public final int     lotSqm;
    public final int     floorSqm;
    public final int     bedrooms;
    public final int     bathrooms;
    public final double  reservationFee;
    public final double  processingFee;

    // ── Constructor ───────────────────────────────────────────────────────────
    HouseType(String displayName, double basePrice,
              int lotSqm, int floorSqm,
              int bedrooms, int bathrooms,
              double reservationFee, double processingFee) {
        this.displayName     = displayName;
        this.basePrice       = basePrice;
        this.lotSqm          = lotSqm;
        this.floorSqm        = floorSqm;
        this.bedrooms        = bedrooms;
        this.bathrooms       = bathrooms;
        this.reservationFee  = reservationFee;
        this.processingFee   = processingFee;
    }

    /**
     * Returns the image filename for this house type.
     * Images must be placed in src/fiestaSystem/ui/images/
     */
    public String getImageFileName() {
        switch (this) {
            case ROW_BARE:          return "row_bare.png";
            case ROW_IMPROVED:      return "row_improved.png";
            case ROW_IMPROVED_END:  return "row_improved_end.png";
            case DUPLEX_2BR:        return "duplex_2br.png";
            case DUPLEX_3BR:        return "duplex_3br.png";
            default:                return null;
        }
    }

    /**
     * Pag-IBIG interest rate varies by price bracket (Circular 310).
     * Below 750k = 7.375%, 750k and above = 6.5%
     */
    public double getPagIbigRate() {
        return (basePrice < 750_000) ? 0.07375 : 0.065;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
