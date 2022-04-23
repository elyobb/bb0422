import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.firstInMonth;

/**
 * Handles most of the business logic of creating a rental agreement. For a given rental period, calculates how many days qualify
 * for a rental charge. Depending on the type of tool being rented, holidays and/or weekends may be exempt from a rental charge.
 */
public class ToolRentalController {

    public static LocalDate calculateDueDate(LocalDate startDate, int numberOfDays)
    {
        return LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth()).plusDays(numberOfDays);
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
    public static int calculateChargeDays(ToolChargeData toolChargeData, LocalDate rentalDate, LocalDate dueDate, int numberOfDays)
    {
        int daysToSubtract = 0;
        // deduct any holidays from the rental period if needed
        if(!toolChargeData.hasHolidayCharge())
        {
            daysToSubtract += determineNumberOfHolidays(rentalDate, dueDate);
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
     *
     * @return the number of holidays identified within the rental period, between 0 and 2.
     */
    private static int determineNumberOfHolidays(LocalDate rentalDate, LocalDate dueDate)
    {
        int numberOfHolidays = 0;
        int currentYear = rentalDate.getYear();

        LocalDate julyFourth = LocalDate.of(currentYear, 7, 4);
        DayOfWeek julyFourthDayOfWeek = julyFourth.getDayOfWeek();

        if(julyFourthDayOfWeek == DayOfWeek.SATURDAY)
        {
            julyFourth = julyFourth.plusDays(-1);
        }
        else if (julyFourthDayOfWeek == DayOfWeek.SUNDAY)
        {
            julyFourth = julyFourth.plusDays(1);
        }

        // we count the holiday even if it happens to be on a weekend - it gets moved to the nearest weekday,
        // so we always count the holiday as well as the weekend days later on
        if(rentalDate.isBefore(julyFourth) && (dueDate.isAfter(julyFourth) || dueDate.isEqual(julyFourth)))
        {
            numberOfHolidays++;
        }

        LocalDate laborDay = LocalDate.of(currentYear, 9, 1).with(firstInMonth(DayOfWeek.MONDAY));

        if(rentalDate.isBefore(laborDay) && (dueDate.isAfter(laborDay) || dueDate.isEqual(laborDay)))
        {
            numberOfHolidays++;
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
    private static int determineNumberOfWeekendDays(LocalDate rentalDate, LocalDate dueDate)
    {
        int numWeekendDays = 0;
        for(LocalDate startDate = rentalDate; startDate.compareTo(dueDate) <= 0; startDate = startDate.plusDays(1))
        {
            DayOfWeek dayOfWeek = startDate.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
            {
                numWeekendDays++;
            }
        }
        return numWeekendDays;
    }
}
