import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Controller {

    public static Date calculateDueDate(Date startDate, int numberOfDays)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
        return calendar.getTime();
    }

    public static int calculateChargeDays(ToolChargeData toolChargeData, Date rentalDate, Date dueDate, int numberOfDays)
    {
        /**
         * Independence Day, July 4th - If falls on weekend, it is observed on the closest weekday (if Sat,
         * then Friday before, if Sunday, then Monday after)
         *
         * Labor Day - First Monday in September
         */
        int daysToSubtract = 0;
        if(!toolChargeData.hasHolidayCharge())
        {
            daysToSubtract += determineNumberOfHolidays(rentalDate, dueDate, toolChargeData.hasWeekendCharge());
        }
        if(!toolChargeData.hasWeekendCharge())
        {
            daysToSubtract += determineNumberOfWeekendDays(rentalDate, dueDate);
        }

        return numberOfDays - daysToSubtract;
    }

    private static int determineNumberOfHolidays(Date rentalDate, Date dueDate, boolean hasWeekendCharge)
    {
        int numHolidays = 0;
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
                numHolidays++;
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
                numHolidays++;
            }
        }

        return numHolidays;
    }
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
            DayOfWeek dayOfWeek = DayOfWeek.of(dayOfWeekIndex);
            if(dayOfWeekIndex == 1 || dayOfWeekIndex == 7)
            {
                numWeekendDays++;
            }
            rentalDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return numWeekendDays;
    }
}
