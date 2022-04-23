import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Produces an agreement for tool rental. The agreement details the number of days for the tool rental.
 * Depending on the tool being rented, holidays and/or weekends may be exempt from a daily charge. The agreement
 * details the number of days which are applicable for a rental charge, and the final amount will reflect any discount
 * provided by the employee.
 */
public class RentalAgreement {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");
    // used in intermediate calculations, this decimal format will specify a "round up" behavior
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    // the final rental charge does not need to be rounded, so it needs to be a separate decimal format
    private static final DecimalFormat finalAmountDecimalFormat =  new DecimalFormat("0.00");
    // handles adding the local currency symbol (such as '$') to a value representing currency
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
     * Constructs the rental agreement for a tool rental.
     *
     * @param toolCode the type of tool being rented. Must correspond to a tool code stored by {@link Tools}.
     * @param rentalDayCount the number of days in the rental period
     * @param discountPercent the discount percentage to apply to the final rental charge
     * @param checkoutDate the start of the rental period
     *
     * @return the fully-constructed and formatted rental agreement
     */
    public static String checkout(String toolCode, int rentalDayCount, int discountPercent, Date checkoutDate)
    {
        if(rentalDayCount < 1)
        {
            throw new IllegalArgumentException("Rental day count must be at least 1.");
        }

        if(discountPercent < 0 || discountPercent > 100)
        {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        final Tool tool = Optional.ofNullable(Tools.getTool(toolCode))
                    .orElseThrow(() -> new IllegalArgumentException("Tool code does not correspond to a known tool type."));
        final ToolChargeData toolChargeData = tool.getChargeData();

        final Date dueDate = ToolRentalController.calculateDueDate(checkoutDate, rentalDayCount);
        final int chargeDays = ToolRentalController.calculateChargeDays(toolChargeData, checkoutDate, dueDate, rentalDayCount);
        final double dailyCharge = toolChargeData.getDailyRentalCharge();
        decimalFormat.setRoundingMode(RoundingMode.UP);
        final double preDiscountCharge = Double.parseDouble(decimalFormat.format(chargeDays * dailyCharge));
        final double discountAmount = Double.parseDouble(decimalFormat.format(preDiscountCharge * ((double)discountPercent / 100)));
        final double finalCharge = Double.parseDouble(finalAmountDecimalFormat.format(preDiscountCharge - discountAmount));

        return "Tool code: " + toolCode + "\n" +
                "Tool type: " + tool.getToolType() + "\n" +
                "Tool brand: " + tool.getToolBrand() + "\n" +
                "Rental days: " + rentalDayCount + "\n" +
                "Checkout date: " + simpleDateFormat.format(checkoutDate) + "\n" +
                "Due date: " + simpleDateFormat.format(dueDate) + "\n" +
                "Daily rental charge: " + currencyFormat.format(dailyCharge) + "\n" +
                "Charge days: " + chargeDays + "\n" +
                "Pre-discount charge: " + currencyFormat.format(preDiscountCharge) + "\n" +
                "Discount percent: " + discountPercent + "%\n" +
                "Discount amount: " + currencyFormat.format(discountAmount) + "\n" +
                "Final charge: " + currencyFormat.format(finalCharge);
    }
}
