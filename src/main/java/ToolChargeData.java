/**
 * Contains info for a given tool's daily rental price and whether a charge is incurred on holidays or weekends.
 */
public abstract class ToolChargeData {

    public abstract double getDailyRentalCharge();
    public abstract boolean hasHolidayCharge();
    public abstract boolean hasWeekendCharge();
}
