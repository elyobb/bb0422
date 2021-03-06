/**
 * Daily charge information specific to renting a ladder.
 */
public class LadderChargeData extends ToolChargeData {

    @Override
    public double getDailyRentalCharge() {
        return 1.99;
    }

    @Override
    public boolean hasHolidayCharge() {
        return false;
    }

    @Override
    public boolean hasWeekendCharge() {
        return true;
    }

}
