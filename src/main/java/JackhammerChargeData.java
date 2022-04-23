public class JackhammerChargeData extends ToolChargeData {

    @Override
    public double getDailyRentalCharge() {
        return 2.99;
    }

    @Override
    public boolean hasWeekendCharge() {
        return false;
    }

    @Override
    public boolean hasHolidayCharge() {
        return false;
    }
}
