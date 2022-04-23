import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Handles the business logic of creating a rental agreement. For a given rental period, calculates how many days qualify
 * for a rental charge. Depending on the type of tool being rented, holidays and/or weekends may be exempt from a rental charge.
 */
public class ToolRentalController {

    public static Date calculateDueDate(Date startDate, int numberOfDays)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
        return calendar.getTime();
    }

    /**
     * Calculates the total number of billable days for a tool rental agreement. If holidays and/or weekends fall in the
     * rental period they may be exempt from a rental charge, depending on the type of tool being rented.
     *
     * @param toolChargeData contains the daily charge data and whether the tool has a daily charge on holidays and weekends
     * @param rentalDate the date of the start of the tool rental
     * @param dueDate the date when the tool is due for return
     * @param numberOfDays the number of days in the rental period
     * @return the number of days in the rental period for which the customer will be charged
     */
    public static int calculateChargeDays(ToolChargeData toolChargeData, Date rentalDate, Date dueDate, int numberOfDays)
    {
        int daysToSubtract = 0;
        // deduct any holidays from the rental period if needed
        if(!toolChargeData.hasHolidayCharge())
        {
            daysToSubtract += determineNumberOfHolidays(rentalDate, dueDate, toolChargeData.hasWeekendCharge());
        }
        // deduct any weekend days from the rental period if needed
        if(!toolChargeData.hasWeekendCharge())
        {
            daysToSubtract += determineNumberOfWeekendDays(rentalDate, dueDate);
        }

        return numberOfDays - daysToSubtract;
    }

    /**
     * Determines the number of holidays which fall in the tool rental period. The holidays recognized by the rental agreement
     * are Independence Day and Labor Day.
     *
     * @param rentalDate the date of the start of the tool rental
     * @param dueDate the date when the tool is due for return
     * @param hasWeekendCharge used when a holiday happens to fall upon a weekend --
     *                         in this case we will ignore the holiday and instead the day will be deducted as
     *                         a weekend day in {@link #determineNumberOfWeekendDays(Date, Date)}. This is because
     *                         even if a holiday falls on a weekend, it is still just a single day exempt from rental charge
     *
     * @return the number of holidays identified within the rental period, between 0 and 2.
     */
    private static int determineNumberOfHolidays(Date rentalDate, Date dueDate, boolean hasWeekendCharge)
    {
        int numberOfHolidays = 0;
        Calendar rentalDateCalendar = Calendar.getInstance();
        rentalDateCalendar.setTime(rentalDate);
        int currentYear = rentalDateCalendar.get(Calendar.YEAR);

        Calendar julyFourthCalendar = new GregorianCalendar(currentYear, Calendar.JULY, 4);
        Date julyFourth = julyFourthCalendar.getTime();
        DayOfWeek julyFourthDayOfWeek = DayOfWeek.of(julyFourthCalendar.get(Calendar.DAY_OF_WEEK));
        boolean julyFourthOnWeekend = julyFourthDayOfWeek == DayOfWeek.SATURDAY || julyFourthDayOfWeek == DayOfWeek.SUNDAY;

        if(hasWeekendCharge || !julyFourthOnWeekend)
        {
            if(rentalDate.before(julyFourth) && dueDate.compareTo(julyFourth) >= 0)
            {
                numberOfHolidays++;
            }
        }

        Calendar laborDayCalendar = new GregorianCalendar();
        laborDayCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        laborDayCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        laborDayCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
        laborDayCalendar.set(Calendar.YEAR, currentYear);
        Date laborDay = laborDayCalendar.getTime();
        DayOfWeek laborDayDayOfWeek = DayOfWeek.of(laborDayCalendar.get(Calendar.DAY_OF_WEEK));
        boolean laborDayOnWeekend = laborDayDayOfWeek == DayOfWeek.SATURDAY || laborDayDayOfWeek == DayOfWeek.SUNDAY;

        if(hasWeekendCharge || !laborDayOnWeekend)
        {
            if(rentalDate.before(laborDay) && dueDate.compareTo(laborDay) >= 0)
            {
                numberOfHolidays++;
            }
        }

        return numberOfHolidays;
    }

    /**
     * Determines the number of days in the rental period which fall upon a weekend (Saturday or Sunday).
     *
     * @param rentalDate the date of the start of the tool rental
     * @param dueDate the date when the tool is due for return
     *
     * @return the number of days which fall upon a weekend
     */
    private static int determineNumberOfWeekendDays(Date rentalDate, Date dueDate)
    {
        Calendar rentalDateCalendar = Calendar.getInstance();
        rentalDateCalendar.setTime(rentalDate);

        Calendar dueDateCalendar = Calendar.getInstance();
        dueDateCalendar.setTime(dueDate);
        int numWeekendDays = 0;
        while(rentalDateCalendar.get(Calendar.DATE) <= dueDateCalendar.get(Calendar.DATE))
        {
            int dayOfWeekIndex = rentalDateCalendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeekIndex == 1 || dayOfWeekIndex == 7)
            {
                numWeekendDays++;
            }
            rentalDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return numWeekendDays;
    }
}
