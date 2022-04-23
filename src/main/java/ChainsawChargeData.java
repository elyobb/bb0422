/**
 * Daily charge information specific to renting a chainsaw.
 */
public class ChainsawChargeData extends ToolChargeData {

    @Override
    public double getDailyRentalCharge() {
        return 1.49;
    }

    @Override
    public boolean hasWeekendCharge() {
        return false;
    }

    @Override
    public boolean hasHolidayCharge() {
        return true;
    }
}
