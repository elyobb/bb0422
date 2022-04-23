/**
 * Represents a given tool's unique daily rental charge properties.
 */
public abstract class ToolChargeData {

    public abstract double getDailyRentalCharge();
    public abstract boolean hasWeekendCharge();
    public abstract boolean hasHolidayCharge();
}
