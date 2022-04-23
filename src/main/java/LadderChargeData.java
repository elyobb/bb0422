public class LadderChargeData extends ToolChargeData {

    @Override
    public double getDailyRentalCharge() {
        return 1.99;
    }

    @Override
    public boolean hasWeekendCharge() {
        return true;
    }

    @Override
    public boolean hasHolidayCharge() {
        return false;
    }
}
