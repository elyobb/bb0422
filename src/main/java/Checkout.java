import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Produces a {@link ToolRentalAgreement} via {@link #createRentalAgreement(String, int, int, LocalDate)}.
 */
public class Checkout {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy");

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
     * @param numberOfDays the number of days in the rental period
     * @param discountPercent the discount percentage to apply to the final rental charge
     * @param checkoutDate the start of the rental period
     *
     * @return the fully-constructed {@link ToolRentalAgreement}
     */
    public static ToolRentalAgreement createRentalAgreement(String toolCode, int numberOfDays, int discountPercent, LocalDate checkoutDate)
    {
        if(numberOfDays < 1)
        {
            throw new IllegalArgumentException("Rental day count must be at least 1.");
        }

        if(discountPercent < 0 || discountPercent > 100)
        {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        final Tool tool = Optional.ofNullable(Tools.getTool(toolCode))
                    .orElseThrow(() -> new IllegalArgumentException("Tool code does not correspond to an existing tool."));
        final ToolChargeData toolChargeData = tool.getChargeData();

        final LocalDate dueDate = ToolRentalController.calculateDueDate(checkoutDate, numberOfDays);
        final int chargeDays = ToolRentalController.calculateChargeDays(toolChargeData, checkoutDate, dueDate, numberOfDays);
        final double dailyRentalCharge = toolChargeData.getDailyRentalCharge();
        decimalFormat.setRoundingMode(RoundingMode.UP);
        final double preDiscountCharge = Double.parseDouble(decimalFormat.format(chargeDays * dailyRentalCharge));
        final double discountAmount = Double.parseDouble(decimalFormat.format(preDiscountCharge * ((double)discountPercent / 100)));
        final double finalCharge = Double.parseDouble(finalAmountDecimalFormat.format(preDiscountCharge - discountAmount));


        return new ToolRentalAgreement(toolCode, tool.getType().getValue(), tool.getBrand(), numberOfDays, checkoutDate.format(formatter),
                dueDate.format(formatter), currencyFormat.format(dailyRentalCharge), chargeDays, currencyFormat.format(preDiscountCharge),
                discountPercent, currencyFormat.format(discountAmount), currencyFormat.format(finalCharge));
    }
}
