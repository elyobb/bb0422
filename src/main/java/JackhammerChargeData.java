/**
 * Daily charge information specific to renting a jackhammer.
 */
public class JackhammerChargeData extends ToolChargeData {

    @Override
    public double getDailyRentalCharge() {
        return 2.99;
    }

    @Override
    public boolean hasHolidayCharge() {
        return false;
    }

    @Override
    public boolean hasWeekendCharge() {
        return false;
    }
}
